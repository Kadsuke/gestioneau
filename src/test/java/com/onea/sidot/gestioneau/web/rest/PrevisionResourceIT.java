package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.Prevision;
import com.onea.sidot.gestioneau.repository.PrevisionRepository;
import com.onea.sidot.gestioneau.repository.search.PrevisionSearchRepository;
import com.onea.sidot.gestioneau.service.dto.PrevisionDTO;
import com.onea.sidot.gestioneau.service.mapper.PrevisionMapper;
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
 * Integration tests for the {@link PrevisionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrevisionResourceIT {

    private static final Integer DEFAULT_NB_LATRINE = 1;
    private static final Integer UPDATED_NB_LATRINE = 2;

    private static final Integer DEFAULT_NB_PUISARD = 1;
    private static final Integer UPDATED_NB_PUISARD = 2;

    private static final Integer DEFAULT_NB_PUBLIC = 1;
    private static final Integer UPDATED_NB_PUBLIC = 2;

    private static final Integer DEFAULT_NB_SCOLAIRE = 1;
    private static final Integer UPDATED_NB_SCOLAIRE = 2;

    private static final String ENTITY_API_URL = "/api/previsions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/previsions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrevisionRepository previsionRepository;

    @Autowired
    private PrevisionMapper previsionMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.PrevisionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrevisionSearchRepository mockPrevisionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrevisionMockMvc;

    private Prevision prevision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prevision createEntity(EntityManager em) {
        Prevision prevision = new Prevision()
            .nbLatrine(DEFAULT_NB_LATRINE)
            .nbPuisard(DEFAULT_NB_PUISARD)
            .nbPublic(DEFAULT_NB_PUBLIC)
            .nbScolaire(DEFAULT_NB_SCOLAIRE);
        return prevision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prevision createUpdatedEntity(EntityManager em) {
        Prevision prevision = new Prevision()
            .nbLatrine(UPDATED_NB_LATRINE)
            .nbPuisard(UPDATED_NB_PUISARD)
            .nbPublic(UPDATED_NB_PUBLIC)
            .nbScolaire(UPDATED_NB_SCOLAIRE);
        return prevision;
    }

    @BeforeEach
    public void initTest() {
        prevision = createEntity(em);
    }

    @Test
    @Transactional
    void createPrevision() throws Exception {
        int databaseSizeBeforeCreate = previsionRepository.findAll().size();
        // Create the Prevision
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);
        restPrevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(previsionDTO)))
            .andExpect(status().isCreated());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeCreate + 1);
        Prevision testPrevision = previsionList.get(previsionList.size() - 1);
        assertThat(testPrevision.getNbLatrine()).isEqualTo(DEFAULT_NB_LATRINE);
        assertThat(testPrevision.getNbPuisard()).isEqualTo(DEFAULT_NB_PUISARD);
        assertThat(testPrevision.getNbPublic()).isEqualTo(DEFAULT_NB_PUBLIC);
        assertThat(testPrevision.getNbScolaire()).isEqualTo(DEFAULT_NB_SCOLAIRE);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(1)).save(testPrevision);
    }

    @Test
    @Transactional
    void createPrevisionWithExistingId() throws Exception {
        // Create the Prevision with an existing ID
        prevision.setId(1L);
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        int databaseSizeBeforeCreate = previsionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(previsionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(0)).save(prevision);
    }

    @Test
    @Transactional
    void checkNbLatrineIsRequired() throws Exception {
        int databaseSizeBeforeTest = previsionRepository.findAll().size();
        // set the field null
        prevision.setNbLatrine(null);

        // Create the Prevision, which fails.
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        restPrevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(previsionDTO)))
            .andExpect(status().isBadRequest());

        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbPuisardIsRequired() throws Exception {
        int databaseSizeBeforeTest = previsionRepository.findAll().size();
        // set the field null
        prevision.setNbPuisard(null);

        // Create the Prevision, which fails.
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        restPrevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(previsionDTO)))
            .andExpect(status().isBadRequest());

        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbPublicIsRequired() throws Exception {
        int databaseSizeBeforeTest = previsionRepository.findAll().size();
        // set the field null
        prevision.setNbPublic(null);

        // Create the Prevision, which fails.
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        restPrevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(previsionDTO)))
            .andExpect(status().isBadRequest());

        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbScolaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = previsionRepository.findAll().size();
        // set the field null
        prevision.setNbScolaire(null);

        // Create the Prevision, which fails.
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        restPrevisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(previsionDTO)))
            .andExpect(status().isBadRequest());

        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrevisions() throws Exception {
        // Initialize the database
        previsionRepository.saveAndFlush(prevision);

        // Get all the previsionList
        restPrevisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prevision.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbLatrine").value(hasItem(DEFAULT_NB_LATRINE)))
            .andExpect(jsonPath("$.[*].nbPuisard").value(hasItem(DEFAULT_NB_PUISARD)))
            .andExpect(jsonPath("$.[*].nbPublic").value(hasItem(DEFAULT_NB_PUBLIC)))
            .andExpect(jsonPath("$.[*].nbScolaire").value(hasItem(DEFAULT_NB_SCOLAIRE)));
    }

    @Test
    @Transactional
    void getPrevision() throws Exception {
        // Initialize the database
        previsionRepository.saveAndFlush(prevision);

        // Get the prevision
        restPrevisionMockMvc
            .perform(get(ENTITY_API_URL_ID, prevision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prevision.getId().intValue()))
            .andExpect(jsonPath("$.nbLatrine").value(DEFAULT_NB_LATRINE))
            .andExpect(jsonPath("$.nbPuisard").value(DEFAULT_NB_PUISARD))
            .andExpect(jsonPath("$.nbPublic").value(DEFAULT_NB_PUBLIC))
            .andExpect(jsonPath("$.nbScolaire").value(DEFAULT_NB_SCOLAIRE));
    }

    @Test
    @Transactional
    void getNonExistingPrevision() throws Exception {
        // Get the prevision
        restPrevisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrevision() throws Exception {
        // Initialize the database
        previsionRepository.saveAndFlush(prevision);

        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();

        // Update the prevision
        Prevision updatedPrevision = previsionRepository.findById(prevision.getId()).get();
        // Disconnect from session so that the updates on updatedPrevision are not directly saved in db
        em.detach(updatedPrevision);
        updatedPrevision
            .nbLatrine(UPDATED_NB_LATRINE)
            .nbPuisard(UPDATED_NB_PUISARD)
            .nbPublic(UPDATED_NB_PUBLIC)
            .nbScolaire(UPDATED_NB_SCOLAIRE);
        PrevisionDTO previsionDTO = previsionMapper.toDto(updatedPrevision);

        restPrevisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, previsionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(previsionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);
        Prevision testPrevision = previsionList.get(previsionList.size() - 1);
        assertThat(testPrevision.getNbLatrine()).isEqualTo(UPDATED_NB_LATRINE);
        assertThat(testPrevision.getNbPuisard()).isEqualTo(UPDATED_NB_PUISARD);
        assertThat(testPrevision.getNbPublic()).isEqualTo(UPDATED_NB_PUBLIC);
        assertThat(testPrevision.getNbScolaire()).isEqualTo(UPDATED_NB_SCOLAIRE);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository).save(testPrevision);
    }

    @Test
    @Transactional
    void putNonExistingPrevision() throws Exception {
        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();
        prevision.setId(count.incrementAndGet());

        // Create the Prevision
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrevisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, previsionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(previsionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(0)).save(prevision);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrevision() throws Exception {
        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();
        prevision.setId(count.incrementAndGet());

        // Create the Prevision
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrevisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(previsionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(0)).save(prevision);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrevision() throws Exception {
        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();
        prevision.setId(count.incrementAndGet());

        // Create the Prevision
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrevisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(previsionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(0)).save(prevision);
    }

    @Test
    @Transactional
    void partialUpdatePrevisionWithPatch() throws Exception {
        // Initialize the database
        previsionRepository.saveAndFlush(prevision);

        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();

        // Update the prevision using partial update
        Prevision partialUpdatedPrevision = new Prevision();
        partialUpdatedPrevision.setId(prevision.getId());

        partialUpdatedPrevision
            .nbLatrine(UPDATED_NB_LATRINE)
            .nbPuisard(UPDATED_NB_PUISARD)
            .nbPublic(UPDATED_NB_PUBLIC)
            .nbScolaire(UPDATED_NB_SCOLAIRE);

        restPrevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrevision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrevision))
            )
            .andExpect(status().isOk());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);
        Prevision testPrevision = previsionList.get(previsionList.size() - 1);
        assertThat(testPrevision.getNbLatrine()).isEqualTo(UPDATED_NB_LATRINE);
        assertThat(testPrevision.getNbPuisard()).isEqualTo(UPDATED_NB_PUISARD);
        assertThat(testPrevision.getNbPublic()).isEqualTo(UPDATED_NB_PUBLIC);
        assertThat(testPrevision.getNbScolaire()).isEqualTo(UPDATED_NB_SCOLAIRE);
    }

    @Test
    @Transactional
    void fullUpdatePrevisionWithPatch() throws Exception {
        // Initialize the database
        previsionRepository.saveAndFlush(prevision);

        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();

        // Update the prevision using partial update
        Prevision partialUpdatedPrevision = new Prevision();
        partialUpdatedPrevision.setId(prevision.getId());

        partialUpdatedPrevision
            .nbLatrine(UPDATED_NB_LATRINE)
            .nbPuisard(UPDATED_NB_PUISARD)
            .nbPublic(UPDATED_NB_PUBLIC)
            .nbScolaire(UPDATED_NB_SCOLAIRE);

        restPrevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrevision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrevision))
            )
            .andExpect(status().isOk());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);
        Prevision testPrevision = previsionList.get(previsionList.size() - 1);
        assertThat(testPrevision.getNbLatrine()).isEqualTo(UPDATED_NB_LATRINE);
        assertThat(testPrevision.getNbPuisard()).isEqualTo(UPDATED_NB_PUISARD);
        assertThat(testPrevision.getNbPublic()).isEqualTo(UPDATED_NB_PUBLIC);
        assertThat(testPrevision.getNbScolaire()).isEqualTo(UPDATED_NB_SCOLAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingPrevision() throws Exception {
        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();
        prevision.setId(count.incrementAndGet());

        // Create the Prevision
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, previsionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(previsionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(0)).save(prevision);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrevision() throws Exception {
        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();
        prevision.setId(count.incrementAndGet());

        // Create the Prevision
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrevisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(previsionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(0)).save(prevision);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrevision() throws Exception {
        int databaseSizeBeforeUpdate = previsionRepository.findAll().size();
        prevision.setId(count.incrementAndGet());

        // Create the Prevision
        PrevisionDTO previsionDTO = previsionMapper.toDto(prevision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrevisionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(previsionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prevision in the database
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(0)).save(prevision);
    }

    @Test
    @Transactional
    void deletePrevision() throws Exception {
        // Initialize the database
        previsionRepository.saveAndFlush(prevision);

        int databaseSizeBeforeDelete = previsionRepository.findAll().size();

        // Delete the prevision
        restPrevisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prevision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prevision> previsionList = previsionRepository.findAll();
        assertThat(previsionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Prevision in Elasticsearch
        verify(mockPrevisionSearchRepository, times(1)).deleteById(prevision.getId());
    }

    @Test
    @Transactional
    void searchPrevision() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        previsionRepository.saveAndFlush(prevision);
        when(mockPrevisionSearchRepository.search(queryStringQuery("id:" + prevision.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prevision), PageRequest.of(0, 1), 1));

        // Search the prevision
        restPrevisionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prevision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prevision.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbLatrine").value(hasItem(DEFAULT_NB_LATRINE)))
            .andExpect(jsonPath("$.[*].nbPuisard").value(hasItem(DEFAULT_NB_PUISARD)))
            .andExpect(jsonPath("$.[*].nbPublic").value(hasItem(DEFAULT_NB_PUBLIC)))
            .andExpect(jsonPath("$.[*].nbScolaire").value(hasItem(DEFAULT_NB_SCOLAIRE)));
    }
}
