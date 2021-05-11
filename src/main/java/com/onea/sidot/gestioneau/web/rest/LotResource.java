package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.LotRepository;
import com.onea.sidot.gestioneau.service.LotService;
import com.onea.sidot.gestioneau.service.dto.LotDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Lot}.
 */
@RestController
@RequestMapping("/api")
public class LotResource {

    private final Logger log = LoggerFactory.getLogger(LotResource.class);

    private static final String ENTITY_NAME = "gestioneauLot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LotService lotService;

    private final LotRepository lotRepository;

    public LotResource(LotService lotService, LotRepository lotRepository) {
        this.lotService = lotService;
        this.lotRepository = lotRepository;
    }

    /**
     * {@code POST  /lots} : Create a new lot.
     *
     * @param lotDTO the lotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lotDTO, or with status {@code 400 (Bad Request)} if the lot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lots")
    public ResponseEntity<LotDTO> createLot(@Valid @RequestBody LotDTO lotDTO) throws URISyntaxException {
        log.debug("REST request to save Lot : {}", lotDTO);
        if (lotDTO.getId() != null) {
            throw new BadRequestAlertException("A new lot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LotDTO result = lotService.save(lotDTO);
        return ResponseEntity
            .created(new URI("/api/lots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lots/:id} : Updates an existing lot.
     *
     * @param id the id of the lotDTO to save.
     * @param lotDTO the lotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotDTO,
     * or with status {@code 400 (Bad Request)} if the lotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lots/{id}")
    public ResponseEntity<LotDTO> updateLot(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody LotDTO lotDTO)
        throws URISyntaxException {
        log.debug("REST request to update Lot : {}, {}", id, lotDTO);
        if (lotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LotDTO result = lotService.save(lotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lots/:id} : Partial updates given fields of an existing lot, field will ignore if it is null
     *
     * @param id the id of the lotDTO to save.
     * @param lotDTO the lotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotDTO,
     * or with status {@code 400 (Bad Request)} if the lotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LotDTO> partialUpdateLot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LotDTO lotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lot partially : {}, {}", id, lotDTO);
        if (lotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LotDTO> result = lotService.partialUpdate(lotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lots} : get all the lots.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lots in body.
     */
    @GetMapping("/lots")
    public ResponseEntity<List<LotDTO>> getAllLots(Pageable pageable) {
        log.debug("REST request to get a page of Lots");
        Page<LotDTO> page = lotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lots/:id} : get the "id" lot.
     *
     * @param id the id of the lotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lots/{id}")
    public ResponseEntity<LotDTO> getLot(@PathVariable Long id) {
        log.debug("REST request to get Lot : {}", id);
        Optional<LotDTO> lotDTO = lotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lotDTO);
    }

    /**
     * {@code DELETE  /lots/:id} : delete the "id" lot.
     *
     * @param id the id of the lotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lots/{id}")
    public ResponseEntity<Void> deleteLot(@PathVariable Long id) {
        log.debug("REST request to delete Lot : {}", id);
        lotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lots?query=:query} : search for the lot corresponding
     * to the query.
     *
     * @param query the query of the lot search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lots")
    public ResponseEntity<List<LotDTO>> searchLots(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Lots for query {}", query);
        Page<LotDTO> page = lotService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
