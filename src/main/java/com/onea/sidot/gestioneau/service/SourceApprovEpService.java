package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.SourceApprovEp;
import com.onea.sidot.gestioneau.repository.SourceApprovEpRepository;
import com.onea.sidot.gestioneau.repository.search.SourceApprovEpSearchRepository;
import com.onea.sidot.gestioneau.service.dto.SourceApprovEpDTO;
import com.onea.sidot.gestioneau.service.mapper.SourceApprovEpMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SourceApprovEp}.
 */
@Service
@Transactional
public class SourceApprovEpService {

    private final Logger log = LoggerFactory.getLogger(SourceApprovEpService.class);

    private final SourceApprovEpRepository sourceApprovEpRepository;

    private final SourceApprovEpMapper sourceApprovEpMapper;

    private final SourceApprovEpSearchRepository sourceApprovEpSearchRepository;

    public SourceApprovEpService(
        SourceApprovEpRepository sourceApprovEpRepository,
        SourceApprovEpMapper sourceApprovEpMapper,
        SourceApprovEpSearchRepository sourceApprovEpSearchRepository
    ) {
        this.sourceApprovEpRepository = sourceApprovEpRepository;
        this.sourceApprovEpMapper = sourceApprovEpMapper;
        this.sourceApprovEpSearchRepository = sourceApprovEpSearchRepository;
    }

    /**
     * Save a sourceApprovEp.
     *
     * @param sourceApprovEpDTO the entity to save.
     * @return the persisted entity.
     */
    public SourceApprovEpDTO save(SourceApprovEpDTO sourceApprovEpDTO) {
        log.debug("Request to save SourceApprovEp : {}", sourceApprovEpDTO);
        SourceApprovEp sourceApprovEp = sourceApprovEpMapper.toEntity(sourceApprovEpDTO);
        sourceApprovEp = sourceApprovEpRepository.save(sourceApprovEp);
        SourceApprovEpDTO result = sourceApprovEpMapper.toDto(sourceApprovEp);
        sourceApprovEpSearchRepository.save(sourceApprovEp);
        return result;
    }

    /**
     * Partially update a sourceApprovEp.
     *
     * @param sourceApprovEpDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SourceApprovEpDTO> partialUpdate(SourceApprovEpDTO sourceApprovEpDTO) {
        log.debug("Request to partially update SourceApprovEp : {}", sourceApprovEpDTO);

        return sourceApprovEpRepository
            .findById(sourceApprovEpDTO.getId())
            .map(
                existingSourceApprovEp -> {
                    sourceApprovEpMapper.partialUpdate(existingSourceApprovEp, sourceApprovEpDTO);
                    return existingSourceApprovEp;
                }
            )
            .map(sourceApprovEpRepository::save)
            .map(
                savedSourceApprovEp -> {
                    sourceApprovEpSearchRepository.save(savedSourceApprovEp);

                    return savedSourceApprovEp;
                }
            )
            .map(sourceApprovEpMapper::toDto);
    }

    /**
     * Get all the sourceApprovEps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SourceApprovEpDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SourceApprovEps");
        return sourceApprovEpRepository.findAll(pageable).map(sourceApprovEpMapper::toDto);
    }

    /**
     * Get one sourceApprovEp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SourceApprovEpDTO> findOne(Long id) {
        log.debug("Request to get SourceApprovEp : {}", id);
        return sourceApprovEpRepository.findById(id).map(sourceApprovEpMapper::toDto);
    }

    /**
     * Delete the sourceApprovEp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SourceApprovEp : {}", id);
        sourceApprovEpRepository.deleteById(id);
        sourceApprovEpSearchRepository.deleteById(id);
    }

    /**
     * Search for the sourceApprovEp corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SourceApprovEpDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SourceApprovEps for query {}", query);
        return sourceApprovEpSearchRepository.search(queryStringQuery(query), pageable).map(sourceApprovEpMapper::toDto);
    }
}
