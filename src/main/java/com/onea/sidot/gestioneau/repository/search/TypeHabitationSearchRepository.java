package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.TypeHabitation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeHabitation} entity.
 */
public interface TypeHabitationSearchRepository extends ElasticsearchRepository<TypeHabitation, Long> {}
