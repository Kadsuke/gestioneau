package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.ModeEvacExcreta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ModeEvacExcreta} entity.
 */
public interface ModeEvacExcretaSearchRepository extends ElasticsearchRepository<ModeEvacExcreta, Long> {}
