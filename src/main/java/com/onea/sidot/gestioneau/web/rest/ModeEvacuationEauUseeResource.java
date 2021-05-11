package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.ModeEvacuationEauUseeRepository;
import com.onea.sidot.gestioneau.service.ModeEvacuationEauUseeService;
import com.onea.sidot.gestioneau.service.dto.ModeEvacuationEauUseeDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.ModeEvacuationEauUsee}.
 */
@RestController
@RequestMapping("/api")
public class ModeEvacuationEauUseeResource {

    private final Logger log = LoggerFactory.getLogger(ModeEvacuationEauUseeResource.class);

    private static final String ENTITY_NAME = "gestioneauModeEvacuationEauUsee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModeEvacuationEauUseeService modeEvacuationEauUseeService;

    private final ModeEvacuationEauUseeRepository modeEvacuationEauUseeRepository;

    public ModeEvacuationEauUseeResource(
        ModeEvacuationEauUseeService modeEvacuationEauUseeService,
        ModeEvacuationEauUseeRepository modeEvacuationEauUseeRepository
    ) {
        this.modeEvacuationEauUseeService = modeEvacuationEauUseeService;
        this.modeEvacuationEauUseeRepository = modeEvacuationEauUseeRepository;
    }

    /**
     * {@code POST  /mode-evacuation-eau-usees} : Create a new modeEvacuationEauUsee.
     *
     * @param modeEvacuationEauUseeDTO the modeEvacuationEauUseeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modeEvacuationEauUseeDTO, or with status {@code 400 (Bad Request)} if the modeEvacuationEauUsee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mode-evacuation-eau-usees")
    public ResponseEntity<ModeEvacuationEauUseeDTO> createModeEvacuationEauUsee(
        @Valid @RequestBody ModeEvacuationEauUseeDTO modeEvacuationEauUseeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ModeEvacuationEauUsee : {}", modeEvacuationEauUseeDTO);
        if (modeEvacuationEauUseeDTO.getId() != null) {
            throw new BadRequestAlertException("A new modeEvacuationEauUsee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModeEvacuationEauUseeDTO result = modeEvacuationEauUseeService.save(modeEvacuationEauUseeDTO);
        return ResponseEntity
            .created(new URI("/api/mode-evacuation-eau-usees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mode-evacuation-eau-usees/:id} : Updates an existing modeEvacuationEauUsee.
     *
     * @param id the id of the modeEvacuationEauUseeDTO to save.
     * @param modeEvacuationEauUseeDTO the modeEvacuationEauUseeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeEvacuationEauUseeDTO,
     * or with status {@code 400 (Bad Request)} if the modeEvacuationEauUseeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modeEvacuationEauUseeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mode-evacuation-eau-usees/{id}")
    public ResponseEntity<ModeEvacuationEauUseeDTO> updateModeEvacuationEauUsee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModeEvacuationEauUseeDTO modeEvacuationEauUseeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ModeEvacuationEauUsee : {}, {}", id, modeEvacuationEauUseeDTO);
        if (modeEvacuationEauUseeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeEvacuationEauUseeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeEvacuationEauUseeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ModeEvacuationEauUseeDTO result = modeEvacuationEauUseeService.save(modeEvacuationEauUseeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeEvacuationEauUseeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mode-evacuation-eau-usees/:id} : Partial updates given fields of an existing modeEvacuationEauUsee, field will ignore if it is null
     *
     * @param id the id of the modeEvacuationEauUseeDTO to save.
     * @param modeEvacuationEauUseeDTO the modeEvacuationEauUseeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modeEvacuationEauUseeDTO,
     * or with status {@code 400 (Bad Request)} if the modeEvacuationEauUseeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modeEvacuationEauUseeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modeEvacuationEauUseeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mode-evacuation-eau-usees/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ModeEvacuationEauUseeDTO> partialUpdateModeEvacuationEauUsee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModeEvacuationEauUseeDTO modeEvacuationEauUseeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ModeEvacuationEauUsee partially : {}, {}", id, modeEvacuationEauUseeDTO);
        if (modeEvacuationEauUseeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modeEvacuationEauUseeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modeEvacuationEauUseeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModeEvacuationEauUseeDTO> result = modeEvacuationEauUseeService.partialUpdate(modeEvacuationEauUseeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modeEvacuationEauUseeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mode-evacuation-eau-usees} : get all the modeEvacuationEauUsees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modeEvacuationEauUsees in body.
     */
    @GetMapping("/mode-evacuation-eau-usees")
    public ResponseEntity<List<ModeEvacuationEauUseeDTO>> getAllModeEvacuationEauUsees(Pageable pageable) {
        log.debug("REST request to get a page of ModeEvacuationEauUsees");
        Page<ModeEvacuationEauUseeDTO> page = modeEvacuationEauUseeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mode-evacuation-eau-usees/:id} : get the "id" modeEvacuationEauUsee.
     *
     * @param id the id of the modeEvacuationEauUseeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modeEvacuationEauUseeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mode-evacuation-eau-usees/{id}")
    public ResponseEntity<ModeEvacuationEauUseeDTO> getModeEvacuationEauUsee(@PathVariable Long id) {
        log.debug("REST request to get ModeEvacuationEauUsee : {}", id);
        Optional<ModeEvacuationEauUseeDTO> modeEvacuationEauUseeDTO = modeEvacuationEauUseeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modeEvacuationEauUseeDTO);
    }

    /**
     * {@code DELETE  /mode-evacuation-eau-usees/:id} : delete the "id" modeEvacuationEauUsee.
     *
     * @param id the id of the modeEvacuationEauUseeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mode-evacuation-eau-usees/{id}")
    public ResponseEntity<Void> deleteModeEvacuationEauUsee(@PathVariable Long id) {
        log.debug("REST request to delete ModeEvacuationEauUsee : {}", id);
        modeEvacuationEauUseeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/mode-evacuation-eau-usees?query=:query} : search for the modeEvacuationEauUsee corresponding
     * to the query.
     *
     * @param query the query of the modeEvacuationEauUsee search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/mode-evacuation-eau-usees")
    public ResponseEntity<List<ModeEvacuationEauUseeDTO>> searchModeEvacuationEauUsees(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ModeEvacuationEauUsees for query {}", query);
        Page<ModeEvacuationEauUseeDTO> page = modeEvacuationEauUseeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
