package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.PrevisionRepository;
import com.onea.sidot.gestioneau.service.PrevisionService;
import com.onea.sidot.gestioneau.service.dto.PrevisionDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Prevision}.
 */
@RestController
@RequestMapping("/api")
public class PrevisionResource {

    private final Logger log = LoggerFactory.getLogger(PrevisionResource.class);

    private static final String ENTITY_NAME = "gestioneauPrevision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrevisionService previsionService;

    private final PrevisionRepository previsionRepository;

    public PrevisionResource(PrevisionService previsionService, PrevisionRepository previsionRepository) {
        this.previsionService = previsionService;
        this.previsionRepository = previsionRepository;
    }

    /**
     * {@code POST  /previsions} : Create a new prevision.
     *
     * @param previsionDTO the previsionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new previsionDTO, or with status {@code 400 (Bad Request)} if the prevision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/previsions")
    public ResponseEntity<PrevisionDTO> createPrevision(@Valid @RequestBody PrevisionDTO previsionDTO) throws URISyntaxException {
        log.debug("REST request to save Prevision : {}", previsionDTO);
        if (previsionDTO.getId() != null) {
            throw new BadRequestAlertException("A new prevision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrevisionDTO result = previsionService.save(previsionDTO);
        return ResponseEntity
            .created(new URI("/api/previsions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /previsions/:id} : Updates an existing prevision.
     *
     * @param id the id of the previsionDTO to save.
     * @param previsionDTO the previsionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated previsionDTO,
     * or with status {@code 400 (Bad Request)} if the previsionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the previsionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/previsions/{id}")
    public ResponseEntity<PrevisionDTO> updatePrevision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrevisionDTO previsionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Prevision : {}, {}", id, previsionDTO);
        if (previsionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, previsionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!previsionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrevisionDTO result = previsionService.save(previsionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, previsionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /previsions/:id} : Partial updates given fields of an existing prevision, field will ignore if it is null
     *
     * @param id the id of the previsionDTO to save.
     * @param previsionDTO the previsionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated previsionDTO,
     * or with status {@code 400 (Bad Request)} if the previsionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the previsionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the previsionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/previsions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PrevisionDTO> partialUpdatePrevision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrevisionDTO previsionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prevision partially : {}, {}", id, previsionDTO);
        if (previsionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, previsionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!previsionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrevisionDTO> result = previsionService.partialUpdate(previsionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, previsionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /previsions} : get all the previsions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of previsions in body.
     */
    @GetMapping("/previsions")
    public ResponseEntity<List<PrevisionDTO>> getAllPrevisions(Pageable pageable) {
        log.debug("REST request to get a page of Previsions");
        Page<PrevisionDTO> page = previsionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /previsions/:id} : get the "id" prevision.
     *
     * @param id the id of the previsionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the previsionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/previsions/{id}")
    public ResponseEntity<PrevisionDTO> getPrevision(@PathVariable Long id) {
        log.debug("REST request to get Prevision : {}", id);
        Optional<PrevisionDTO> previsionDTO = previsionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(previsionDTO);
    }

    /**
     * {@code DELETE  /previsions/:id} : delete the "id" prevision.
     *
     * @param id the id of the previsionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/previsions/{id}")
    public ResponseEntity<Void> deletePrevision(@PathVariable Long id) {
        log.debug("REST request to delete Prevision : {}", id);
        previsionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/previsions?query=:query} : search for the prevision corresponding
     * to the query.
     *
     * @param query the query of the prevision search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/previsions")
    public ResponseEntity<List<PrevisionDTO>> searchPrevisions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Previsions for query {}", query);
        Page<PrevisionDTO> page = previsionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
