package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Commune;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Commune} entity.
 */
public interface CommuneSearchRepository extends ElasticsearchRepository<Commune, Long> {}
