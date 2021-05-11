package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.DirectionRegionale;
import com.onea.sidot.gestioneau.repository.DirectionRegionaleRepository;
import com.onea.sidot.gestioneau.repository.search.DirectionRegionaleSearchRepository;
import com.onea.sidot.gestioneau.service.dto.DirectionRegionaleDTO;
import com.onea.sidot.gestioneau.service.mapper.DirectionRegionaleMapper;
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
 * Integration tests for the {@link DirectionRegionaleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DirectionRegionaleResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/direction-regionales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/direction-regionales";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirectionRegionaleRepository directionRegionaleRepository;

    @Autowired
    private DirectionRegionaleMapper directionRegionaleMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.DirectionRegionaleSearchRepositoryMockConfiguration
     */
    @Autowired
    private DirectionRegionaleSearchRepository mockDirectionRegionaleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirectionRegionaleMockMvc;

    private DirectionRegionale directionRegionale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectionRegionale createEntity(EntityManager em) {
        DirectionRegionale directionRegionale = new DirectionRegionale()
            .libelle(DEFAULT_LIBELLE)
            .responsable(DEFAULT_RESPONSABLE)
            .contact(DEFAULT_CONTACT);
        return directionRegionale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectionRegionale createUpdatedEntity(EntityManager em) {
        DirectionRegionale directionRegionale = new DirectionRegionale()
            .libelle(UPDATED_LIBELLE)
            .responsable(UPDATED_RESPONSABLE)
            .contact(UPDATED_CONTACT);
        return directionRegionale;
    }

    @BeforeEach
    public void initTest() {
        directionRegionale = createEntity(em);
    }

    @Test
    @Transactional
    void createDirectionRegionale() throws Exception {
        int databaseSizeBeforeCreate = directionRegionaleRepository.findAll().size();
        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);
        restDirectionRegionaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeCreate + 1);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(DEFAULT_CONTACT);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(1)).save(testDirectionRegionale);
    }

    @Test
    @Transactional
    void createDirectionRegionaleWithExistingId() throws Exception {
        // Create the DirectionRegionale with an existing ID
        directionRegionale.setId(1L);
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        int databaseSizeBeforeCreate = directionRegionaleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectionRegionaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeCreate);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(0)).save(directionRegionale);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRegionaleRepository.findAll().size();
        // set the field null
        directionRegionale.setLibelle(null);

        // Create the DirectionRegionale, which fails.
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        restDirectionRegionaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRegionaleRepository.findAll().size();
        // set the field null
        directionRegionale.setResponsable(null);

        // Create the DirectionRegionale, which fails.
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        restDirectionRegionaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRegionaleRepository.findAll().size();
        // set the field null
        directionRegionale.setContact(null);

        // Create the DirectionRegionale, which fails.
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        restDirectionRegionaleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDirectionRegionales() throws Exception {
        // Initialize the database
        directionRegionaleRepository.saveAndFlush(directionRegionale);

        // Get all the directionRegionaleList
        restDirectionRegionaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directionRegionale.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @Test
    @Transactional
    void getDirectionRegionale() throws Exception {
        // Initialize the database
        directionRegionaleRepository.saveAndFlush(directionRegionale);

        // Get the directionRegionale
        restDirectionRegionaleMockMvc
            .perform(get(ENTITY_API_URL_ID, directionRegionale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directionRegionale.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingDirectionRegionale() throws Exception {
        // Get the directionRegionale
        restDirectionRegionaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDirectionRegionale() throws Exception {
        // Initialize the database
        directionRegionaleRepository.saveAndFlush(directionRegionale);

        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();

        // Update the directionRegionale
        DirectionRegionale updatedDirectionRegionale = directionRegionaleRepository.findById(directionRegionale.getId()).get();
        // Disconnect from session so that the updates on updatedDirectionRegionale are not directly saved in db
        em.detach(updatedDirectionRegionale);
        updatedDirectionRegionale.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(updatedDirectionRegionale);

        restDirectionRegionaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directionRegionaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isOk());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(UPDATED_CONTACT);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository).save(testDirectionRegionale);
    }

    @Test
    @Transactional
    void putNonExistingDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectionRegionaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directionRegionaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(0)).save(directionRegionale);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionRegionaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(0)).save(directionRegionale);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionRegionaleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(0)).save(directionRegionale);
    }

    @Test
    @Transactional
    void partialUpdateDirectionRegionaleWithPatch() throws Exception {
        // Initialize the database
        directionRegionaleRepository.saveAndFlush(directionRegionale);

        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();

        // Update the directionRegionale using partial update
        DirectionRegionale partialUpdatedDirectionRegionale = new DirectionRegionale();
        partialUpdatedDirectionRegionale.setId(directionRegionale.getId());

        restDirectionRegionaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectionRegionale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectionRegionale))
            )
            .andExpect(status().isOk());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdateDirectionRegionaleWithPatch() throws Exception {
        // Initialize the database
        directionRegionaleRepository.saveAndFlush(directionRegionale);

        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();

        // Update the directionRegionale using partial update
        DirectionRegionale partialUpdatedDirectionRegionale = new DirectionRegionale();
        partialUpdatedDirectionRegionale.setId(directionRegionale.getId());

        partialUpdatedDirectionRegionale.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        restDirectionRegionaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectionRegionale.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectionRegionale))
            )
            .andExpect(status().isOk());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectionRegionaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directionRegionaleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(0)).save(directionRegionale);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionRegionaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(0)).save(directionRegionale);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectionRegionaleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(0)).save(directionRegionale);
    }

    @Test
    @Transactional
    void deleteDirectionRegionale() throws Exception {
        // Initialize the database
        directionRegionaleRepository.saveAndFlush(directionRegionale);

        int databaseSizeBeforeDelete = directionRegionaleRepository.findAll().size();

        // Delete the directionRegionale
        restDirectionRegionaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, directionRegionale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DirectionRegionale in Elasticsearch
        verify(mockDirectionRegionaleSearchRepository, times(1)).deleteById(directionRegionale.getId());
    }

    @Test
    @Transactional
    void searchDirectionRegionale() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        directionRegionaleRepository.saveAndFlush(directionRegionale);
        when(mockDirectionRegionaleSearchRepository.search(queryStringQuery("id:" + directionRegionale.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(directionRegionale), PageRequest.of(0, 1), 1));

        // Search the directionRegionale
        restDirectionRegionaleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + directionRegionale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directionRegionale.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }
}
