package com.onea.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.sidot.gestioneau.IntegrationTest;
import com.onea.sidot.gestioneau.domain.Secteur;
import com.onea.sidot.gestioneau.repository.SecteurRepository;
import com.onea.sidot.gestioneau.repository.search.SecteurSearchRepository;
import com.onea.sidot.gestioneau.service.dto.SecteurDTO;
import com.onea.sidot.gestioneau.service.mapper.SecteurMapper;
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
 * Integration tests for the {@link SecteurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SecteurResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/secteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/secteurs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecteurRepository secteurRepository;

    @Autowired
    private SecteurMapper secteurMapper;

    /**
     * This repository is mocked in the com.onea.sidot.gestioneau.repository.search test package.
     *
     * @see com.onea.sidot.gestioneau.repository.search.SecteurSearchRepositoryMockConfiguration
     */
    @Autowired
    private SecteurSearchRepository mockSecteurSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecteurMockMvc;

    private Secteur secteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Secteur createEntity(EntityManager em) {
        Secteur secteur = new Secteur().libelle(DEFAULT_LIBELLE);
        return secteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Secteur createUpdatedEntity(EntityManager em) {
        Secteur secteur = new Secteur().libelle(UPDATED_LIBELLE);
        return secteur;
    }

    @BeforeEach
    public void initTest() {
        secteur = createEntity(em);
    }

    @Test
    @Transactional
    void createSecteur() throws Exception {
        int databaseSizeBeforeCreate = secteurRepository.findAll().size();
        // Create the Secteur
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);
        restSecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secteurDTO)))
            .andExpect(status().isCreated());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeCreate + 1);
        Secteur testSecteur = secteurList.get(secteurList.size() - 1);
        assertThat(testSecteur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(1)).save(testSecteur);
    }

    @Test
    @Transactional
    void createSecteurWithExistingId() throws Exception {
        // Create the Secteur with an existing ID
        secteur.setId(1L);
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        int databaseSizeBeforeCreate = secteurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeCreate);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(0)).save(secteur);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = secteurRepository.findAll().size();
        // set the field null
        secteur.setLibelle(null);

        // Create the Secteur, which fails.
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        restSecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secteurDTO)))
            .andExpect(status().isBadRequest());

        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSecteurs() throws Exception {
        // Initialize the database
        secteurRepository.saveAndFlush(secteur);

        // Get all the secteurList
        restSecteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(secteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getSecteur() throws Exception {
        // Initialize the database
        secteurRepository.saveAndFlush(secteur);

        // Get the secteur
        restSecteurMockMvc
            .perform(get(ENTITY_API_URL_ID, secteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(secteur.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingSecteur() throws Exception {
        // Get the secteur
        restSecteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecteur() throws Exception {
        // Initialize the database
        secteurRepository.saveAndFlush(secteur);

        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();

        // Update the secteur
        Secteur updatedSecteur = secteurRepository.findById(secteur.getId()).get();
        // Disconnect from session so that the updates on updatedSecteur are not directly saved in db
        em.detach(updatedSecteur);
        updatedSecteur.libelle(UPDATED_LIBELLE);
        SecteurDTO secteurDTO = secteurMapper.toDto(updatedSecteur);

        restSecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, secteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(secteurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);
        Secteur testSecteur = secteurList.get(secteurList.size() - 1);
        assertThat(testSecteur.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository).save(testSecteur);
    }

    @Test
    @Transactional
    void putNonExistingSecteur() throws Exception {
        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();
        secteur.setId(count.incrementAndGet());

        // Create the Secteur
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, secteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(secteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(0)).save(secteur);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecteur() throws Exception {
        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();
        secteur.setId(count.incrementAndGet());

        // Create the Secteur
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(secteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(0)).save(secteur);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecteur() throws Exception {
        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();
        secteur.setId(count.incrementAndGet());

        // Create the Secteur
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(secteurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(0)).save(secteur);
    }

    @Test
    @Transactional
    void partialUpdateSecteurWithPatch() throws Exception {
        // Initialize the database
        secteurRepository.saveAndFlush(secteur);

        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();

        // Update the secteur using partial update
        Secteur partialUpdatedSecteur = new Secteur();
        partialUpdatedSecteur.setId(secteur.getId());

        restSecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecteur))
            )
            .andExpect(status().isOk());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);
        Secteur testSecteur = secteurList.get(secteurList.size() - 1);
        assertThat(testSecteur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateSecteurWithPatch() throws Exception {
        // Initialize the database
        secteurRepository.saveAndFlush(secteur);

        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();

        // Update the secteur using partial update
        Secteur partialUpdatedSecteur = new Secteur();
        partialUpdatedSecteur.setId(secteur.getId());

        partialUpdatedSecteur.libelle(UPDATED_LIBELLE);

        restSecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecteur))
            )
            .andExpect(status().isOk());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);
        Secteur testSecteur = secteurList.get(secteurList.size() - 1);
        assertThat(testSecteur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingSecteur() throws Exception {
        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();
        secteur.setId(count.incrementAndGet());

        // Create the Secteur
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, secteurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(secteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(0)).save(secteur);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecteur() throws Exception {
        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();
        secteur.setId(count.incrementAndGet());

        // Create the Secteur
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(secteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(0)).save(secteur);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecteur() throws Exception {
        int databaseSizeBeforeUpdate = secteurRepository.findAll().size();
        secteur.setId(count.incrementAndGet());

        // Create the Secteur
        SecteurDTO secteurDTO = secteurMapper.toDto(secteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecteurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(secteurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Secteur in the database
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(0)).save(secteur);
    }

    @Test
    @Transactional
    void deleteSecteur() throws Exception {
        // Initialize the database
        secteurRepository.saveAndFlush(secteur);

        int databaseSizeBeforeDelete = secteurRepository.findAll().size();

        // Delete the secteur
        restSecteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, secteur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Secteur> secteurList = secteurRepository.findAll();
        assertThat(secteurList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Secteur in Elasticsearch
        verify(mockSecteurSearchRepository, times(1)).deleteById(secteur.getId());
    }

    @Test
    @Transactional
    void searchSecteur() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        secteurRepository.saveAndFlush(secteur);
        when(mockSecteurSearchRepository.search(queryStringQuery("id:" + secteur.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(secteur), PageRequest.of(0, 1), 1));

        // Search the secteur
        restSecteurMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + secteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(secteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
