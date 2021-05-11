package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.Macon;
import com.onea.sidot.gestioneau.repository.MaconRepository;
import com.onea.sidot.gestioneau.repository.search.MaconSearchRepository;
import com.onea.sidot.gestioneau.service.dto.MaconDTO;
import com.onea.sidot.gestioneau.service.mapper.MaconMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Macon}.
 */
@Service
@Transactional
public class MaconService {

    private final Logger log = LoggerFactory.getLogger(MaconService.class);

    private final MaconRepository maconRepository;

    private final MaconMapper maconMapper;

    private final MaconSearchRepository maconSearchRepository;

    public MaconService(MaconRepository maconRepository, MaconMapper maconMapper, MaconSearchRepository maconSearchRepository) {
        this.maconRepository = maconRepository;
        this.maconMapper = maconMapper;
        this.maconSearchRepository = maconSearchRepository;
    }

    /**
     * Save a macon.
     *
     * @param maconDTO the entity to save.
     * @return the persisted entity.
     */
    public MaconDTO save(MaconDTO maconDTO) {
        log.debug("Request to save Macon : {}", maconDTO);
        Macon macon = maconMapper.toEntity(maconDTO);
        macon = maconRepository.save(macon);
        MaconDTO result = maconMapper.toDto(macon);
        maconSearchRepository.save(macon);
        return result;
    }

    /**
     * Partially update a macon.
     *
     * @param maconDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MaconDTO> partialUpdate(MaconDTO maconDTO) {
        log.debug("Request to partially update Macon : {}", maconDTO);

        return maconRepository
            .findById(maconDTO.getId())
            .map(
                existingMacon -> {
                    maconMapper.partialUpdate(existingMacon, maconDTO);
                    return existingMacon;
                }
            )
            .map(maconRepository::save)
            .map(
                savedMacon -> {
                    maconSearchRepository.save(savedMacon);

                    return savedMacon;
                }
            )
            .map(maconMapper::toDto);
    }

    /**
     * Get all the macons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaconDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Macons");
        return maconRepository.findAll(pageable).map(maconMapper::toDto);
    }

    /**
     * Get one macon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MaconDTO> findOne(Long id) {
        log.debug("Request to get Macon : {}", id);
        return maconRepository.findById(id).map(maconMapper::toDto);
    }

    /**
     * Delete the macon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Macon : {}", id);
        maconRepository.deleteById(id);
        maconSearchRepository.deleteById(id);
    }

    /**
     * Search for the macon corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MaconDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Macons for query {}", query);
        return maconSearchRepository.search(queryStringQuery(query), pageable).map(maconMapper::toDto);
    }
}
