package com.onea.sidot.gestioneau.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.domain.FicheSuiviOuvrage;
import com.onea.sidot.gestioneau.repository.FicheSuiviOuvrageRepository;
import com.onea.sidot.gestioneau.repository.search.FicheSuiviOuvrageSearchRepository;
import com.onea.sidot.gestioneau.service.dto.FicheSuiviOuvrageDTO;
import com.onea.sidot.gestioneau.service.mapper.FicheSuiviOuvrageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FicheSuiviOuvrage}.
 */
@Service
@Transactional
public class FicheSuiviOuvrageService {

    private final Logger log = LoggerFactory.getLogger(FicheSuiviOuvrageService.class);

    private final FicheSuiviOuvrageRepository ficheSuiviOuvrageRepository;

    private final FicheSuiviOuvrageMapper ficheSuiviOuvrageMapper;

    private final FicheSuiviOuvrageSearchRepository ficheSuiviOuvrageSearchRepository;

    public FicheSuiviOuvrageService(
        FicheSuiviOuvrageRepository ficheSuiviOuvrageRepository,
        FicheSuiviOuvrageMapper ficheSuiviOuvrageMapper,
        FicheSuiviOuvrageSearchRepository ficheSuiviOuvrageSearchRepository
    ) {
        this.ficheSuiviOuvrageRepository = ficheSuiviOuvrageRepository;
        this.ficheSuiviOuvrageMapper = ficheSuiviOuvrageMapper;
        this.ficheSuiviOuvrageSearchRepository = ficheSuiviOuvrageSearchRepository;
    }

    /**
     * Save a ficheSuiviOuvrage.
     *
     * @param ficheSuiviOuvrageDTO the entity to save.
     * @return the persisted entity.
     */
    public FicheSuiviOuvrageDTO save(FicheSuiviOuvrageDTO ficheSuiviOuvrageDTO) {
        log.debug("Request to save FicheSuiviOuvrage : {}", ficheSuiviOuvrageDTO);
        FicheSuiviOuvrage ficheSuiviOuvrage = ficheSuiviOuvrageMapper.toEntity(ficheSuiviOuvrageDTO);
        ficheSuiviOuvrage = ficheSuiviOuvrageRepository.save(ficheSuiviOuvrage);
        FicheSuiviOuvrageDTO result = ficheSuiviOuvrageMapper.toDto(ficheSuiviOuvrage);
        ficheSuiviOuvrageSearchRepository.save(ficheSuiviOuvrage);
        return result;
    }

    /**
     * Partially update a ficheSuiviOuvrage.
     *
     * @param ficheSuiviOuvrageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FicheSuiviOuvrageDTO> partialUpdate(FicheSuiviOuvrageDTO ficheSuiviOuvrageDTO) {
        log.debug("Request to partially update FicheSuiviOuvrage : {}", ficheSuiviOuvrageDTO);

        return ficheSuiviOuvrageRepository
            .findById(ficheSuiviOuvrageDTO.getId())
            .map(
                existingFicheSuiviOuvrage -> {
                    ficheSuiviOuvrageMapper.partialUpdate(existingFicheSuiviOuvrage, ficheSuiviOuvrageDTO);
                    return existingFicheSuiviOuvrage;
                }
            )
            .map(ficheSuiviOuvrageRepository::save)
            .map(
                savedFicheSuiviOuvrage -> {
                    ficheSuiviOuvrageSearchRepository.save(savedFicheSuiviOuvrage);

                    return savedFicheSuiviOuvrage;
                }
            )
            .map(ficheSuiviOuvrageMapper::toDto);
    }

    /**
     * Get all the ficheSuiviOuvrages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FicheSuiviOuvrageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FicheSuiviOuvrages");
        return ficheSuiviOuvrageRepository.findAll(pageable).map(ficheSuiviOuvrageMapper::toDto);
    }

    /**
     * Get one ficheSuiviOuvrage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FicheSuiviOuvrageDTO> findOne(Long id) {
        log.debug("Request to get FicheSuiviOuvrage : {}", id);
        return ficheSuiviOuvrageRepository.findById(id).map(ficheSuiviOuvrageMapper::toDto);
    }

    /**
     * Delete the ficheSuiviOuvrage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FicheSuiviOuvrage : {}", id);
        ficheSuiviOuvrageRepository.deleteById(id);
        ficheSuiviOuvrageSearchRepository.deleteById(id);
    }

    /**
     * Search for the ficheSuiviOuvrage corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FicheSuiviOuvrageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FicheSuiviOuvrages for query {}", query);
        return ficheSuiviOuvrageSearchRepository.search(queryStringQuery(query), pageable).map(ficheSuiviOuvrageMapper::toDto);
    }
}
