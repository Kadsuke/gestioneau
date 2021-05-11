package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.TypeCommuneRepository;
import com.onea.sidot.gestioneau.service.TypeCommuneService;
import com.onea.sidot.gestioneau.service.dto.TypeCommuneDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.TypeCommune}.
 */
@RestController
@RequestMapping("/api")
public class TypeCommuneResource {

    private final Logger log = LoggerFactory.getLogger(TypeCommuneResource.class);

    private static final String ENTITY_NAME = "gestioneauTypeCommune";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeCommuneService typeCommuneService;

    private final TypeCommuneRepository typeCommuneRepository;

    public TypeCommuneResource(TypeCommuneService typeCommuneService, TypeCommuneRepository typeCommuneRepository) {
        this.typeCommuneService = typeCommuneService;
        this.typeCommuneRepository = typeCommuneRepository;
    }

    /**
     * {@code POST  /type-communes} : Create a new typeCommune.
     *
     * @param typeCommuneDTO the typeCommuneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeCommuneDTO, or with status {@code 400 (Bad Request)} if the typeCommune has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-communes")
    public ResponseEntity<TypeCommuneDTO> createTypeCommune(@Valid @RequestBody TypeCommuneDTO typeCommuneDTO) throws URISyntaxException {
        log.debug("REST request to save TypeCommune : {}", typeCommuneDTO);
        if (typeCommuneDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeCommune cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeCommuneDTO result = typeCommuneService.save(typeCommuneDTO);
        return ResponseEntity
            .created(new URI("/api/type-communes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-communes/:id} : Updates an existing typeCommune.
     *
     * @param id the id of the typeCommuneDTO to save.
     * @param typeCommuneDTO the typeCommuneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeCommuneDTO,
     * or with status {@code 400 (Bad Request)} if the typeCommuneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeCommuneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-communes/{id}")
    public ResponseEntity<TypeCommuneDTO> updateTypeCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeCommuneDTO typeCommuneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeCommune : {}, {}", id, typeCommuneDTO);
        if (typeCommuneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeCommuneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeCommuneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeCommuneDTO result = typeCommuneService.save(typeCommuneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeCommuneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-communes/:id} : Partial updates given fields of an existing typeCommune, field will ignore if it is null
     *
     * @param id the id of the typeCommuneDTO to save.
     * @param typeCommuneDTO the typeCommuneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeCommuneDTO,
     * or with status {@code 400 (Bad Request)} if the typeCommuneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeCommuneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeCommuneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-communes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeCommuneDTO> partialUpdateTypeCommune(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeCommuneDTO typeCommuneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeCommune partially : {}, {}", id, typeCommuneDTO);
        if (typeCommuneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeCommuneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeCommuneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeCommuneDTO> result = typeCommuneService.partialUpdate(typeCommuneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeCommuneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-communes} : get all the typeCommunes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeCommunes in body.
     */
    @GetMapping("/type-communes")
    public ResponseEntity<List<TypeCommuneDTO>> getAllTypeCommunes(Pageable pageable) {
        log.debug("REST request to get a page of TypeCommunes");
        Page<TypeCommuneDTO> page = typeCommuneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-communes/:id} : get the "id" typeCommune.
     *
     * @param id the id of the typeCommuneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeCommuneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-communes/{id}")
    public ResponseEntity<TypeCommuneDTO> getTypeCommune(@PathVariable Long id) {
        log.debug("REST request to get TypeCommune : {}", id);
        Optional<TypeCommuneDTO> typeCommuneDTO = typeCommuneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeCommuneDTO);
    }

    /**
     * {@code DELETE  /type-communes/:id} : delete the "id" typeCommune.
     *
     * @param id the id of the typeCommuneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-communes/{id}")
    public ResponseEntity<Void> deleteTypeCommune(@PathVariable Long id) {
        log.debug("REST request to delete TypeCommune : {}", id);
        typeCommuneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/type-communes?query=:query} : search for the typeCommune corresponding
     * to the query.
     *
     * @param query the query of the typeCommune search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/type-communes")
    public ResponseEntity<List<TypeCommuneDTO>> searchTypeCommunes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TypeCommunes for query {}", query);
        Page<TypeCommuneDTO> page = typeCommuneService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
