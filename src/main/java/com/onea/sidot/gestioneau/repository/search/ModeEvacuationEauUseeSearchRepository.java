package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.ModeEvacuationEauUsee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ModeEvacuationEauUsee} entity.
 */
public interface ModeEvacuationEauUseeSearchRepository extends ElasticsearchRepository<ModeEvacuationEauUsee, Long> {}
