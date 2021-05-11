package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.Lot;
import com.onea.sidot.gestioneau.repository.LotRepository;
import com.onea.sidot.gestioneau.repository.search.LotSearchRepository;
import com.onea.sidot.gestioneau.service.dto.LotDTO;
import com.onea.sidot.gestioneau.service.mapper.LotMapper;
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
 * Integration tests for the {@link LotResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LotResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/lots";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private LotMapper lotMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.LotSearchRepositoryMockConfiguration
     */
    @Autowired
    private LotSearchRepository mockLotSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLotMockMvc;

    private Lot lot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lot createEntity(EntityManager em) {
        Lot lot = new Lot().libelle(DEFAULT_LIBELLE);
        return lot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lot createUpdatedEntity(EntityManager em) {
        Lot lot = new Lot().libelle(UPDATED_LIBELLE);
        return lot;
    }

    @BeforeEach
    public void initTest() {
        lot = createEntity(em);
    }

    @Test
    @Transactional
    void createLot() throws Exception {
        int databaseSizeBeforeCreate = lotRepository.findAll().size();
        // Create the Lot
        LotDTO lotDTO = lotMapper.toDto(lot);
        restLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lotDTO)))
            .andExpect(status().isCreated());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeCreate + 1);
        Lot testLot = lotList.get(lotList.size() - 1);
        assertThat(testLot.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(1)).save(testLot);
    }

    @Test
    @Transactional
    void createLotWithExistingId() throws Exception {
        // Create the Lot with an existing ID
        lot.setId(1L);
        LotDTO lotDTO = lotMapper.toDto(lot);

        int databaseSizeBeforeCreate = lotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeCreate);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(0)).save(lot);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = lotRepository.findAll().size();
        // set the field null
        lot.setLibelle(null);

        // Create the Lot, which fails.
        LotDTO lotDTO = lotMapper.toDto(lot);

        restLotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lotDTO)))
            .andExpect(status().isBadRequest());

        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLots() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        // Get all the lotList
        restLotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lot.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getLot() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        // Get the lot
        restLotMockMvc
            .perform(get(ENTITY_API_URL_ID, lot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lot.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingLot() throws Exception {
        // Get the lot
        restLotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLot() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        int databaseSizeBeforeUpdate = lotRepository.findAll().size();

        // Update the lot
        Lot updatedLot = lotRepository.findById(lot.getId()).get();
        // Disconnect from session so that the updates on updatedLot are not directly saved in db
        em.detach(updatedLot);
        updatedLot.libelle(UPDATED_LIBELLE);
        LotDTO lotDTO = lotMapper.toDto(updatedLot);

        restLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);
        Lot testLot = lotList.get(lotList.size() - 1);
        assertThat(testLot.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository).save(testLot);
    }

    @Test
    @Transactional
    void putNonExistingLot() throws Exception {
        int databaseSizeBeforeUpdate = lotRepository.findAll().size();
        lot.setId(count.incrementAndGet());

        // Create the Lot
        LotDTO lotDTO = lotMapper.toDto(lot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(0)).save(lot);
    }

    @Test
    @Transactional
    void putWithIdMismatchLot() throws Exception {
        int databaseSizeBeforeUpdate = lotRepository.findAll().size();
        lot.setId(count.incrementAndGet());

        // Create the Lot
        LotDTO lotDTO = lotMapper.toDto(lot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(0)).save(lot);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLot() throws Exception {
        int databaseSizeBeforeUpdate = lotRepository.findAll().size();
        lot.setId(count.incrementAndGet());

        // Create the Lot
        LotDTO lotDTO = lotMapper.toDto(lot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(0)).save(lot);
    }

    @Test
    @Transactional
    void partialUpdateLotWithPatch() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        int databaseSizeBeforeUpdate = lotRepository.findAll().size();

        // Update the lot using partial update
        Lot partialUpdatedLot = new Lot();
        partialUpdatedLot.setId(lot.getId());

        partialUpdatedLot.libelle(UPDATED_LIBELLE);

        restLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLot))
            )
            .andExpect(status().isOk());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);
        Lot testLot = lotList.get(lotList.size() - 1);
        assertThat(testLot.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateLotWithPatch() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        int databaseSizeBeforeUpdate = lotRepository.findAll().size();

        // Update the lot using partial update
        Lot partialUpdatedLot = new Lot();
        partialUpdatedLot.setId(lot.getId());

        partialUpdatedLot.libelle(UPDATED_LIBELLE);

        restLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLot))
            )
            .andExpect(status().isOk());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);
        Lot testLot = lotList.get(lotList.size() - 1);
        assertThat(testLot.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingLot() throws Exception {
        int databaseSizeBeforeUpdate = lotRepository.findAll().size();
        lot.setId(count.incrementAndGet());

        // Create the Lot
        LotDTO lotDTO = lotMapper.toDto(lot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(0)).save(lot);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLot() throws Exception {
        int databaseSizeBeforeUpdate = lotRepository.findAll().size();
        lot.setId(count.incrementAndGet());

        // Create the Lot
        LotDTO lotDTO = lotMapper.toDto(lot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(0)).save(lot);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLot() throws Exception {
        int databaseSizeBeforeUpdate = lotRepository.findAll().size();
        lot.setId(count.incrementAndGet());

        // Create the Lot
        LotDTO lotDTO = lotMapper.toDto(lot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lot in the database
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(0)).save(lot);
    }

    @Test
    @Transactional
    void deleteLot() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        int databaseSizeBeforeDelete = lotRepository.findAll().size();

        // Delete the lot
        restLotMockMvc.perform(delete(ENTITY_API_URL_ID, lot.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lot> lotList = lotRepository.findAll();
        assertThat(lotList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Lot in Elasticsearch
        verify(mockLotSearchRepository, times(1)).deleteById(lot.getId());
    }

    @Test
    @Transactional
    void searchLot() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        lotRepository.saveAndFlush(lot);
        when(mockLotSearchRepository.search(queryStringQuery("id:" + lot.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lot), PageRequest.of(0, 1), 1));

        // Search the lot
        restLotMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + lot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lot.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
