package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.DirectionRegionale;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DirectionRegionale} entity.
 */
public interface DirectionRegionaleSearchRepository extends ElasticsearchRepository<DirectionRegionale, Long> {}
