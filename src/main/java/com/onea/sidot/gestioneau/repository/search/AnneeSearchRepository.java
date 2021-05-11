package com.onea.sidot.gestioneau.repository.search;

import com.onea.sidot.gestioneau.domain.Annee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Annee} entity.
 */
public interface AnneeSearchRepository extends ElasticsearchRepository<Annee, Long> {}
