package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.Macon;
import com.onea.sidot.gestioneau.repository.MaconRepository;
import com.onea.sidot.gestioneau.repository.search.MaconSearchRepository;
import com.onea.sidot.gestioneau.service.dto.MaconDTO;
import com.onea.sidot.gestioneau.service.mapper.MaconMapper;
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
 * Integration tests for the {@link MaconResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MaconResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/macons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/macons";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaconRepository maconRepository;

    @Autowired
    private MaconMapper maconMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.MaconSearchRepositoryMockConfiguration
     */
    @Autowired
    private MaconSearchRepository mockMaconSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaconMockMvc;

    private Macon macon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Macon createEntity(EntityManager em) {
        Macon macon = new Macon().libelle(DEFAULT_LIBELLE);
        return macon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Macon createUpdatedEntity(EntityManager em) {
        Macon macon = new Macon().libelle(UPDATED_LIBELLE);
        return macon;
    }

    @BeforeEach
    public void initTest() {
        macon = createEntity(em);
    }

    @Test
    @Transactional
    void createMacon() throws Exception {
        int databaseSizeBeforeCreate = maconRepository.findAll().size();
        // Create the Macon
        MaconDTO maconDTO = maconMapper.toDto(macon);
        restMaconMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maconDTO)))
            .andExpect(status().isCreated());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeCreate + 1);
        Macon testMacon = maconList.get(maconList.size() - 1);
        assertThat(testMacon.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(1)).save(testMacon);
    }

    @Test
    @Transactional
    void createMaconWithExistingId() throws Exception {
        // Create the Macon with an existing ID
        macon.setId(1L);
        MaconDTO maconDTO = maconMapper.toDto(macon);

        int databaseSizeBeforeCreate = maconRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaconMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maconDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeCreate);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(0)).save(macon);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = maconRepository.findAll().size();
        // set the field null
        macon.setLibelle(null);

        // Create the Macon, which fails.
        MaconDTO maconDTO = maconMapper.toDto(macon);

        restMaconMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maconDTO)))
            .andExpect(status().isBadRequest());

        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMacons() throws Exception {
        // Initialize the database
        maconRepository.saveAndFlush(macon);

        // Get all the maconList
        restMaconMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(macon.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getMacon() throws Exception {
        // Initialize the database
        maconRepository.saveAndFlush(macon);

        // Get the macon
        restMaconMockMvc
            .perform(get(ENTITY_API_URL_ID, macon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(macon.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingMacon() throws Exception {
        // Get the macon
        restMaconMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMacon() throws Exception {
        // Initialize the database
        maconRepository.saveAndFlush(macon);

        int databaseSizeBeforeUpdate = maconRepository.findAll().size();

        // Update the macon
        Macon updatedMacon = maconRepository.findById(macon.getId()).get();
        // Disconnect from session so that the updates on updatedMacon are not directly saved in db
        em.detach(updatedMacon);
        updatedMacon.libelle(UPDATED_LIBELLE);
        MaconDTO maconDTO = maconMapper.toDto(updatedMacon);

        restMaconMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maconDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maconDTO))
            )
            .andExpect(status().isOk());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);
        Macon testMacon = maconList.get(maconList.size() - 1);
        assertThat(testMacon.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository).save(testMacon);
    }

    @Test
    @Transactional
    void putNonExistingMacon() throws Exception {
        int databaseSizeBeforeUpdate = maconRepository.findAll().size();
        macon.setId(count.incrementAndGet());

        // Create the Macon
        MaconDTO maconDTO = maconMapper.toDto(macon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaconMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maconDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maconDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(0)).save(macon);
    }

    @Test
    @Transactional
    void putWithIdMismatchMacon() throws Exception {
        int databaseSizeBeforeUpdate = maconRepository.findAll().size();
        macon.setId(count.incrementAndGet());

        // Create the Macon
        MaconDTO maconDTO = maconMapper.toDto(macon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaconMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maconDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(0)).save(macon);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMacon() throws Exception {
        int databaseSizeBeforeUpdate = maconRepository.findAll().size();
        macon.setId(count.incrementAndGet());

        // Create the Macon
        MaconDTO maconDTO = maconMapper.toDto(macon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaconMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(maconDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(0)).save(macon);
    }

    @Test
    @Transactional
    void partialUpdateMaconWithPatch() throws Exception {
        // Initialize the database
        maconRepository.saveAndFlush(macon);

        int databaseSizeBeforeUpdate = maconRepository.findAll().size();

        // Update the macon using partial update
        Macon partialUpdatedMacon = new Macon();
        partialUpdatedMacon.setId(macon.getId());

        partialUpdatedMacon.libelle(UPDATED_LIBELLE);

        restMaconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMacon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMacon))
            )
            .andExpect(status().isOk());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);
        Macon testMacon = maconList.get(maconList.size() - 1);
        assertThat(testMacon.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateMaconWithPatch() throws Exception {
        // Initialize the database
        maconRepository.saveAndFlush(macon);

        int databaseSizeBeforeUpdate = maconRepository.findAll().size();

        // Update the macon using partial update
        Macon partialUpdatedMacon = new Macon();
        partialUpdatedMacon.setId(macon.getId());

        partialUpdatedMacon.libelle(UPDATED_LIBELLE);

        restMaconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMacon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMacon))
            )
            .andExpect(status().isOk());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);
        Macon testMacon = maconList.get(maconList.size() - 1);
        assertThat(testMacon.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingMacon() throws Exception {
        int databaseSizeBeforeUpdate = maconRepository.findAll().size();
        macon.setId(count.incrementAndGet());

        // Create the Macon
        MaconDTO maconDTO = maconMapper.toDto(macon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maconDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maconDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(0)).save(macon);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMacon() throws Exception {
        int databaseSizeBeforeUpdate = maconRepository.findAll().size();
        macon.setId(count.incrementAndGet());

        // Create the Macon
        MaconDTO maconDTO = maconMapper.toDto(macon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaconMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maconDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(0)).save(macon);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMacon() throws Exception {
        int databaseSizeBeforeUpdate = maconRepository.findAll().size();
        macon.setId(count.incrementAndGet());

        // Create the Macon
        MaconDTO maconDTO = maconMapper.toDto(macon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaconMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(maconDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Macon in the database
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(0)).save(macon);
    }

    @Test
    @Transactional
    void deleteMacon() throws Exception {
        // Initialize the database
        maconRepository.saveAndFlush(macon);

        int databaseSizeBeforeDelete = maconRepository.findAll().size();

        // Delete the macon
        restMaconMockMvc
            .perform(delete(ENTITY_API_URL_ID, macon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Macon> maconList = maconRepository.findAll();
        assertThat(maconList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Macon in Elasticsearch
        verify(mockMaconSearchRepository, times(1)).deleteById(macon.getId());
    }

    @Test
    @Transactional
    void searchMacon() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        maconRepository.saveAndFlush(macon);
        when(mockMaconSearchRepository.search(queryStringQuery("id:" + macon.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(macon), PageRequest.of(0, 1), 1));

        // Search the macon
        restMaconMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + macon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(macon.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
