package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.TypeCommune;
import com.onea.sidot.gestioneau.repository.TypeCommuneRepository;
import com.onea.sidot.gestioneau.repository.search.TypeCommuneSearchRepository;
import com.onea.sidot.gestioneau.service.dto.TypeCommuneDTO;
import com.onea.sidot.gestioneau.service.mapper.TypeCommuneMapper;
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
 * Integration tests for the {@link TypeCommuneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TypeCommuneResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-communes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/type-communes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeCommuneRepository typeCommuneRepository;

    @Autowired
    private TypeCommuneMapper typeCommuneMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.TypeCommuneSearchRepositoryMockConfiguration
     */
    @Autowired
    private TypeCommuneSearchRepository mockTypeCommuneSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeCommuneMockMvc;

    private TypeCommune typeCommune;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeCommune createEntity(EntityManager em) {
        TypeCommune typeCommune = new TypeCommune().libelle(DEFAULT_LIBELLE);
        return typeCommune;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeCommune createUpdatedEntity(EntityManager em) {
        TypeCommune typeCommune = new TypeCommune().libelle(UPDATED_LIBELLE);
        return typeCommune;
    }

    @BeforeEach
    public void initTest() {
        typeCommune = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeCommune() throws Exception {
        int databaseSizeBeforeCreate = typeCommuneRepository.findAll().size();
        // Create the TypeCommune
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);
        restTypeCommuneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeCreate + 1);
        TypeCommune testTypeCommune = typeCommuneList.get(typeCommuneList.size() - 1);
        assertThat(testTypeCommune.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(1)).save(testTypeCommune);
    }

    @Test
    @Transactional
    void createTypeCommuneWithExistingId() throws Exception {
        // Create the TypeCommune with an existing ID
        typeCommune.setId(1L);
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        int databaseSizeBeforeCreate = typeCommuneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeCommuneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeCreate);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(0)).save(typeCommune);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeCommuneRepository.findAll().size();
        // set the field null
        typeCommune.setLibelle(null);

        // Create the TypeCommune, which fails.
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        restTypeCommuneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isBadRequest());

        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeCommunes() throws Exception {
        // Initialize the database
        typeCommuneRepository.saveAndFlush(typeCommune);

        // Get all the typeCommuneList
        restTypeCommuneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeCommune.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeCommune() throws Exception {
        // Initialize the database
        typeCommuneRepository.saveAndFlush(typeCommune);

        // Get the typeCommune
        restTypeCommuneMockMvc
            .perform(get(ENTITY_API_URL_ID, typeCommune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeCommune.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeCommune() throws Exception {
        // Get the typeCommune
        restTypeCommuneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeCommune() throws Exception {
        // Initialize the database
        typeCommuneRepository.saveAndFlush(typeCommune);

        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();

        // Update the typeCommune
        TypeCommune updatedTypeCommune = typeCommuneRepository.findById(typeCommune.getId()).get();
        // Disconnect from session so that the updates on updatedTypeCommune are not directly saved in db
        em.detach(updatedTypeCommune);
        updatedTypeCommune.libelle(UPDATED_LIBELLE);
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(updatedTypeCommune);

        restTypeCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeCommuneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);
        TypeCommune testTypeCommune = typeCommuneList.get(typeCommuneList.size() - 1);
        assertThat(testTypeCommune.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository).save(testTypeCommune);
    }

    @Test
    @Transactional
    void putNonExistingTypeCommune() throws Exception {
        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();
        typeCommune.setId(count.incrementAndGet());

        // Create the TypeCommune
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeCommuneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(0)).save(typeCommune);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeCommune() throws Exception {
        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();
        typeCommune.setId(count.incrementAndGet());

        // Create the TypeCommune
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCommuneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(0)).save(typeCommune);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeCommune() throws Exception {
        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();
        typeCommune.setId(count.incrementAndGet());

        // Create the TypeCommune
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCommuneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(0)).save(typeCommune);
    }

    @Test
    @Transactional
    void partialUpdateTypeCommuneWithPatch() throws Exception {
        // Initialize the database
        typeCommuneRepository.saveAndFlush(typeCommune);

        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();

        // Update the typeCommune using partial update
        TypeCommune partialUpdatedTypeCommune = new TypeCommune();
        partialUpdatedTypeCommune.setId(typeCommune.getId());

        restTypeCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeCommune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeCommune))
            )
            .andExpect(status().isOk());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);
        TypeCommune testTypeCommune = typeCommuneList.get(typeCommuneList.size() - 1);
        assertThat(testTypeCommune.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateTypeCommuneWithPatch() throws Exception {
        // Initialize the database
        typeCommuneRepository.saveAndFlush(typeCommune);

        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();

        // Update the typeCommune using partial update
        TypeCommune partialUpdatedTypeCommune = new TypeCommune();
        partialUpdatedTypeCommune.setId(typeCommune.getId());

        partialUpdatedTypeCommune.libelle(UPDATED_LIBELLE);

        restTypeCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeCommune.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeCommune))
            )
            .andExpect(status().isOk());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);
        TypeCommune testTypeCommune = typeCommuneList.get(typeCommuneList.size() - 1);
        assertThat(testTypeCommune.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeCommune() throws Exception {
        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();
        typeCommune.setId(count.incrementAndGet());

        // Create the TypeCommune
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeCommuneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(0)).save(typeCommune);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeCommune() throws Exception {
        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();
        typeCommune.setId(count.incrementAndGet());

        // Create the TypeCommune
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(0)).save(typeCommune);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeCommune() throws Exception {
        int databaseSizeBeforeUpdate = typeCommuneRepository.findAll().size();
        typeCommune.setId(count.incrementAndGet());

        // Create the TypeCommune
        TypeCommuneDTO typeCommuneDTO = typeCommuneMapper.toDto(typeCommune);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeCommuneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeCommuneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeCommune in the database
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(0)).save(typeCommune);
    }

    @Test
    @Transactional
    void deleteTypeCommune() throws Exception {
        // Initialize the database
        typeCommuneRepository.saveAndFlush(typeCommune);

        int databaseSizeBeforeDelete = typeCommuneRepository.findAll().size();

        // Delete the typeCommune
        restTypeCommuneMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeCommune.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeCommune> typeCommuneList = typeCommuneRepository.findAll();
        assertThat(typeCommuneList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TypeCommune in Elasticsearch
        verify(mockTypeCommuneSearchRepository, times(1)).deleteById(typeCommune.getId());
    }

    @Test
    @Transactional
    void searchTypeCommune() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        typeCommuneRepository.saveAndFlush(typeCommune);
        when(mockTypeCommuneSearchRepository.search(queryStringQuery("id:" + typeCommune.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(typeCommune), PageRequest.of(0, 1), 1));

        // Search the typeCommune
        restTypeCommuneMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + typeCommune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeCommune.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
