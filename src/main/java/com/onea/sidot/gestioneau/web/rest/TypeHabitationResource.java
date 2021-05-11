package com.onea.sidot.gestioneau.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.onea.sidot.gestioneau.repository.TypeHabitationRepository;
import com.onea.sidot.gestioneau.service.TypeHabitationService;
import com.onea.sidot.gestioneau.service.dto.TypeHabitationDTO;
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
 * REST controller for managing {@link com.onea.sidot.gestioneau.domain.TypeHabitation}.
 */
@RestController
@RequestMapping("/api")
public class TypeHabitationResource {

    private final Logger log = LoggerFactory.getLogger(TypeHabitationResource.class);

    private static final String ENTITY_NAME = "gestioneauTypeHabitation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeHabitationService typeHabitationService;

    private final TypeHabitationRepository typeHabitationRepository;

    public TypeHabitationResource(TypeHabitationService typeHabitationService, TypeHabitationRepository typeHabitationRepository) {
        this.typeHabitationService = typeHabitationService;
        this.typeHabitationRepository = typeHabitationRepository;
    }

    /**
     * {@code POST  /type-habitations} : Create a new typeHabitation.
     *
     * @param typeHabitationDTO the typeHabitationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeHabitationDTO, or with status {@code 400 (Bad Request)} if the typeHabitation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-habitations")
    public ResponseEntity<TypeHabitationDTO> createTypeHabitation(@Valid @RequestBody TypeHabitationDTO typeHabitationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeHabitation : {}", typeHabitationDTO);
        if (typeHabitationDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeHabitation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeHabitationDTO result = typeHabitationService.save(typeHabitationDTO);
        return ResponseEntity
            .created(new URI("/api/type-habitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-habitations/:id} : Updates an existing typeHabitation.
     *
     * @param id the id of the typeHabitationDTO to save.
     * @param typeHabitationDTO the typeHabitationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeHabitationDTO,
     * or with status {@code 400 (Bad Request)} if the typeHabitationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeHabitationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-habitations/{id}")
    public ResponseEntity<TypeHabitationDTO> updateTypeHabitation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeHabitationDTO typeHabitationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeHabitation : {}, {}", id, typeHabitationDTO);
        if (typeHabitationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeHabitationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeHabitationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeHabitationDTO result = typeHabitationService.save(typeHabitationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeHabitationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-habitations/:id} : Partial updates given fields of an existing typeHabitation, field will ignore if it is null
     *
     * @param id the id of the typeHabitationDTO to save.
     * @param typeHabitationDTO the typeHabitationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeHabitationDTO,
     * or with status {@code 400 (Bad Request)} if the typeHabitationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeHabitationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeHabitationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-habitations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeHabitationDTO> partialUpdateTypeHabitation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeHabitationDTO typeHabitationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeHabitation partially : {}, {}", id, typeHabitationDTO);
        if (typeHabitationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeHabitationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeHabitationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeHabitationDTO> result = typeHabitationService.partialUpdate(typeHabitationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeHabitationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-habitations} : get all the typeHabitations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeHabitations in body.
     */
    @GetMapping("/type-habitations")
    public ResponseEntity<List<TypeHabitationDTO>> getAllTypeHabitations(Pageable pageable) {
        log.debug("REST request to get a page of TypeHabitations");
        Page<TypeHabitationDTO> page = typeHabitationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-habitations/:id} : get the "id" typeHabitation.
     *
     * @param id the id of the typeHabitationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeHabitationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-habitations/{id}")
    public ResponseEntity<TypeHabitationDTO> getTypeHabitation(@PathVariable Long id) {
        log.debug("REST request to get TypeHabitation : {}", id);
        Optional<TypeHabitationDTO> typeHabitationDTO = typeHabitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeHabitationDTO);
    }

    /**
     * {@code DELETE  /type-habitations/:id} : delete the "id" typeHabitation.
     *
     * @param id the id of the typeHabitationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-habitations/{id}")
    public ResponseEntity<Void> deleteTypeHabitation(@PathVariable Long id) {
        log.debug("REST request to delete TypeHabitation : {}", id);
        typeHabitationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/type-habitations?query=:query} : search for the typeHabitation corresponding
     * to the query.
     *
     * @param query the query of the typeHabitation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/type-habitations")
    public ResponseEntity<List<TypeHabitationDTO>> searchTypeHabitations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TypeHabitations for query {}", query);
        Page<TypeHabitationDTO> page = typeHabitationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
