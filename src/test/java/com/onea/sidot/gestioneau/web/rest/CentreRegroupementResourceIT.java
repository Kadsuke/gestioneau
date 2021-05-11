package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.CentreRegroupement;
import com.onea.sidot.gestioneau.repository.CentreRegroupementRepository;
import com.onea.sidot.gestioneau.repository.search.CentreRegroupementSearchRepository;
import com.onea.sidot.gestioneau.service.dto.CentreRegroupementDTO;
import com.onea.sidot.gestioneau.service.mapper.CentreRegroupementMapper;
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
 * Integration tests for the {@link CentreRegroupementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CentreRegroupementResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/centre-regroupements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/centre-regroupements";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CentreRegroupementRepository centreRegroupementRepository;

    @Autowired
    private CentreRegroupementMapper centreRegroupementMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.CentreRegroupementSearchRepositoryMockConfiguration
     */
    @Autowired
    private CentreRegroupementSearchRepository mockCentreRegroupementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCentreRegroupementMockMvc;

    private CentreRegroupement centreRegroupement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CentreRegroupement createEntity(EntityManager em) {
        CentreRegroupement centreRegroupement = new CentreRegroupement()
            .libelle(DEFAULT_LIBELLE)
            .responsable(DEFAULT_RESPONSABLE)
            .contact(DEFAULT_CONTACT);
        return centreRegroupement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CentreRegroupement createUpdatedEntity(EntityManager em) {
        CentreRegroupement centreRegroupement = new CentreRegroupement()
            .libelle(UPDATED_LIBELLE)
            .responsable(UPDATED_RESPONSABLE)
            .contact(UPDATED_CONTACT);
        return centreRegroupement;
    }

    @BeforeEach
    public void initTest() {
        centreRegroupement = createEntity(em);
    }

    @Test
    @Transactional
    void createCentreRegroupement() throws Exception {
        int databaseSizeBeforeCreate = centreRegroupementRepository.findAll().size();
        // Create the CentreRegroupement
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);
        restCentreRegroupementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeCreate + 1);
        CentreRegroupement testCentreRegroupement = centreRegroupementList.get(centreRegroupementList.size() - 1);
        assertThat(testCentreRegroupement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCentreRegroupement.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testCentreRegroupement.getContact()).isEqualTo(DEFAULT_CONTACT);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(1)).save(testCentreRegroupement);
    }

    @Test
    @Transactional
    void createCentreRegroupementWithExistingId() throws Exception {
        // Create the CentreRegroupement with an existing ID
        centreRegroupement.setId(1L);
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        int databaseSizeBeforeCreate = centreRegroupementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCentreRegroupementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeCreate);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(0)).save(centreRegroupement);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRegroupementRepository.findAll().size();
        // set the field null
        centreRegroupement.setLibelle(null);

        // Create the CentreRegroupement, which fails.
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        restCentreRegroupementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRegroupementRepository.findAll().size();
        // set the field null
        centreRegroupement.setResponsable(null);

        // Create the CentreRegroupement, which fails.
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        restCentreRegroupementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRegroupementRepository.findAll().size();
        // set the field null
        centreRegroupement.setContact(null);

        // Create the CentreRegroupement, which fails.
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        restCentreRegroupementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCentreRegroupements() throws Exception {
        // Initialize the database
        centreRegroupementRepository.saveAndFlush(centreRegroupement);

        // Get all the centreRegroupementList
        restCentreRegroupementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centreRegroupement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @Test
    @Transactional
    void getCentreRegroupement() throws Exception {
        // Initialize the database
        centreRegroupementRepository.saveAndFlush(centreRegroupement);

        // Get the centreRegroupement
        restCentreRegroupementMockMvc
            .perform(get(ENTITY_API_URL_ID, centreRegroupement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(centreRegroupement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingCentreRegroupement() throws Exception {
        // Get the centreRegroupement
        restCentreRegroupementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCentreRegroupement() throws Exception {
        // Initialize the database
        centreRegroupementRepository.saveAndFlush(centreRegroupement);

        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();

        // Update the centreRegroupement
        CentreRegroupement updatedCentreRegroupement = centreRegroupementRepository.findById(centreRegroupement.getId()).get();
        // Disconnect from session so that the updates on updatedCentreRegroupement are not directly saved in db
        em.detach(updatedCentreRegroupement);
        updatedCentreRegroupement.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(updatedCentreRegroupement);

        restCentreRegroupementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centreRegroupementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isOk());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);
        CentreRegroupement testCentreRegroupement = centreRegroupementList.get(centreRegroupementList.size() - 1);
        assertThat(testCentreRegroupement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCentreRegroupement.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testCentreRegroupement.getContact()).isEqualTo(UPDATED_CONTACT);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository).save(testCentreRegroupement);
    }

    @Test
    @Transactional
    void putNonExistingCentreRegroupement() throws Exception {
        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();
        centreRegroupement.setId(count.incrementAndGet());

        // Create the CentreRegroupement
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentreRegroupementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centreRegroupementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(0)).save(centreRegroupement);
    }

    @Test
    @Transactional
    void putWithIdMismatchCentreRegroupement() throws Exception {
        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();
        centreRegroupement.setId(count.incrementAndGet());

        // Create the CentreRegroupement
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreRegroupementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(0)).save(centreRegroupement);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCentreRegroupement() throws Exception {
        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();
        centreRegroupement.setId(count.incrementAndGet());

        // Create the CentreRegroupement
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreRegroupementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(0)).save(centreRegroupement);
    }

    @Test
    @Transactional
    void partialUpdateCentreRegroupementWithPatch() throws Exception {
        // Initialize the database
        centreRegroupementRepository.saveAndFlush(centreRegroupement);

        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();

        // Update the centreRegroupement using partial update
        CentreRegroupement partialUpdatedCentreRegroupement = new CentreRegroupement();
        partialUpdatedCentreRegroupement.setId(centreRegroupement.getId());

        partialUpdatedCentreRegroupement.libelle(UPDATED_LIBELLE);

        restCentreRegroupementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentreRegroupement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCentreRegroupement))
            )
            .andExpect(status().isOk());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);
        CentreRegroupement testCentreRegroupement = centreRegroupementList.get(centreRegroupementList.size() - 1);
        assertThat(testCentreRegroupement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCentreRegroupement.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testCentreRegroupement.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdateCentreRegroupementWithPatch() throws Exception {
        // Initialize the database
        centreRegroupementRepository.saveAndFlush(centreRegroupement);

        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();

        // Update the centreRegroupement using partial update
        CentreRegroupement partialUpdatedCentreRegroupement = new CentreRegroupement();
        partialUpdatedCentreRegroupement.setId(centreRegroupement.getId());

        partialUpdatedCentreRegroupement.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        restCentreRegroupementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentreRegroupement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCentreRegroupement))
            )
            .andExpect(status().isOk());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);
        CentreRegroupement testCentreRegroupement = centreRegroupementList.get(centreRegroupementList.size() - 1);
        assertThat(testCentreRegroupement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCentreRegroupement.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testCentreRegroupement.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingCentreRegroupement() throws Exception {
        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();
        centreRegroupement.setId(count.incrementAndGet());

        // Create the CentreRegroupement
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentreRegroupementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, centreRegroupementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(0)).save(centreRegroupement);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCentreRegroupement() throws Exception {
        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();
        centreRegroupement.setId(count.incrementAndGet());

        // Create the CentreRegroupement
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreRegroupementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(0)).save(centreRegroupement);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCentreRegroupement() throws Exception {
        int databaseSizeBeforeUpdate = centreRegroupementRepository.findAll().size();
        centreRegroupement.setId(count.incrementAndGet());

        // Create the CentreRegroupement
        CentreRegroupementDTO centreRegroupementDTO = centreRegroupementMapper.toDto(centreRegroupement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreRegroupementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(centreRegroupementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CentreRegroupement in the database
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(0)).save(centreRegroupement);
    }

    @Test
    @Transactional
    void deleteCentreRegroupement() throws Exception {
        // Initialize the database
        centreRegroupementRepository.saveAndFlush(centreRegroupement);

        int databaseSizeBeforeDelete = centreRegroupementRepository.findAll().size();

        // Delete the centreRegroupement
        restCentreRegroupementMockMvc
            .perform(delete(ENTITY_API_URL_ID, centreRegroupement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CentreRegroupement> centreRegroupementList = centreRegroupementRepository.findAll();
        assertThat(centreRegroupementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CentreRegroupement in Elasticsearch
        verify(mockCentreRegroupementSearchRepository, times(1)).deleteById(centreRegroupement.getId());
    }

    @Test
    @Transactional
    void searchCentreRegroupement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        centreRegroupementRepository.saveAndFlush(centreRegroupement);
        when(mockCentreRegroupementSearchRepository.search(queryStringQuery("id:" + centreRegroupement.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(centreRegroupement), PageRequest.of(0, 1), 1));

        // Search the centreRegroupement
        restCentreRegroupementMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + centreRegroupement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centreRegroupement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }
}
