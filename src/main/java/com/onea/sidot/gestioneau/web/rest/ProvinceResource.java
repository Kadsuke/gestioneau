package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.ProvinceRepository;
import com.onea.sidot.gestioneau.service.ProvinceService;
import com.onea.sidot.gestioneau.service.dto.ProvinceDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.Province}.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);

    private static final String ENTITY_NAME = "gestioneauProvince";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProvinceService provinceService;

    private final ProvinceRepository provinceRepository;

    public ProvinceResource(ProvinceService provinceService, ProvinceRepository provinceRepository) {
        this.provinceService = provinceService;
        this.provinceRepository = provinceRepository;
    }

    /**
     * {@code POST  /provinces} : Create a new province.
     *
     * @param provinceDTO the provinceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provinceDTO, or with status {@code 400 (Bad Request)} if the province has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/provinces")
    public ResponseEntity<ProvinceDTO> createProvince(@Valid @RequestBody ProvinceDTO provinceDTO) throws URISyntaxException {
        log.debug("REST request to save Province : {}", provinceDTO);
        if (provinceDTO.getId() != null) {
            throw new BadRequestAlertException("A new province cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProvinceDTO result = provinceService.save(provinceDTO);
        return ResponseEntity
            .created(new URI("/api/provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provinces/:id} : Updates an existing province.
     *
     * @param id the id of the provinceDTO to save.
     * @param provinceDTO the provinceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provinceDTO,
     * or with status {@code 400 (Bad Request)} if the provinceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provinceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/provinces/{id}")
    public ResponseEntity<ProvinceDTO> updateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProvinceDTO provinceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Province : {}, {}", id, provinceDTO);
        if (provinceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provinceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!provinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProvinceDTO result = provinceService.save(provinceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provinceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /provinces/:id} : Partial updates given fields of an existing province, field will ignore if it is null
     *
     * @param id the id of the provinceDTO to save.
     * @param provinceDTO the provinceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provinceDTO,
     * or with status {@code 400 (Bad Request)} if the provinceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the provinceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the provinceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/provinces/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProvinceDTO> partialUpdateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProvinceDTO provinceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Province partially : {}, {}", id, provinceDTO);
        if (provinceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provinceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!provinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProvinceDTO> result = provinceService.partialUpdate(provinceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provinceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /provinces} : get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of provinces in body.
     */
    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces(Pageable pageable) {
        log.debug("REST request to get a page of Provinces");
        Page<ProvinceDTO> page = provinceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /provinces/:id} : get the "id" province.
     *
     * @param id the id of the provinceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provinceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/provinces/{id}")
    public ResponseEntity<ProvinceDTO> getProvince(@PathVariable Long id) {
        log.debug("REST request to get Province : {}", id);
        Optional<ProvinceDTO> provinceDTO = provinceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(provinceDTO);
    }

    /**
     * {@code DELETE  /provinces/:id} : delete the "id" province.
     *
     * @param id the id of the provinceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        log.debug("REST request to delete Province : {}", id);
        provinceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/provinces?query=:query} : search for the province corresponding
     * to the query.
     *
     * @param query the query of the province search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/provinces")
    public ResponseEntity<List<ProvinceDTO>> searchProvinces(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Provinces for query {}", query);
        Page<ProvinceDTO> page = provinceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
