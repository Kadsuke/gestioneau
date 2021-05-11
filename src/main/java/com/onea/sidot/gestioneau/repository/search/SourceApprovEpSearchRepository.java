package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.SourceApprovEp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SourceApprovEp} entity.
 */
public interface SourceApprovEpSearchRepository extends ElasticsearchRepository<SourceApprovEp, Long> {}
