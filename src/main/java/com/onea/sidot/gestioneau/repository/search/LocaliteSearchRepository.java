package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Localite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Localite} entity.
 */
public interface LocaliteSearchRepository extends ElasticsearchRepository<Localite, Long> {}
