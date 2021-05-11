package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.Prefabricant;
import com.onea.sidot.gestioneau.repository.PrefabricantRepository;
import com.onea.sidot.gestioneau.repository.search.PrefabricantSearchRepository;
import com.onea.sidot.gestioneau.service.dto.PrefabricantDTO;
import com.onea.sidot.gestioneau.service.mapper.PrefabricantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prefabricant}.
 */
@Service
@Transactional
public class PrefabricantService {

    private final Logger log = LoggerFactory.getLogger(PrefabricantService.class);

    private final PrefabricantRepository prefabricantRepository;

    private final PrefabricantMapper prefabricantMapper;

    private final PrefabricantSearchRepository prefabricantSearchRepository;

    public PrefabricantService(
        PrefabricantRepository prefabricantRepository,
        PrefabricantMapper prefabricantMapper,
        PrefabricantSearchRepository prefabricantSearchRepository
    ) {
        this.prefabricantRepository = prefabricantRepository;
        this.prefabricantMapper = prefabricantMapper;
        this.prefabricantSearchRepository = prefabricantSearchRepository;
    }

    /**
     * Save a prefabricant.
     *
     * @param prefabricantDTO the entity to save.
     * @return the persisted entity.
     */
    public PrefabricantDTO save(PrefabricantDTO prefabricantDTO) {
        log.debug("Request to save Prefabricant : {}", prefabricantDTO);
        Prefabricant prefabricant = prefabricantMapper.toEntity(prefabricantDTO);
        prefabricant = prefabricantRepository.save(prefabricant);
        PrefabricantDTO result = prefabricantMapper.toDto(prefabricant);
        prefabricantSearchRepository.save(prefabricant);
        return result;
    }

    /**
     * Partially update a prefabricant.
     *
     * @param prefabricantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrefabricantDTO> partialUpdate(PrefabricantDTO prefabricantDTO) {
        log.debug("Request to partially update Prefabricant : {}", prefabricantDTO);

        return prefabricantRepository
            .findById(prefabricantDTO.getId())
            .map(
                existingPrefabricant -> {
                    prefabricantMapper.partialUpdate(existingPrefabricant, prefabricantDTO);
                    return existingPrefabricant;
                }
            )
            .map(prefabricantRepository::save)
            .map(
                savedPrefabricant -> {
                    prefabricantSearchRepository.save(savedPrefabricant);

                    return savedPrefabricant;
                }
            )
            .map(prefabricantMapper::toDto);
    }

    /**
     * Get all the prefabricants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrefabricantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prefabricants");
        return prefabricantRepository.findAll(pageable).map(prefabricantMapper::toDto);
    }

    /**
     * Get one prefabricant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrefabricantDTO> findOne(Long id) {
        log.debug("Request to get Prefabricant : {}", id);
        return prefabricantRepository.findById(id).map(prefabricantMapper::toDto);
    }

    /**
     * Delete the prefabricant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prefabricant : {}", id);
        prefabricantRepository.deleteById(id);
        prefabricantSearchRepository.deleteById(id);
    }

    /**
     * Search for the prefabricant corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrefabricantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Prefabricants for query {}", query);
        return prefabricantSearchRepository.search(queryStringQuery(query), pageable).map(prefabricantMapper::toDto);
    }
}
