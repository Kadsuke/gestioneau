package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.CentreRepository;
import com.onea.sidot.gestioneau.service.CentreService;
import com.onea.sidot.gestioneau.service.dto.CentreDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Centre}.
 */
@RestController
@RequestMapping("/api")
public class CentreResource {

    private final Logger log = LoggerFactory.getLogger(CentreResource.class);

    private static final String ENTITY_NAME = "gestioneauCentre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CentreService centreService;

    private final CentreRepository centreRepository;

    public CentreResource(CentreService centreService, CentreRepository centreRepository) {
        this.centreService = centreService;
        this.centreRepository = centreRepository;
    }

    /**
     * {@code POST  /centres} : Create a new centre.
     *
     * @param centreDTO the centreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new centreDTO, or with status {@code 400 (Bad Request)} if the centre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/centres")
    public ResponseEntity<CentreDTO> createCentre(@Valid @RequestBody CentreDTO centreDTO) throws URISyntaxException {
        log.debug("REST request to save Centre : {}", centreDTO);
        if (centreDTO.getId() != null) {
            throw new BadRequestAlertException("A new centre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CentreDTO result = centreService.save(centreDTO);
        return ResponseEntity
            .created(new URI("/api/centres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /centres/:id} : Updates an existing centre.
     *
     * @param id the id of the centreDTO to save.
     * @param centreDTO the centreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centreDTO,
     * or with status {@code 400 (Bad Request)} if the centreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the centreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/centres/{id}")
    public ResponseEntity<CentreDTO> updateCentre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CentreDTO centreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Centre : {}, {}", id, centreDTO);
        if (centreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CentreDTO result = centreService.save(centreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /centres/:id} : Partial updates given fields of an existing centre, field will ignore if it is null
     *
     * @param id the id of the centreDTO to save.
     * @param centreDTO the centreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centreDTO,
     * or with status {@code 400 (Bad Request)} if the centreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/centres/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CentreDTO> partialUpdateCentre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CentreDTO centreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Centre partially : {}, {}", id, centreDTO);
        if (centreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CentreDTO> result = centreService.partialUpdate(centreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /centres} : get all the centres.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of centres in body.
     */
    @GetMapping("/centres")
    public ResponseEntity<List<CentreDTO>> getAllCentres(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("prevision-is-null".equals(filter)) {
            log.debug("REST request to get all Centres where prevision is null");
            return new ResponseEntity<>(centreService.findAllWherePrevisionIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Centres");
        Page<CentreDTO> page = centreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /centres/:id} : get the "id" centre.
     *
     * @param id the id of the centreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the centreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/centres/{id}")
    public ResponseEntity<CentreDTO> getCentre(@PathVariable Long id) {
        log.debug("REST request to get Centre : {}", id);
        Optional<CentreDTO> centreDTO = centreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(centreDTO);
    }

    /**
     * {@code DELETE  /centres/:id} : delete the "id" centre.
     *
     * @param id the id of the centreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/centres/{id}")
    public ResponseEntity<Void> deleteCentre(@PathVariable Long id) {
        log.debug("REST request to delete Centre : {}", id);
        centreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/centres?query=:query} : search for the centre corresponding
     * to the query.
     *
     * @param query the query of the centre search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/centres")
    public ResponseEntity<List<CentreDTO>> searchCentres(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Centres for query {}", query);
        Page<CentreDTO> page = centreService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
