package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.TypeCommune;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeCommune} entity.
 */
public interface TypeCommuneSearchRepository extends ElasticsearchRepository<TypeCommune, Long> {}
