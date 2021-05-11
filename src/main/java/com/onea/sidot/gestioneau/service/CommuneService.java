package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.Commune;
import com.onea.sidot.gestioneau.repository.CommuneRepository;
import com.onea.sidot.gestioneau.repository.search.CommuneSearchRepository;
import com.onea.sidot.gestioneau.service.dto.CommuneDTO;
import com.onea.sidot.gestioneau.service.mapper.CommuneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commune}.
 */
@Service
@Transactional
public class CommuneService {

    private final Logger log = LoggerFactory.getLogger(CommuneService.class);

    private final CommuneRepository communeRepository;

    private final CommuneMapper communeMapper;

    private final CommuneSearchRepository communeSearchRepository;

    public CommuneService(
        CommuneRepository communeRepository,
        CommuneMapper communeMapper,
        CommuneSearchRepository communeSearchRepository
    ) {
        this.communeRepository = communeRepository;
        this.communeMapper = communeMapper;
        this.communeSearchRepository = communeSearchRepository;
    }

    /**
     * Save a commune.
     *
     * @param communeDTO the entity to save.
     * @return the persisted entity.
     */
    public CommuneDTO save(CommuneDTO communeDTO) {
        log.debug("Request to save Commune : {}", communeDTO);
        Commune commune = communeMapper.toEntity(communeDTO);
        commune = communeRepository.save(commune);
        CommuneDTO result = communeMapper.toDto(commune);
        communeSearchRepository.save(commune);
        return result;
    }

    /**
     * Partially update a commune.
     *
     * @param communeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommuneDTO> partialUpdate(CommuneDTO communeDTO) {
        log.debug("Request to partially update Commune : {}", communeDTO);

        return communeRepository
            .findById(communeDTO.getId())
            .map(
                existingCommune -> {
                    communeMapper.partialUpdate(existingCommune, communeDTO);
                    return existingCommune;
                }
            )
            .map(communeRepository::save)
            .map(
                savedCommune -> {
                    communeSearchRepository.save(savedCommune);

                    return savedCommune;
                }
            )
            .map(communeMapper::toDto);
    }

    /**
     * Get all the communes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommuneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Communes");
        return communeRepository.findAll(pageable).map(communeMapper::toDto);
    }

    /**
     * Get one commune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommuneDTO> findOne(Long id) {
        log.debug("Request to get Commune : {}", id);
        return communeRepository.findById(id).map(communeMapper::toDto);
    }

    /**
     * Delete the commune by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Commune : {}", id);
        communeRepository.deleteById(id);
        communeSearchRepository.deleteById(id);
    }

    /**
     * Search for the commune corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommuneDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Communes for query {}", query);
        return communeSearchRepository.search(queryStringQuery(query), pageable).map(communeMapper::toDto);
    }
}
