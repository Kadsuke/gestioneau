package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.FicheSuiviOuvrage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FicheSuiviOuvrage} entity.
 */
public interface FicheSuiviOuvrageSearchRepository extends ElasticsearchRepository<FicheSuiviOuvrage, Long> {}
