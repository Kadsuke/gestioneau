package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.TypeCommune;
import com.onea.sidot.gestioneau.repository.TypeCommuneRepository;
import com.onea.sidot.gestioneau.repository.search.TypeCommuneSearchRepository;
import com.onea.sidot.gestioneau.service.dto.TypeCommuneDTO;
import com.onea.sidot.gestioneau.service.mapper.TypeCommuneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeCommune}.
 */
@Service
@Transactional
public class TypeCommuneService {

    private final Logger log = LoggerFactory.getLogger(TypeCommuneService.class);

    private final TypeCommuneRepository typeCommuneRepository;

    private final TypeCommuneMapper typeCommuneMapper;

    private final TypeCommuneSearchRepository typeCommuneSearchRepository;

    public TypeCommuneService(
        TypeCommuneRepository typeCommuneRepository,
        TypeCommuneMapper typeCommuneMapper,
        TypeCommuneSearchRepository typeCommuneSearchRepository
    ) {
        this.typeCommuneRepository = typeCommuneRepository;
        this.typeCommuneMapper = typeCommuneMapper;
        this.typeCommuneSearchRepository = typeCommuneSearchRepository;
    }

    /**
     * Save a typeCommune.
     *
     * @param typeCommuneDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeCommuneDTO save(TypeCommuneDTO typeCommuneDTO) {
        log.debug("Request to save TypeCommune : {}", typeCommuneDTO);
        TypeCommune typeCommune = typeCommuneMapper.toEntity(typeCommuneDTO);
        typeCommune = typeCommuneRepository.save(typeCommune);
        TypeCommuneDTO result = typeCommuneMapper.toDto(typeCommune);
        typeCommuneSearchRepository.save(typeCommune);
        return result;
    }

    /**
     * Partially update a typeCommune.
     *
     * @param typeCommuneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeCommuneDTO> partialUpdate(TypeCommuneDTO typeCommuneDTO) {
        log.debug("Request to partially update TypeCommune : {}", typeCommuneDTO);

        return typeCommuneRepository
            .findById(typeCommuneDTO.getId())
            .map(
                existingTypeCommune -> {
                    typeCommuneMapper.partialUpdate(existingTypeCommune, typeCommuneDTO);
                    return existingTypeCommune;
                }
            )
            .map(typeCommuneRepository::save)
            .map(
                savedTypeCommune -> {
                    typeCommuneSearchRepository.save(savedTypeCommune);

                    return savedTypeCommune;
                }
            )
            .map(typeCommuneMapper::toDto);
    }

    /**
     * Get all the typeCommunes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeCommuneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeCommunes");
        return typeCommuneRepository.findAll(pageable).map(typeCommuneMapper::toDto);
    }

    /**
     * Get one typeCommune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeCommuneDTO> findOne(Long id) {
        log.debug("Request to get TypeCommune : {}", id);
        return typeCommuneRepository.findById(id).map(typeCommuneMapper::toDto);
    }

    /**
     * Delete the typeCommune by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeCommune : {}", id);
        typeCommuneRepository.deleteById(id);
        typeCommuneSearchRepository.deleteById(id);
    }

    /**
     * Search for the typeCommune corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeCommuneDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeCommunes for query {}", query);
        return typeCommuneSearchRepository.search(queryStringQuery(query), pageable).map(typeCommuneMapper::toDto);
    }
}
