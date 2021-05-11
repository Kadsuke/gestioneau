package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.Localite;
import com.onea.sidot.gestioneau.repository.LocaliteRepository;
import com.onea.sidot.gestioneau.repository.search.LocaliteSearchRepository;
import com.onea.sidot.gestioneau.service.dto.LocaliteDTO;
import com.onea.sidot.gestioneau.service.mapper.LocaliteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Localite}.
 */
@Service
@Transactional
public class LocaliteService {

    private final Logger log = LoggerFactory.getLogger(LocaliteService.class);

    private final LocaliteRepository localiteRepository;

    private final LocaliteMapper localiteMapper;

    private final LocaliteSearchRepository localiteSearchRepository;

    public LocaliteService(
        LocaliteRepository localiteRepository,
        LocaliteMapper localiteMapper,
        LocaliteSearchRepository localiteSearchRepository
    ) {
        this.localiteRepository = localiteRepository;
        this.localiteMapper = localiteMapper;
        this.localiteSearchRepository = localiteSearchRepository;
    }

    /**
     * Save a localite.
     *
     * @param localiteDTO the entity to save.
     * @return the persisted entity.
     */
    public LocaliteDTO save(LocaliteDTO localiteDTO) {
        log.debug("Request to save Localite : {}", localiteDTO);
        Localite localite = localiteMapper.toEntity(localiteDTO);
        localite = localiteRepository.save(localite);
        LocaliteDTO result = localiteMapper.toDto(localite);
        localiteSearchRepository.save(localite);
        return result;
    }

    /**
     * Partially update a localite.
     *
     * @param localiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocaliteDTO> partialUpdate(LocaliteDTO localiteDTO) {
        log.debug("Request to partially update Localite : {}", localiteDTO);

        return localiteRepository
            .findById(localiteDTO.getId())
            .map(
                existingLocalite -> {
                    localiteMapper.partialUpdate(existingLocalite, localiteDTO);
                    return existingLocalite;
                }
            )
            .map(localiteRepository::save)
            .map(
                savedLocalite -> {
                    localiteSearchRepository.save(savedLocalite);

                    return savedLocalite;
                }
            )
            .map(localiteMapper::toDto);
    }

    /**
     * Get all the localites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocaliteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Localites");
        return localiteRepository.findAll(pageable).map(localiteMapper::toDto);
    }

    /**
     * Get one localite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocaliteDTO> findOne(Long id) {
        log.debug("Request to get Localite : {}", id);
        return localiteRepository.findById(id).map(localiteMapper::toDto);
    }

    /**
     * Delete the localite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Localite : {}", id);
        localiteRepository.deleteById(id);
        localiteSearchRepository.deleteById(id);
    }

    /**
     * Search for the localite corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocaliteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Localites for query {}", query);
        return localiteSearchRepository.search(queryStringQuery(query), pageable).map(localiteMapper::toDto);
    }
}
