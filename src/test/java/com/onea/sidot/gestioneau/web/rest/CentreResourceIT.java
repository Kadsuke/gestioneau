package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.Centre;
import com.onea.sidot.gestioneau.repository.CentreRepository;
import com.onea.sidot.gestioneau.repository.search.CentreSearchRepository;
import com.onea.sidot.gestioneau.service.dto.CentreDTO;
import com.onea.sidot.gestioneau.service.mapper.CentreMapper;
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
 * Integration tests for the {@link CentreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CentreResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/centres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/centres";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private CentreMapper centreMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.CentreSearchRepositoryMockConfiguration
     */
    @Autowired
    private CentreSearchRepository mockCentreSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCentreMockMvc;

    private Centre centre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centre createEntity(EntityManager em) {
        Centre centre = new Centre().libelle(DEFAULT_LIBELLE).responsable(DEFAULT_RESPONSABLE).contact(DEFAULT_CONTACT);
        return centre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centre createUpdatedEntity(EntityManager em) {
        Centre centre = new Centre().libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        return centre;
    }

    @BeforeEach
    public void initTest() {
        centre = createEntity(em);
    }

    @Test
    @Transactional
    void createCentre() throws Exception {
        int databaseSizeBeforeCreate = centreRepository.findAll().size();
        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);
        restCentreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(centreDTO)))
            .andExpect(status().isCreated());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeCreate + 1);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCentre.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testCentre.getContact()).isEqualTo(DEFAULT_CONTACT);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(1)).save(testCentre);
    }

    @Test
    @Transactional
    void createCentreWithExistingId() throws Exception {
        // Create the Centre with an existing ID
        centre.setId(1L);
        CentreDTO centreDTO = centreMapper.toDto(centre);

        int databaseSizeBeforeCreate = centreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCentreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(centreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeCreate);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(0)).save(centre);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRepository.findAll().size();
        // set the field null
        centre.setLibelle(null);

        // Create the Centre, which fails.
        CentreDTO centreDTO = centreMapper.toDto(centre);

        restCentreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(centreDTO)))
            .andExpect(status().isBadRequest());

        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRepository.findAll().size();
        // set the field null
        centre.setResponsable(null);

        // Create the Centre, which fails.
        CentreDTO centreDTO = centreMapper.toDto(centre);

        restCentreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(centreDTO)))
            .andExpect(status().isBadRequest());

        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRepository.findAll().size();
        // set the field null
        centre.setContact(null);

        // Create the Centre, which fails.
        CentreDTO centreDTO = centreMapper.toDto(centre);

        restCentreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(centreDTO)))
            .andExpect(status().isBadRequest());

        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCentres() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList
        restCentreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @Test
    @Transactional
    void getCentre() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get the centre
        restCentreMockMvc
            .perform(get(ENTITY_API_URL_ID, centre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(centre.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingCentre() throws Exception {
        // Get the centre
        restCentreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCentre() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        int databaseSizeBeforeUpdate = centreRepository.findAll().size();

        // Update the centre
        Centre updatedCentre = centreRepository.findById(centre.getId()).get();
        // Disconnect from session so that the updates on updatedCentre are not directly saved in db
        em.detach(updatedCentre);
        updatedCentre.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        CentreDTO centreDTO = centreMapper.toDto(updatedCentre);

        restCentreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCentre.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testCentre.getContact()).isEqualTo(UPDATED_CONTACT);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository).save(testCentre);
    }

    @Test
    @Transactional
    void putNonExistingCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().size();
        centre.setId(count.incrementAndGet());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(0)).save(centre);
    }

    @Test
    @Transactional
    void putWithIdMismatchCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().size();
        centre.setId(count.incrementAndGet());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(centreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(0)).save(centre);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().size();
        centre.setId(count.incrementAndGet());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(centreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(0)).save(centre);
    }

    @Test
    @Transactional
    void partialUpdateCentreWithPatch() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        int databaseSizeBeforeUpdate = centreRepository.findAll().size();

        // Update the centre using partial update
        Centre partialUpdatedCentre = new Centre();
        partialUpdatedCentre.setId(centre.getId());

        restCentreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCentre))
            )
            .andExpect(status().isOk());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCentre.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testCentre.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdateCentreWithPatch() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        int databaseSizeBeforeUpdate = centreRepository.findAll().size();

        // Update the centre using partial update
        Centre partialUpdatedCentre = new Centre();
        partialUpdatedCentre.setId(centre.getId());

        partialUpdatedCentre.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        restCentreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCentre))
            )
            .andExpect(status().isOk());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCentre.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testCentre.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().size();
        centre.setId(count.incrementAndGet());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, centreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(centreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(0)).save(centre);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().size();
        centre.setId(count.incrementAndGet());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(centreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(0)).save(centre);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().size();
        centre.setId(count.incrementAndGet());

        // Create the Centre
        CentreDTO centreDTO = centreMapper.toDto(centre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(centreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(0)).save(centre);
    }

    @Test
    @Transactional
    void deleteCentre() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        int databaseSizeBeforeDelete = centreRepository.findAll().size();

        // Delete the centre
        restCentreMockMvc
            .perform(delete(ENTITY_API_URL_ID, centre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Centre in Elasticsearch
        verify(mockCentreSearchRepository, times(1)).deleteById(centre.getId());
    }

    @Test
    @Transactional
    void searchCentre() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        centreRepository.saveAndFlush(centre);
        when(mockCentreSearchRepository.search(queryStringQuery("id:" + centre.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(centre), PageRequest.of(0, 1), 1));

        // Search the centre
        restCentreMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + centre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }
}
