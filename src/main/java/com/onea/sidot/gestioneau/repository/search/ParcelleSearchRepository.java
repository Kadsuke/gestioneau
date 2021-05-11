package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Parcelle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Parcelle} entity.
 */
public interface ParcelleSearchRepository extends ElasticsearchRepository<Parcelle, Long> {}
