package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.SectionRepository;
import com.onea.sidot.gestioneau.service.SectionService;
import com.onea.sidot.gestioneau.service.dto.SectionDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Section}.
 */
@RestController
@RequestMapping("/api")
public class SectionResource {

    private final Logger log = LoggerFactory.getLogger(SectionResource.class);

    private static final String ENTITY_NAME = "gestioneauSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SectionService sectionService;

    private final SectionRepository sectionRepository;

    public SectionResource(SectionService sectionService, SectionRepository sectionRepository) {
        this.sectionService = sectionService;
        this.sectionRepository = sectionRepository;
    }

    /**
     * {@code POST  /sections} : Create a new section.
     *
     * @param sectionDTO the sectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sectionDTO, or with status {@code 400 (Bad Request)} if the section has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sections")
    public ResponseEntity<SectionDTO> createSection(@Valid @RequestBody SectionDTO sectionDTO) throws URISyntaxException {
        log.debug("REST request to save Section : {}", sectionDTO);
        if (sectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new section cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SectionDTO result = sectionService.save(sectionDTO);
        return ResponseEntity
            .created(new URI("/api/sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sections/:id} : Updates an existing section.
     *
     * @param id the id of the sectionDTO to save.
     * @param sectionDTO the sectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sectionDTO,
     * or with status {@code 400 (Bad Request)} if the sectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sections/{id}")
    public ResponseEntity<SectionDTO> updateSection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SectionDTO sectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Section : {}, {}", id, sectionDTO);
        if (sectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SectionDTO result = sectionService.save(sectionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sections/:id} : Partial updates given fields of an existing section, field will ignore if it is null
     *
     * @param id the id of the sectionDTO to save.
     * @param sectionDTO the sectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sectionDTO,
     * or with status {@code 400 (Bad Request)} if the sectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sections/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SectionDTO> partialUpdateSection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SectionDTO sectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Section partially : {}, {}", id, sectionDTO);
        if (sectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SectionDTO> result = sectionService.partialUpdate(sectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sections} : get all the sections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sections in body.
     */
    @GetMapping("/sections")
    public ResponseEntity<List<SectionDTO>> getAllSections(Pageable pageable) {
        log.debug("REST request to get a page of Sections");
        Page<SectionDTO> page = sectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sections/:id} : get the "id" section.
     *
     * @param id the id of the sectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sections/{id}")
    public ResponseEntity<SectionDTO> getSection(@PathVariable Long id) {
        log.debug("REST request to get Section : {}", id);
        Optional<SectionDTO> sectionDTO = sectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sectionDTO);
    }

    /**
     * {@code DELETE  /sections/:id} : delete the "id" section.
     *
     * @param id the id of the sectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sections/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        log.debug("REST request to delete Section : {}", id);
        sectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/sections?query=:query} : search for the section corresponding
     * to the query.
     *
     * @param query the query of the section search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sections")
    public ResponseEntity<List<SectionDTO>> searchSections(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sections for query {}", query);
        Page<SectionDTO> page = sectionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
