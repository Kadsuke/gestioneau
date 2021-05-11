package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Lot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Lot} entity.
 */
public interface LotSearchRepository extends ElasticsearchRepository<Lot, Long> {}
