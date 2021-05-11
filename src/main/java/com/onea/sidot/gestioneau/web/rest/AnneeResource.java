package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.AnneeRepository;
import com.onea.sidot.gestioneau.service.AnneeService;
import com.onea.sidot.gestioneau.service.dto.AnneeDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Annee}.
 */
@RestController
@RequestMapping("/api")
public class AnneeResource {

    private final Logger log = LoggerFactory.getLogger(AnneeResource.class);

    private static final String ENTITY_NAME = "gestioneauAnnee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnneeService anneeService;

    private final AnneeRepository anneeRepository;

    public AnneeResource(AnneeService anneeService, AnneeRepository anneeRepository) {
        this.anneeService = anneeService;
        this.anneeRepository = anneeRepository;
    }

    /**
     * {@code POST  /annees} : Create a new annee.
     *
     * @param anneeDTO the anneeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new anneeDTO, or with status {@code 400 (Bad Request)} if the annee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annees")
    public ResponseEntity<AnneeDTO> createAnnee(@Valid @RequestBody AnneeDTO anneeDTO) throws URISyntaxException {
        log.debug("REST request to save Annee : {}", anneeDTO);
        if (anneeDTO.getId() != null) {
            throw new BadRequestAlertException("A new annee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnneeDTO result = anneeService.save(anneeDTO);
        return ResponseEntity
            .created(new URI("/api/annees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annees/:id} : Updates an existing annee.
     *
     * @param id the id of the anneeDTO to save.
     * @param anneeDTO the anneeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anneeDTO,
     * or with status {@code 400 (Bad Request)} if the anneeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the anneeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annees/{id}")
    public ResponseEntity<AnneeDTO> updateAnnee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnneeDTO anneeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Annee : {}, {}", id, anneeDTO);
        if (anneeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anneeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnneeDTO result = anneeService.save(anneeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anneeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /annees/:id} : Partial updates given fields of an existing annee, field will ignore if it is null
     *
     * @param id the id of the anneeDTO to save.
     * @param anneeDTO the anneeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anneeDTO,
     * or with status {@code 400 (Bad Request)} if the anneeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the anneeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the anneeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annees/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AnneeDTO> partialUpdateAnnee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnneeDTO anneeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Annee partially : {}, {}", id, anneeDTO);
        if (anneeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anneeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnneeDTO> result = anneeService.partialUpdate(anneeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anneeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /annees} : get all the annees.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annees in body.
     */
    @GetMapping("/annees")
    public ResponseEntity<List<AnneeDTO>> getAllAnnees(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("prevision-is-null".equals(filter)) {
            log.debug("REST request to get all Annees where prevision is null");
            return new ResponseEntity<>(anneeService.findAllWherePrevisionIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Annees");
        Page<AnneeDTO> page = anneeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annees/:id} : get the "id" annee.
     *
     * @param id the id of the anneeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the anneeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annees/{id}")
    public ResponseEntity<AnneeDTO> getAnnee(@PathVariable Long id) {
        log.debug("REST request to get Annee : {}", id);
        Optional<AnneeDTO> anneeDTO = anneeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(anneeDTO);
    }

    /**
     * {@code DELETE  /annees/:id} : delete the "id" annee.
     *
     * @param id the id of the anneeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annees/{id}")
    public ResponseEntity<Void> deleteAnnee(@PathVariable Long id) {
        log.debug("REST request to delete Annee : {}", id);
        anneeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/annees?query=:query} : search for the annee corresponding
     * to the query.
     *
     * @param query the query of the annee search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/annees")
    public ResponseEntity<List<AnneeDTO>> searchAnnees(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Annees for query {}", query);
        Page<AnneeDTO> page = anneeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
