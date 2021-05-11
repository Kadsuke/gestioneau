package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Centre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Centre} entity.
 */
public interface CentreSearchRepository extends ElasticsearchRepository<Centre, Long> {}
