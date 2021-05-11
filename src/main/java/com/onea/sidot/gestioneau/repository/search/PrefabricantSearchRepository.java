package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Prefabricant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Prefabricant} entity.
 */
public interface PrefabricantSearchRepository extends ElasticsearchRepository<Prefabricant, Long> {}
