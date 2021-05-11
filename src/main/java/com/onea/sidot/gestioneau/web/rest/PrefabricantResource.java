package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.PrefabricantRepository;
import com.onea.sidot.gestioneau.service.PrefabricantService;
import com.onea.sidot.gestioneau.service.dto.PrefabricantDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Prefabricant}.
 */
@RestController
@RequestMapping("/api")
public class PrefabricantResource {

    private final Logger log = LoggerFactory.getLogger(PrefabricantResource.class);

    private static final String ENTITY_NAME = "gestioneauPrefabricant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrefabricantService prefabricantService;

    private final PrefabricantRepository prefabricantRepository;

    public PrefabricantResource(PrefabricantService prefabricantService, PrefabricantRepository prefabricantRepository) {
        this.prefabricantService = prefabricantService;
        this.prefabricantRepository = prefabricantRepository;
    }

    /**
     * {@code POST  /prefabricants} : Create a new prefabricant.
     *
     * @param prefabricantDTO the prefabricantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prefabricantDTO, or with status {@code 400 (Bad Request)} if the prefabricant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prefabricants")
    public ResponseEntity<PrefabricantDTO> createPrefabricant(@Valid @RequestBody PrefabricantDTO prefabricantDTO)
        throws URISyntaxException {
        log.debug("REST request to save Prefabricant : {}", prefabricantDTO);
        if (prefabricantDTO.getId() != null) {
            throw new BadRequestAlertException("A new prefabricant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrefabricantDTO result = prefabricantService.save(prefabricantDTO);
        return ResponseEntity
            .created(new URI("/api/prefabricants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prefabricants/:id} : Updates an existing prefabricant.
     *
     * @param id the id of the prefabricantDTO to save.
     * @param prefabricantDTO the prefabricantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prefabricantDTO,
     * or with status {@code 400 (Bad Request)} if the prefabricantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prefabricantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prefabricants/{id}")
    public ResponseEntity<PrefabricantDTO> updatePrefabricant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrefabricantDTO prefabricantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Prefabricant : {}, {}", id, prefabricantDTO);
        if (prefabricantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prefabricantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prefabricantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrefabricantDTO result = prefabricantService.save(prefabricantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prefabricantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prefabricants/:id} : Partial updates given fields of an existing prefabricant, field will ignore if it is null
     *
     * @param id the id of the prefabricantDTO to save.
     * @param prefabricantDTO the prefabricantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prefabricantDTO,
     * or with status {@code 400 (Bad Request)} if the prefabricantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prefabricantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prefabricantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prefabricants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PrefabricantDTO> partialUpdatePrefabricant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrefabricantDTO prefabricantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prefabricant partially : {}, {}", id, prefabricantDTO);
        if (prefabricantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prefabricantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prefabricantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrefabricantDTO> result = prefabricantService.partialUpdate(prefabricantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prefabricantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prefabricants} : get all the prefabricants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prefabricants in body.
     */
    @GetMapping("/prefabricants")
    public ResponseEntity<List<PrefabricantDTO>> getAllPrefabricants(Pageable pageable) {
        log.debug("REST request to get a page of Prefabricants");
        Page<PrefabricantDTO> page = prefabricantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prefabricants/:id} : get the "id" prefabricant.
     *
     * @param id the id of the prefabricantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prefabricantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prefabricants/{id}")
    public ResponseEntity<PrefabricantDTO> getPrefabricant(@PathVariable Long id) {
        log.debug("REST request to get Prefabricant : {}", id);
        Optional<PrefabricantDTO> prefabricantDTO = prefabricantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prefabricantDTO);
    }

    /**
     * {@code DELETE  /prefabricants/:id} : delete the "id" prefabricant.
     *
     * @param id the id of the prefabricantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prefabricants/{id}")
    public ResponseEntity<Void> deletePrefabricant(@PathVariable Long id) {
        log.debug("REST request to delete Prefabricant : {}", id);
        prefabricantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prefabricants?query=:query} : search for the prefabricant corresponding
     * to the query.
     *
     * @param query the query of the prefabricant search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prefabricants")
    public ResponseEntity<List<PrefabricantDTO>> searchPrefabricants(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Prefabricants for query {}", query);
        Page<PrefabricantDTO> page = prefabricantService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
