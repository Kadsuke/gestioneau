package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.ModeEvacExcreta;
import com.onea.sidot.gestioneau.repository.ModeEvacExcretaRepository;
import com.onea.sidot.gestioneau.repository.search.ModeEvacExcretaSearchRepository;
import com.onea.sidot.gestioneau.service.dto.ModeEvacExcretaDTO;
import com.onea.sidot.gestioneau.service.mapper.ModeEvacExcretaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ModeEvacExcreta}.
 */
@Service
@Transactional
public class ModeEvacExcretaService {

    private final Logger log = LoggerFactory.getLogger(ModeEvacExcretaService.class);

    private final ModeEvacExcretaRepository modeEvacExcretaRepository;

    private final ModeEvacExcretaMapper modeEvacExcretaMapper;

    private final ModeEvacExcretaSearchRepository modeEvacExcretaSearchRepository;

    public ModeEvacExcretaService(
        ModeEvacExcretaRepository modeEvacExcretaRepository,
        ModeEvacExcretaMapper modeEvacExcretaMapper,
        ModeEvacExcretaSearchRepository modeEvacExcretaSearchRepository
    ) {
        this.modeEvacExcretaRepository = modeEvacExcretaRepository;
        this.modeEvacExcretaMapper = modeEvacExcretaMapper;
        this.modeEvacExcretaSearchRepository = modeEvacExcretaSearchRepository;
    }

    /**
     * Save a modeEvacExcreta.
     *
     * @param modeEvacExcretaDTO the entity to save.
     * @return the persisted entity.
     */
    public ModeEvacExcretaDTO save(ModeEvacExcretaDTO modeEvacExcretaDTO) {
        log.debug("Request to save ModeEvacExcreta : {}", modeEvacExcretaDTO);
        ModeEvacExcreta modeEvacExcreta = modeEvacExcretaMapper.toEntity(modeEvacExcretaDTO);
        modeEvacExcreta = modeEvacExcretaRepository.save(modeEvacExcreta);
        ModeEvacExcretaDTO result = modeEvacExcretaMapper.toDto(modeEvacExcreta);
        modeEvacExcretaSearchRepository.save(modeEvacExcreta);
        return result;
    }

    /**
     * Partially update a modeEvacExcreta.
     *
     * @param modeEvacExcretaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModeEvacExcretaDTO> partialUpdate(ModeEvacExcretaDTO modeEvacExcretaDTO) {
        log.debug("Request to partially update ModeEvacExcreta : {}", modeEvacExcretaDTO);

        return modeEvacExcretaRepository
            .findById(modeEvacExcretaDTO.getId())
            .map(
                existingModeEvacExcreta -> {
                    modeEvacExcretaMapper.partialUpdate(existingModeEvacExcreta, modeEvacExcretaDTO);
                    return existingModeEvacExcreta;
                }
            )
            .map(modeEvacExcretaRepository::save)
            .map(
                savedModeEvacExcreta -> {
                    modeEvacExcretaSearchRepository.save(savedModeEvacExcreta);

                    return savedModeEvacExcreta;
                }
            )
            .map(modeEvacExcretaMapper::toDto);
    }

    /**
     * Get all the modeEvacExcretas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModeEvacExcretaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ModeEvacExcretas");
        return modeEvacExcretaRepository.findAll(pageable).map(modeEvacExcretaMapper::toDto);
    }

    /**
     * Get one modeEvacExcreta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModeEvacExcretaDTO> findOne(Long id) {
        log.debug("Request to get ModeEvacExcreta : {}", id);
        return modeEvacExcretaRepository.findById(id).map(modeEvacExcretaMapper::toDto);
    }

    /**
     * Delete the modeEvacExcreta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ModeEvacExcreta : {}", id);
        modeEvacExcretaRepository.deleteById(id);
        modeEvacExcretaSearchRepository.deleteById(id);
    }

    /**
     * Search for the modeEvacExcreta corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModeEvacExcretaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ModeEvacExcretas for query {}", query);
        return modeEvacExcretaSearchRepository.search(queryStringQuery(query), pageable).map(modeEvacExcretaMapper::toDto);
    }
}
