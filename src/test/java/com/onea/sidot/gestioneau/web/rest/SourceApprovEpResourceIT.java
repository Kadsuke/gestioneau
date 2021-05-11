package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.SourceApprovEp;
import com.onea.sidot.gestioneau.repository.SourceApprovEpRepository;
import com.onea.sidot.gestioneau.repository.search.SourceApprovEpSearchRepository;
import com.onea.sidot.gestioneau.service.dto.SourceApprovEpDTO;
import com.onea.sidot.gestioneau.service.mapper.SourceApprovEpMapper;
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
 * Integration tests for the {@link SourceApprovEpResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SourceApprovEpResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/source-approv-eps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/source-approv-eps";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SourceApprovEpRepository sourceApprovEpRepository;

    @Autowired
    private SourceApprovEpMapper sourceApprovEpMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.SourceApprovEpSearchRepositoryMockConfiguration
     */
    @Autowired
    private SourceApprovEpSearchRepository mockSourceApprovEpSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceApprovEpMockMvc;

    private SourceApprovEp sourceApprovEp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceApprovEp createEntity(EntityManager em) {
        SourceApprovEp sourceApprovEp = new SourceApprovEp().libelle(DEFAULT_LIBELLE);
        return sourceApprovEp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceApprovEp createUpdatedEntity(EntityManager em) {
        SourceApprovEp sourceApprovEp = new SourceApprovEp().libelle(UPDATED_LIBELLE);
        return sourceApprovEp;
    }

    @BeforeEach
    public void initTest() {
        sourceApprovEp = createEntity(em);
    }

    @Test
    @Transactional
    void createSourceApprovEp() throws Exception {
        int databaseSizeBeforeCreate = sourceApprovEpRepository.findAll().size();
        // Create the SourceApprovEp
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);
        restSourceApprovEpMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeCreate + 1);
        SourceApprovEp testSourceApprovEp = sourceApprovEpList.get(sourceApprovEpList.size() - 1);
        assertThat(testSourceApprovEp.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(1)).save(testSourceApprovEp);
    }

    @Test
    @Transactional
    void createSourceApprovEpWithExistingId() throws Exception {
        // Create the SourceApprovEp with an existing ID
        sourceApprovEp.setId(1L);
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        int databaseSizeBeforeCreate = sourceApprovEpRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceApprovEpMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeCreate);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(0)).save(sourceApprovEp);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceApprovEpRepository.findAll().size();
        // set the field null
        sourceApprovEp.setLibelle(null);

        // Create the SourceApprovEp, which fails.
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        restSourceApprovEpMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isBadRequest());

        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSourceApprovEps() throws Exception {
        // Initialize the database
        sourceApprovEpRepository.saveAndFlush(sourceApprovEp);

        // Get all the sourceApprovEpList
        restSourceApprovEpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceApprovEp.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getSourceApprovEp() throws Exception {
        // Initialize the database
        sourceApprovEpRepository.saveAndFlush(sourceApprovEp);

        // Get the sourceApprovEp
        restSourceApprovEpMockMvc
            .perform(get(ENTITY_API_URL_ID, sourceApprovEp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourceApprovEp.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingSourceApprovEp() throws Exception {
        // Get the sourceApprovEp
        restSourceApprovEpMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSourceApprovEp() throws Exception {
        // Initialize the database
        sourceApprovEpRepository.saveAndFlush(sourceApprovEp);

        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();

        // Update the sourceApprovEp
        SourceApprovEp updatedSourceApprovEp = sourceApprovEpRepository.findById(sourceApprovEp.getId()).get();
        // Disconnect from session so that the updates on updatedSourceApprovEp are not directly saved in db
        em.detach(updatedSourceApprovEp);
        updatedSourceApprovEp.libelle(UPDATED_LIBELLE);
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(updatedSourceApprovEp);

        restSourceApprovEpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceApprovEpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isOk());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);
        SourceApprovEp testSourceApprovEp = sourceApprovEpList.get(sourceApprovEpList.size() - 1);
        assertThat(testSourceApprovEp.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository).save(testSourceApprovEp);
    }

    @Test
    @Transactional
    void putNonExistingSourceApprovEp() throws Exception {
        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();
        sourceApprovEp.setId(count.incrementAndGet());

        // Create the SourceApprovEp
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceApprovEpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceApprovEpDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(0)).save(sourceApprovEp);
    }

    @Test
    @Transactional
    void putWithIdMismatchSourceApprovEp() throws Exception {
        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();
        sourceApprovEp.setId(count.incrementAndGet());

        // Create the SourceApprovEp
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApprovEpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(0)).save(sourceApprovEp);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSourceApprovEp() throws Exception {
        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();
        sourceApprovEp.setId(count.incrementAndGet());

        // Create the SourceApprovEp
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApprovEpMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(0)).save(sourceApprovEp);
    }

    @Test
    @Transactional
    void partialUpdateSourceApprovEpWithPatch() throws Exception {
        // Initialize the database
        sourceApprovEpRepository.saveAndFlush(sourceApprovEp);

        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();

        // Update the sourceApprovEp using partial update
        SourceApprovEp partialUpdatedSourceApprovEp = new SourceApprovEp();
        partialUpdatedSourceApprovEp.setId(sourceApprovEp.getId());

        restSourceApprovEpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceApprovEp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourceApprovEp))
            )
            .andExpect(status().isOk());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);
        SourceApprovEp testSourceApprovEp = sourceApprovEpList.get(sourceApprovEpList.size() - 1);
        assertThat(testSourceApprovEp.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateSourceApprovEpWithPatch() throws Exception {
        // Initialize the database
        sourceApprovEpRepository.saveAndFlush(sourceApprovEp);

        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();

        // Update the sourceApprovEp using partial update
        SourceApprovEp partialUpdatedSourceApprovEp = new SourceApprovEp();
        partialUpdatedSourceApprovEp.setId(sourceApprovEp.getId());

        partialUpdatedSourceApprovEp.libelle(UPDATED_LIBELLE);

        restSourceApprovEpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceApprovEp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourceApprovEp))
            )
            .andExpect(status().isOk());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);
        SourceApprovEp testSourceApprovEp = sourceApprovEpList.get(sourceApprovEpList.size() - 1);
        assertThat(testSourceApprovEp.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingSourceApprovEp() throws Exception {
        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();
        sourceApprovEp.setId(count.incrementAndGet());

        // Create the SourceApprovEp
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceApprovEpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourceApprovEpDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(0)).save(sourceApprovEp);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSourceApprovEp() throws Exception {
        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();
        sourceApprovEp.setId(count.incrementAndGet());

        // Create the SourceApprovEp
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApprovEpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(0)).save(sourceApprovEp);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSourceApprovEp() throws Exception {
        int databaseSizeBeforeUpdate = sourceApprovEpRepository.findAll().size();
        sourceApprovEp.setId(count.incrementAndGet());

        // Create the SourceApprovEp
        SourceApprovEpDTO sourceApprovEpDTO = sourceApprovEpMapper.toDto(sourceApprovEp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceApprovEpMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceApprovEpDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceApprovEp in the database
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(0)).save(sourceApprovEp);
    }

    @Test
    @Transactional
    void deleteSourceApprovEp() throws Exception {
        // Initialize the database
        sourceApprovEpRepository.saveAndFlush(sourceApprovEp);

        int databaseSizeBeforeDelete = sourceApprovEpRepository.findAll().size();

        // Delete the sourceApprovEp
        restSourceApprovEpMockMvc
            .perform(delete(ENTITY_API_URL_ID, sourceApprovEp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourceApprovEp> sourceApprovEpList = sourceApprovEpRepository.findAll();
        assertThat(sourceApprovEpList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SourceApprovEp in Elasticsearch
        verify(mockSourceApprovEpSearchRepository, times(1)).deleteById(sourceApprovEp.getId());
    }

    @Test
    @Transactional
    void searchSourceApprovEp() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sourceApprovEpRepository.saveAndFlush(sourceApprovEp);
        when(mockSourceApprovEpSearchRepository.search(queryStringQuery("id:" + sourceApprovEp.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sourceApprovEp), PageRequest.of(0, 1), 1));

        // Search the sourceApprovEp
        restSourceApprovEpMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sourceApprovEp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceApprovEp.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
