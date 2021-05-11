package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Macon;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Macon} entity.
 */
public interface MaconSearchRepository extends ElasticsearchRepository<Macon, Long> {}
