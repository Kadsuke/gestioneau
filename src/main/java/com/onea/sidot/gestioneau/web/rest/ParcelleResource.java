package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.ParcelleRepository;
import com.onea.sidot.gestioneau.service.ParcelleService;
import com.onea.sidot.gestioneau.service.dto.ParcelleDTO;
import com.onea.sidot.gestioneau.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Parcelle}.
 */
@RestController
@RequestMapping("/api")
public class ParcelleResource {

    private final Logger log = LoggerFactory.getLogger(ParcelleResource.class);

    private static final String ENTITY_NAME = "gestioneauParcelle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParcelleService parcelleService;

    private final ParcelleRepository parcelleRepository;

    public ParcelleResource(ParcelleService parcelleService, ParcelleRepository parcelleRepository) {
        this.parcelleService = parcelleService;
        this.parcelleRepository = parcelleRepository;
    }

    /**
     * {@code POST  /parcelles} : Create a new parcelle.
     *
     * @param parcelleDTO the parcelleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parcelleDTO, or with status {@code 400 (Bad Request)} if the parcelle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parcelles")
    public ResponseEntity<ParcelleDTO> createParcelle(@Valid @RequestBody ParcelleDTO parcelleDTO) throws URISyntaxException {
        log.debug("REST request to save Parcelle : {}", parcelleDTO);
        if (parcelleDTO.getId() != null) {
            throw new BadRequestAlertException("A new parcelle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParcelleDTO result = parcelleService.save(parcelleDTO);
        return ResponseEntity
            .created(new URI("/api/parcelles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parcelles/:id} : Updates an existing parcelle.
     *
     * @param id the id of the parcelleDTO to save.
     * @param parcelleDTO the parcelleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcelleDTO,
     * or with status {@code 400 (Bad Request)} if the parcelleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parcelleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parcelles/{id}")
    public ResponseEntity<ParcelleDTO> updateParcelle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParcelleDTO parcelleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Parcelle : {}, {}", id, parcelleDTO);
        if (parcelleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcelleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parcelleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParcelleDTO result = parcelleService.save(parcelleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parcelleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parcelles/:id} : Partial updates given fields of an existing parcelle, field will ignore if it is null
     *
     * @param id the id of the parcelleDTO to save.
     * @param parcelleDTO the parcelleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parcelleDTO,
     * or with status {@code 400 (Bad Request)} if the parcelleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parcelleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parcelleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parcelles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ParcelleDTO> partialUpdateParcelle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParcelleDTO parcelleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parcelle partially : {}, {}", id, parcelleDTO);
        if (parcelleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parcelleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parcelleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParcelleDTO> result = parcelleService.partialUpdate(parcelleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parcelleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parcelles} : get all the parcelles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parcelles in body.
     */
    @GetMapping("/parcelles")
    public ResponseEntity<List<ParcelleDTO>> getAllParcelles(Pageable pageable) {
        log.debug("REST request to get a page of Parcelles");
        Page<ParcelleDTO> page = parcelleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parcelles/:id} : get the "id" parcelle.
     *
     * @param id the id of the parcelleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parcelleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parcelles/{id}")
    public ResponseEntity<ParcelleDTO> getParcelle(@PathVariable Long id) {
        log.debug("REST request to get Parcelle : {}", id);
        Optional<ParcelleDTO> parcelleDTO = parcelleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parcelleDTO);
    }

    /**
     * {@code DELETE  /parcelles/:id} : delete the "id" parcelle.
     *
     * @param id the id of the parcelleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parcelles/{id}")
    public ResponseEntity<Void> deleteParcelle(@PathVariable Long id) {
        log.debug("REST request to delete Parcelle : {}", id);
        parcelleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/parcelles?query=:query} : search for the parcelle corresponding
     * to the query.
     *
     * @param query the query of the parcelle search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/parcelles")
    public ResponseEntity<List<ParcelleDTO>> searchParcelles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Parcelles for query {}", query);
        Page<ParcelleDTO> page = parcelleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
