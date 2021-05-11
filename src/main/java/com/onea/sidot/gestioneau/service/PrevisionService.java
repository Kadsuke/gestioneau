package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.Prevision;
import com.onea.sidot.gestioneau.repository.PrevisionRepository;
import com.onea.sidot.gestioneau.repository.search.PrevisionSearchRepository;
import com.onea.sidot.gestioneau.service.dto.PrevisionDTO;
import com.onea.sidot.gestioneau.service.mapper.PrevisionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prevision}.
 */
@Service
@Transactional
public class PrevisionService {

    private final Logger log = LoggerFactory.getLogger(PrevisionService.class);

    private final PrevisionRepository previsionRepository;

    private final PrevisionMapper previsionMapper;

    private final PrevisionSearchRepository previsionSearchRepository;

    public PrevisionService(
        PrevisionRepository previsionRepository,
        PrevisionMapper previsionMapper,
        PrevisionSearchRepository previsionSearchRepository
    ) {
        this.previsionRepository = previsionRepository;
        this.previsionMapper = previsionMapper;
        this.previsionSearchRepository = previsionSearchRepository;
    }

    /**
     * Save a prevision.
     *
     * @param previsionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrevisionDTO save(PrevisionDTO previsionDTO) {
        log.debug("Request to save Prevision : {}", previsionDTO);
        Prevision prevision = previsionMapper.toEntity(previsionDTO);
        prevision = previsionRepository.save(prevision);
        PrevisionDTO result = previsionMapper.toDto(prevision);
        previsionSearchRepository.save(prevision);
        return result;
    }

    /**
     * Partially update a prevision.
     *
     * @param previsionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrevisionDTO> partialUpdate(PrevisionDTO previsionDTO) {
        log.debug("Request to partially update Prevision : {}", previsionDTO);

        return previsionRepository
            .findById(previsionDTO.getId())
            .map(
                existingPrevision -> {
                    previsionMapper.partialUpdate(existingPrevision, previsionDTO);
                    return existingPrevision;
                }
            )
            .map(previsionRepository::save)
            .map(
                savedPrevision -> {
                    previsionSearchRepository.save(savedPrevision);

                    return savedPrevision;
                }
            )
            .map(previsionMapper::toDto);
    }

    /**
     * Get all the previsions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrevisionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Previsions");
        return previsionRepository.findAll(pageable).map(previsionMapper::toDto);
    }

    /**
     * Get one prevision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrevisionDTO> findOne(Long id) {
        log.debug("Request to get Prevision : {}", id);
        return previsionRepository.findById(id).map(previsionMapper::toDto);
    }

    /**
     * Delete the prevision by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prevision : {}", id);
        previsionRepository.deleteById(id);
        previsionSearchRepository.deleteById(id);
    }

    /**
     * Search for the prevision corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrevisionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Previsions for query {}", query);
        return previsionSearchRepository.search(queryStringQuery(query), pageable).map(previsionMapper::toDto);
    }
}
