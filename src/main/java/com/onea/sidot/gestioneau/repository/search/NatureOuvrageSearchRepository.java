package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.NatureOuvrage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link NatureOuvrage} entity.
 */
public interface NatureOuvrageSearchRepository extends ElasticsearchRepository<NatureOuvrage, Long> {}
