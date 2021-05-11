package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Section;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Section} entity.
 */
public interface SectionSearchRepository extends ElasticsearchRepository<Section, Long> {}
