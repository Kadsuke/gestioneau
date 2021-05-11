package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.CentreRegroupement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CentreRegroupement} entity.
 */
public interface CentreRegroupementSearchRepository extends ElasticsearchRepository<CentreRegroupement, Long> {}
