package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Province;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Province} entity.
 */
public interface ProvinceSearchRepository extends ElasticsearchRepository<Province, Long> {}
