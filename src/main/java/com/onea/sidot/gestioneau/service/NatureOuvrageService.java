package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.NatureOuvrage;
import com.onea.sidot.gestioneau.repository.NatureOuvrageRepository;
import com.onea.sidot.gestioneau.repository.search.NatureOuvrageSearchRepository;
import com.onea.sidot.gestioneau.service.dto.NatureOuvrageDTO;
import com.onea.sidot.gestioneau.service.mapper.NatureOuvrageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NatureOuvrage}.
 */
@Service
@Transactional
public class NatureOuvrageService {

    private final Logger log = LoggerFactory.getLogger(NatureOuvrageService.class);

    private final NatureOuvrageRepository natureOuvrageRepository;

    private final NatureOuvrageMapper natureOuvrageMapper;

    private final NatureOuvrageSearchRepository natureOuvrageSearchRepository;

    public NatureOuvrageService(
        NatureOuvrageRepository natureOuvrageRepository,
        NatureOuvrageMapper natureOuvrageMapper,
        NatureOuvrageSearchRepository natureOuvrageSearchRepository
    ) {
        this.natureOuvrageRepository = natureOuvrageRepository;
        this.natureOuvrageMapper = natureOuvrageMapper;
        this.natureOuvrageSearchRepository = natureOuvrageSearchRepository;
    }

    /**
     * Save a natureOuvrage.
     *
     * @param natureOuvrageDTO the entity to save.
     * @return the persisted entity.
     */
    public NatureOuvrageDTO save(NatureOuvrageDTO natureOuvrageDTO) {
        log.debug("Request to save NatureOuvrage : {}", natureOuvrageDTO);
        NatureOuvrage natureOuvrage = natureOuvrageMapper.toEntity(natureOuvrageDTO);
        natureOuvrage = natureOuvrageRepository.save(natureOuvrage);
        NatureOuvrageDTO result = natureOuvrageMapper.toDto(natureOuvrage);
        natureOuvrageSearchRepository.save(natureOuvrage);
        return result;
    }

    /**
     * Partially update a natureOuvrage.
     *
     * @param natureOuvrageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NatureOuvrageDTO> partialUpdate(NatureOuvrageDTO natureOuvrageDTO) {
        log.debug("Request to partially update NatureOuvrage : {}", natureOuvrageDTO);

        return natureOuvrageRepository
            .findById(natureOuvrageDTO.getId())
            .map(
                existingNatureOuvrage -> {
                    natureOuvrageMapper.partialUpdate(existingNatureOuvrage, natureOuvrageDTO);
                    return existingNatureOuvrage;
                }
            )
            .map(natureOuvrageRepository::save)
            .map(
                savedNatureOuvrage -> {
                    natureOuvrageSearchRepository.save(savedNatureOuvrage);

                    return savedNatureOuvrage;
                }
            )
            .map(natureOuvrageMapper::toDto);
    }

    /**
     * Get all the natureOuvrages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NatureOuvrageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NatureOuvrages");
        return natureOuvrageRepository.findAll(pageable).map(natureOuvrageMapper::toDto);
    }

    /**
     * Get one natureOuvrage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NatureOuvrageDTO> findOne(Long id) {
        log.debug("Request to get NatureOuvrage : {}", id);
        return natureOuvrageRepository.findById(id).map(natureOuvrageMapper::toDto);
    }

    /**
     * Delete the natureOuvrage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NatureOuvrage : {}", id);
        natureOuvrageRepository.deleteById(id);
        natureOuvrageSearchRepository.deleteById(id);
    }

    /**
     * Search for the natureOuvrage corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NatureOuvrageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NatureOuvrages for query {}", query);
        return natureOuvrageSearchRepository.search(queryStringQuery(query), pageable).map(natureOuvrageMapper::toDto);
    }
}
