package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Prevision;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Prevision} entity.
 */
public interface PrevisionSearchRepository extends ElasticsearchRepository<Prevision, Long> {}
