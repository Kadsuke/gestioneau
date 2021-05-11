package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.Annee;
import com.onea.sidot.gestioneau.repository.AnneeRepository;
import com.onea.sidot.gestioneau.repository.search.AnneeSearchRepository;
import com.onea.sidot.gestioneau.service.dto.AnneeDTO;
import com.onea.sidot.gestioneau.service.mapper.AnneeMapper;
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
 * Service Implementation for managing {@link Annee}.
 */
@Service
@Transactional
public class AnneeService {

    private final Logger log = LoggerFactory.getLogger(AnneeService.class);

    private final AnneeRepository anneeRepository;

    private final AnneeMapper anneeMapper;

    private final AnneeSearchRepository anneeSearchRepository;

    public AnneeService(AnneeRepository anneeRepository, AnneeMapper anneeMapper, AnneeSearchRepository anneeSearchRepository) {
        this.anneeRepository = anneeRepository;
        this.anneeMapper = anneeMapper;
        this.anneeSearchRepository = anneeSearchRepository;
    }

    /**
     * Save a annee.
     *
     * @param anneeDTO the entity to save.
     * @return the persisted entity.
     */
    public AnneeDTO save(AnneeDTO anneeDTO) {
        log.debug("Request to save Annee : {}", anneeDTO);
        Annee annee = anneeMapper.toEntity(anneeDTO);
        annee = anneeRepository.save(annee);
        AnneeDTO result = anneeMapper.toDto(annee);
        anneeSearchRepository.save(annee);
        return result;
    }

    /**
     * Partially update a annee.
     *
     * @param anneeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AnneeDTO> partialUpdate(AnneeDTO anneeDTO) {
        log.debug("Request to partially update Annee : {}", anneeDTO);

        return anneeRepository
            .findById(anneeDTO.getId())
            .map(
                existingAnnee -> {
                    anneeMapper.partialUpdate(existingAnnee, anneeDTO);
                    return existingAnnee;
                }
            )
            .map(anneeRepository::save)
            .map(
                savedAnnee -> {
                    anneeSearchRepository.save(savedAnnee);

                    return savedAnnee;
                }
            )
            .map(anneeMapper::toDto);
    }

    /**
     * Get all the annees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AnneeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Annees");
        return anneeRepository.findAll(pageable).map(anneeMapper::toDto);
    }

    /**
     *  Get all the annees where Prevision is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AnneeDTO> findAllWherePrevisionIsNull() {
        log.debug("Request to get all annees where Prevision is null");
        return StreamSupport
            .stream(anneeRepository.findAll().spliterator(), false)
            .filter(annee -> annee.getPrevision() == null)
            .map(anneeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one annee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnneeDTO> findOne(Long id) {
        log.debug("Request to get Annee : {}", id);
        return anneeRepository.findById(id).map(anneeMapper::toDto);
    }

    /**
     * Delete the annee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Annee : {}", id);
        anneeRepository.deleteById(id);
        anneeSearchRepository.deleteById(id);
    }

    /**
     * Search for the annee corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AnneeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Annees for query {}", query);
        return anneeSearchRepository.search(queryStringQuery(query), pageable).map(anneeMapper::toDto);
    }
}
