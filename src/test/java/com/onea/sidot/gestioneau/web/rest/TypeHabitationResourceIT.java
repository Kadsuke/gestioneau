package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.TypeHabitation;
import com.onea.sidot.gestioneau.repository.TypeHabitationRepository;
import com.onea.sidot.gestioneau.repository.search.TypeHabitationSearchRepository;
import com.onea.sidot.gestioneau.service.dto.TypeHabitationDTO;
import com.onea.sidot.gestioneau.service.mapper.TypeHabitationMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeHabitationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TypeHabitationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-habitations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/type-habitations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeHabitationRepository typeHabitationRepository;

    @Autowired
    private TypeHabitationMapper typeHabitationMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.TypeHabitationSearchRepositoryMockConfiguration
     */
    @Autowired
    private TypeHabitationSearchRepository mockTypeHabitationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeHabitationMockMvc;

    private TypeHabitation typeHabitation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeHabitation createEntity(EntityManager em) {
        TypeHabitation typeHabitation = new TypeHabitation().libelle(DEFAULT_LIBELLE);
        return typeHabitation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeHabitation createUpdatedEntity(EntityManager em) {
        TypeHabitation typeHabitation = new TypeHabitation().libelle(UPDATED_LIBELLE);
        return typeHabitation;
    }

    @BeforeEach
    public void initTest() {
        typeHabitation = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeHabitation() throws Exception {
        int databaseSizeBeforeCreate = typeHabitationRepository.findAll().size();
        // Create the TypeHabitation
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);
        restTypeHabitationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeCreate + 1);
        TypeHabitation testTypeHabitation = typeHabitationList.get(typeHabitationList.size() - 1);
        assertThat(testTypeHabitation.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(1)).save(testTypeHabitation);
    }

    @Test
    @Transactional
    void createTypeHabitationWithExistingId() throws Exception {
        // Create the TypeHabitation with an existing ID
        typeHabitation.setId(1L);
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        int databaseSizeBeforeCreate = typeHabitationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeHabitationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeCreate);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(0)).save(typeHabitation);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeHabitationRepository.findAll().size();
        // set the field null
        typeHabitation.setLibelle(null);

        // Create the TypeHabitation, which fails.
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        restTypeHabitationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeHabitations() throws Exception {
        // Initialize the database
        typeHabitationRepository.saveAndFlush(typeHabitation);

        // Get all the typeHabitationList
        restTypeHabitationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeHabitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeHabitation() throws Exception {
        // Initialize the database
        typeHabitationRepository.saveAndFlush(typeHabitation);

        // Get the typeHabitation
        restTypeHabitationMockMvc
            .perform(get(ENTITY_API_URL_ID, typeHabitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeHabitation.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeHabitation() throws Exception {
        // Get the typeHabitation
        restTypeHabitationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeHabitation() throws Exception {
        // Initialize the database
        typeHabitationRepository.saveAndFlush(typeHabitation);

        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();

        // Update the typeHabitation
        TypeHabitation updatedTypeHabitation = typeHabitationRepository.findById(typeHabitation.getId()).get();
        // Disconnect from session so that the updates on updatedTypeHabitation are not directly saved in db
        em.detach(updatedTypeHabitation);
        updatedTypeHabitation.libelle(UPDATED_LIBELLE);
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(updatedTypeHabitation);

        restTypeHabitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeHabitationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);
        TypeHabitation testTypeHabitation = typeHabitationList.get(typeHabitationList.size() - 1);
        assertThat(testTypeHabitation.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository).save(testTypeHabitation);
    }

    @Test
    @Transactional
    void putNonExistingTypeHabitation() throws Exception {
        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();
        typeHabitation.setId(count.incrementAndGet());

        // Create the TypeHabitation
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeHabitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeHabitationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(0)).save(typeHabitation);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeHabitation() throws Exception {
        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();
        typeHabitation.setId(count.incrementAndGet());

        // Create the TypeHabitation
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHabitationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(0)).save(typeHabitation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeHabitation() throws Exception {
        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();
        typeHabitation.setId(count.incrementAndGet());

        // Create the TypeHabitation
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHabitationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(0)).save(typeHabitation);
    }

    @Test
    @Transactional
    void partialUpdateTypeHabitationWithPatch() throws Exception {
        // Initialize the database
        typeHabitationRepository.saveAndFlush(typeHabitation);

        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();

        // Update the typeHabitation using partial update
        TypeHabitation partialUpdatedTypeHabitation = new TypeHabitation();
        partialUpdatedTypeHabitation.setId(typeHabitation.getId());

        partialUpdatedTypeHabitation.libelle(UPDATED_LIBELLE);

        restTypeHabitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeHabitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeHabitation))
            )
            .andExpect(status().isOk());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);
        TypeHabitation testTypeHabitation = typeHabitationList.get(typeHabitationList.size() - 1);
        assertThat(testTypeHabitation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateTypeHabitationWithPatch() throws Exception {
        // Initialize the database
        typeHabitationRepository.saveAndFlush(typeHabitation);

        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();

        // Update the typeHabitation using partial update
        TypeHabitation partialUpdatedTypeHabitation = new TypeHabitation();
        partialUpdatedTypeHabitation.setId(typeHabitation.getId());

        partialUpdatedTypeHabitation.libelle(UPDATED_LIBELLE);

        restTypeHabitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeHabitation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeHabitation))
            )
            .andExpect(status().isOk());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);
        TypeHabitation testTypeHabitation = typeHabitationList.get(typeHabitationList.size() - 1);
        assertThat(testTypeHabitation.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeHabitation() throws Exception {
        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();
        typeHabitation.setId(count.incrementAndGet());

        // Create the TypeHabitation
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeHabitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeHabitationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(0)).save(typeHabitation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeHabitation() throws Exception {
        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();
        typeHabitation.setId(count.incrementAndGet());

        // Create the TypeHabitation
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHabitationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(0)).save(typeHabitation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeHabitation() throws Exception {
        int databaseSizeBeforeUpdate = typeHabitationRepository.findAll().size();
        typeHabitation.setId(count.incrementAndGet());

        // Create the TypeHabitation
        TypeHabitationDTO typeHabitationDTO = typeHabitationMapper.toDto(typeHabitation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHabitationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeHabitationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeHabitation in the database
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(0)).save(typeHabitation);
    }

    @Test
    @Transactional
    void deleteTypeHabitation() throws Exception {
        // Initialize the database
        typeHabitationRepository.saveAndFlush(typeHabitation);

        int databaseSizeBeforeDelete = typeHabitationRepository.findAll().size();

        // Delete the typeHabitation
        restTypeHabitationMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeHabitation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeHabitation> typeHabitationList = typeHabitationRepository.findAll();
        assertThat(typeHabitationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TypeHabitation in Elasticsearch
        verify(mockTypeHabitationSearchRepository, times(1)).deleteById(typeHabitation.getId());
    }

    @Test
    @Transactional
    void searchTypeHabitation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        typeHabitationRepository.saveAndFlush(typeHabitation);
        when(mockTypeHabitationSearchRepository.search(queryStringQuery("id:" + typeHabitation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(typeHabitation), PageRequest.of(0, 1), 1));

        // Search the typeHabitation
        restTypeHabitationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + typeHabitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeHabitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
