package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.Centre;
import com.onea.sidot.gestioneau.repository.CentreRepository;
import com.onea.sidot.gestioneau.repository.search.CentreSearchRepository;
import com.onea.sidot.gestioneau.service.dto.CentreDTO;
import com.onea.sidot.gestioneau.service.mapper.CentreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Centre}.
 */
@Service
@Transactional
public class CentreService {

    private final Logger log = LoggerFactory.getLogger(CentreService.class);

    private final CentreRepository centreRepository;

    private final CentreMapper centreMapper;

    private final CentreSearchRepository centreSearchRepository;

    public CentreService(CentreRepository centreRepository, CentreMapper centreMapper, CentreSearchRepository centreSearchRepository) {
        this.centreRepository = centreRepository;
        this.centreMapper = centreMapper;
        this.centreSearchRepository = centreSearchRepository;
    }

    /**
     * Save a centre.
     *
     * @param centreDTO the entity to save.
     * @return the persisted entity.
     */
    public CentreDTO save(CentreDTO centreDTO) {
        log.debug("Request to save Centre : {}", centreDTO);
        Centre centre = centreMapper.toEntity(centreDTO);
        centre = centreRepository.save(centre);
        CentreDTO result = centreMapper.toDto(centre);
        centreSearchRepository.save(centre);
        return result;
    }

    /**
     * Partially update a centre.
     *
     * @param centreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CentreDTO> partialUpdate(CentreDTO centreDTO) {
        log.debug("Request to partially update Centre : {}", centreDTO);

        return centreRepository
            .findById(centreDTO.getId())
            .map(
                existingCentre -> {
                    centreMapper.partialUpdate(existingCentre, centreDTO);
                    return existingCentre;
                }
            )
            .map(centreRepository::save)
            .map(
                savedCentre -> {
                    centreSearchRepository.save(savedCentre);

                    return savedCentre;
                }
            )
            .map(centreMapper::toDto);
    }

    /**
     * Get all the centres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CentreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Centres");
        return centreRepository.findAll(pageable).map(centreMapper::toDto);
    }

    /**
     *  Get all the centres where Prevision is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CentreDTO> findAllWherePrevisionIsNull() {
        log.debug("Request to get all centres where Prevision is null");
        return StreamSupport
            .stream(centreRepository.findAll().spliterator(), false)
            .filter(centre -> centre.getPrevision() == null)
            .map(centreMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one centre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CentreDTO> findOne(Long id) {
        log.debug("Request to get Centre : {}", id);
        return centreRepository.findById(id).map(centreMapper::toDto);
    }

    /**
     * Delete the centre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Centre : {}", id);
        centreRepository.deleteById(id);
        centreSearchRepository.deleteById(id);
    }

    /**
     * Search for the centre corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CentreDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Centres for query {}", query);
        return centreSearchRepository.search(queryStringQuery(query), pageable).map(centreMapper::toDto);
    }
}
