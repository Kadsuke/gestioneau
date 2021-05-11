package com.onea.sidot.gestioneau.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PrefabricantSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PrefabricantSearchRepositoryMockConfiguration {

    @MockBean
    private PrefabricantSearchRepository mockPrefabricantSearchRepository;
}
