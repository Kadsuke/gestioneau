package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.NatureOuvrage;
import com.onea.sidot.gestioneau.repository.NatureOuvrageRepository;
import com.onea.sidot.gestioneau.repository.search.NatureOuvrageSearchRepository;
import com.onea.sidot.gestioneau.service.dto.NatureOuvrageDTO;
import com.onea.sidot.gestioneau.service.mapper.NatureOuvrageMapper;
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
 * Integration tests for the {@link NatureOuvrageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NatureOuvrageResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nature-ouvrages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/nature-ouvrages";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NatureOuvrageRepository natureOuvrageRepository;

    @Autowired
    private NatureOuvrageMapper natureOuvrageMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.NatureOuvrageSearchRepositoryMockConfiguration
     */
    @Autowired
    private NatureOuvrageSearchRepository mockNatureOuvrageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNatureOuvrageMockMvc;

    private NatureOuvrage natureOuvrage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureOuvrage createEntity(EntityManager em) {
        NatureOuvrage natureOuvrage = new NatureOuvrage().libelle(DEFAULT_LIBELLE);
        return natureOuvrage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureOuvrage createUpdatedEntity(EntityManager em) {
        NatureOuvrage natureOuvrage = new NatureOuvrage().libelle(UPDATED_LIBELLE);
        return natureOuvrage;
    }

    @BeforeEach
    public void initTest() {
        natureOuvrage = createEntity(em);
    }

    @Test
    @Transactional
    void createNatureOuvrage() throws Exception {
        int databaseSizeBeforeCreate = natureOuvrageRepository.findAll().size();
        // Create the NatureOuvrage
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);
        restNatureOuvrageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeCreate + 1);
        NatureOuvrage testNatureOuvrage = natureOuvrageList.get(natureOuvrageList.size() - 1);
        assertThat(testNatureOuvrage.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(1)).save(testNatureOuvrage);
    }

    @Test
    @Transactional
    void createNatureOuvrageWithExistingId() throws Exception {
        // Create the NatureOuvrage with an existing ID
        natureOuvrage.setId(1L);
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        int databaseSizeBeforeCreate = natureOuvrageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNatureOuvrageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeCreate);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(0)).save(natureOuvrage);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = natureOuvrageRepository.findAll().size();
        // set the field null
        natureOuvrage.setLibelle(null);

        // Create the NatureOuvrage, which fails.
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        restNatureOuvrageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isBadRequest());

        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNatureOuvrages() throws Exception {
        // Initialize the database
        natureOuvrageRepository.saveAndFlush(natureOuvrage);

        // Get all the natureOuvrageList
        restNatureOuvrageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureOuvrage.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getNatureOuvrage() throws Exception {
        // Initialize the database
        natureOuvrageRepository.saveAndFlush(natureOuvrage);

        // Get the natureOuvrage
        restNatureOuvrageMockMvc
            .perform(get(ENTITY_API_URL_ID, natureOuvrage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(natureOuvrage.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingNatureOuvrage() throws Exception {
        // Get the natureOuvrage
        restNatureOuvrageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNatureOuvrage() throws Exception {
        // Initialize the database
        natureOuvrageRepository.saveAndFlush(natureOuvrage);

        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();

        // Update the natureOuvrage
        NatureOuvrage updatedNatureOuvrage = natureOuvrageRepository.findById(natureOuvrage.getId()).get();
        // Disconnect from session so that the updates on updatedNatureOuvrage are not directly saved in db
        em.detach(updatedNatureOuvrage);
        updatedNatureOuvrage.libelle(UPDATED_LIBELLE);
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(updatedNatureOuvrage);

        restNatureOuvrageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, natureOuvrageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isOk());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);
        NatureOuvrage testNatureOuvrage = natureOuvrageList.get(natureOuvrageList.size() - 1);
        assertThat(testNatureOuvrage.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository).save(testNatureOuvrage);
    }

    @Test
    @Transactional
    void putNonExistingNatureOuvrage() throws Exception {
        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();
        natureOuvrage.setId(count.incrementAndGet());

        // Create the NatureOuvrage
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNatureOuvrageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, natureOuvrageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(0)).save(natureOuvrage);
    }

    @Test
    @Transactional
    void putWithIdMismatchNatureOuvrage() throws Exception {
        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();
        natureOuvrage.setId(count.incrementAndGet());

        // Create the NatureOuvrage
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOuvrageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(0)).save(natureOuvrage);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNatureOuvrage() throws Exception {
        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();
        natureOuvrage.setId(count.incrementAndGet());

        // Create the NatureOuvrage
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOuvrageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(0)).save(natureOuvrage);
    }

    @Test
    @Transactional
    void partialUpdateNatureOuvrageWithPatch() throws Exception {
        // Initialize the database
        natureOuvrageRepository.saveAndFlush(natureOuvrage);

        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();

        // Update the natureOuvrage using partial update
        NatureOuvrage partialUpdatedNatureOuvrage = new NatureOuvrage();
        partialUpdatedNatureOuvrage.setId(natureOuvrage.getId());

        restNatureOuvrageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNatureOuvrage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureOuvrage))
            )
            .andExpect(status().isOk());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);
        NatureOuvrage testNatureOuvrage = natureOuvrageList.get(natureOuvrageList.size() - 1);
        assertThat(testNatureOuvrage.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateNatureOuvrageWithPatch() throws Exception {
        // Initialize the database
        natureOuvrageRepository.saveAndFlush(natureOuvrage);

        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();

        // Update the natureOuvrage using partial update
        NatureOuvrage partialUpdatedNatureOuvrage = new NatureOuvrage();
        partialUpdatedNatureOuvrage.setId(natureOuvrage.getId());

        partialUpdatedNatureOuvrage.libelle(UPDATED_LIBELLE);

        restNatureOuvrageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNatureOuvrage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureOuvrage))
            )
            .andExpect(status().isOk());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);
        NatureOuvrage testNatureOuvrage = natureOuvrageList.get(natureOuvrageList.size() - 1);
        assertThat(testNatureOuvrage.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingNatureOuvrage() throws Exception {
        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();
        natureOuvrage.setId(count.incrementAndGet());

        // Create the NatureOuvrage
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNatureOuvrageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, natureOuvrageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(0)).save(natureOuvrage);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNatureOuvrage() throws Exception {
        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();
        natureOuvrage.setId(count.incrementAndGet());

        // Create the NatureOuvrage
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOuvrageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(0)).save(natureOuvrage);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNatureOuvrage() throws Exception {
        int databaseSizeBeforeUpdate = natureOuvrageRepository.findAll().size();
        natureOuvrage.setId(count.incrementAndGet());

        // Create the NatureOuvrage
        NatureOuvrageDTO natureOuvrageDTO = natureOuvrageMapper.toDto(natureOuvrage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOuvrageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureOuvrageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NatureOuvrage in the database
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(0)).save(natureOuvrage);
    }

    @Test
    @Transactional
    void deleteNatureOuvrage() throws Exception {
        // Initialize the database
        natureOuvrageRepository.saveAndFlush(natureOuvrage);

        int databaseSizeBeforeDelete = natureOuvrageRepository.findAll().size();

        // Delete the natureOuvrage
        restNatureOuvrageMockMvc
            .perform(delete(ENTITY_API_URL_ID, natureOuvrage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NatureOuvrage> natureOuvrageList = natureOuvrageRepository.findAll();
        assertThat(natureOuvrageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NatureOuvrage in Elasticsearch
        verify(mockNatureOuvrageSearchRepository, times(1)).deleteById(natureOuvrage.getId());
    }

    @Test
    @Transactional
    void searchNatureOuvrage() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        natureOuvrageRepository.saveAndFlush(natureOuvrage);
        when(mockNatureOuvrageSearchRepository.search(queryStringQuery("id:" + natureOuvrage.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(natureOuvrage), PageRequest.of(0, 1), 1));

        // Search the natureOuvrage
        restNatureOuvrageMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + natureOuvrage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureOuvrage.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
