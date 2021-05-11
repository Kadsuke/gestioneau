package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.SourceApprovEpRepository;
import com.onea.sidot.gestioneau.service.SourceApprovEpService;
import com.onea.sidot.gestioneau.service.dto.SourceApprovEpDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.SourceApprovEp}.
 */
@RestController
@RequestMapping("/api")
public class SourceApprovEpResource {

    private final Logger log = LoggerFactory.getLogger(SourceApprovEpResource.class);

    private static final String ENTITY_NAME = "gestioneauSourceApprovEp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceApprovEpService sourceApprovEpService;

    private final SourceApprovEpRepository sourceApprovEpRepository;

    public SourceApprovEpResource(SourceApprovEpService sourceApprovEpService, SourceApprovEpRepository sourceApprovEpRepository) {
        this.sourceApprovEpService = sourceApprovEpService;
        this.sourceApprovEpRepository = sourceApprovEpRepository;
    }

    /**
     * {@code POST  /source-approv-eps} : Create a new sourceApprovEp.
     *
     * @param sourceApprovEpDTO the sourceApprovEpDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceApprovEpDTO, or with status {@code 400 (Bad Request)} if the sourceApprovEp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/source-approv-eps")
    public ResponseEntity<SourceApprovEpDTO> createSourceApprovEp(@Valid @RequestBody SourceApprovEpDTO sourceApprovEpDTO)
        throws URISyntaxException {
        log.debug("REST request to save SourceApprovEp : {}", sourceApprovEpDTO);
        if (sourceApprovEpDTO.getId() != null) {
            throw new BadRequestAlertException("A new sourceApprovEp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourceApprovEpDTO result = sourceApprovEpService.save(sourceApprovEpDTO);
        return ResponseEntity
            .created(new URI("/api/source-approv-eps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /source-approv-eps/:id} : Updates an existing sourceApprovEp.
     *
     * @param id the id of the sourceApprovEpDTO to save.
     * @param sourceApprovEpDTO the sourceApprovEpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApprovEpDTO,
     * or with status {@code 400 (Bad Request)} if the sourceApprovEpDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceApprovEpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/source-approv-eps/{id}")
    public ResponseEntity<SourceApprovEpDTO> updateSourceApprovEp(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SourceApprovEpDTO sourceApprovEpDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SourceApprovEp : {}, {}", id, sourceApprovEpDTO);
        if (sourceApprovEpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApprovEpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApprovEpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SourceApprovEpDTO result = sourceApprovEpService.save(sourceApprovEpDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApprovEpDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /source-approv-eps/:id} : Partial updates given fields of an existing sourceApprovEp, field will ignore if it is null
     *
     * @param id the id of the sourceApprovEpDTO to save.
     * @param sourceApprovEpDTO the sourceApprovEpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceApprovEpDTO,
     * or with status {@code 400 (Bad Request)} if the sourceApprovEpDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sourceApprovEpDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceApprovEpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/source-approv-eps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SourceApprovEpDTO> partialUpdateSourceApprovEp(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SourceApprovEpDTO sourceApprovEpDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SourceApprovEp partially : {}, {}", id, sourceApprovEpDTO);
        if (sourceApprovEpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceApprovEpDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceApprovEpRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceApprovEpDTO> result = sourceApprovEpService.partialUpdate(sourceApprovEpDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceApprovEpDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /source-approv-eps} : get all the sourceApprovEps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceApprovEps in body.
     */
    @GetMapping("/source-approv-eps")
    public ResponseEntity<List<SourceApprovEpDTO>> getAllSourceApprovEps(Pageable pageable) {
        log.debug("REST request to get a page of SourceApprovEps");
        Page<SourceApprovEpDTO> page = sourceApprovEpService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /source-approv-eps/:id} : get the "id" sourceApprovEp.
     *
     * @param id the id of the sourceApprovEpDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceApprovEpDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/source-approv-eps/{id}")
    public ResponseEntity<SourceApprovEpDTO> getSourceApprovEp(@PathVariable Long id) {
        log.debug("REST request to get SourceApprovEp : {}", id);
        Optional<SourceApprovEpDTO> sourceApprovEpDTO = sourceApprovEpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceApprovEpDTO);
    }

    /**
     * {@code DELETE  /source-approv-eps/:id} : delete the "id" sourceApprovEp.
     *
     * @param id the id of the sourceApprovEpDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/source-approv-eps/{id}")
    public ResponseEntity<Void> deleteSourceApprovEp(@PathVariable Long id) {
        log.debug("REST request to delete SourceApprovEp : {}", id);
        sourceApprovEpService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/source-approv-eps?query=:query} : search for the sourceApprovEp corresponding
     * to the query.
     *
     * @param query the query of the sourceApprovEp search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/source-approv-eps")
    public ResponseEntity<List<SourceApprovEpDTO>> searchSourceApprovEps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SourceApprovEps for query {}", query);
        Page<SourceApprovEpDTO> page = sourceApprovEpService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
