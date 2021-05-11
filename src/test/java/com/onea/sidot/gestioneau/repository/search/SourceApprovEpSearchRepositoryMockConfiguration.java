package com.onea.sidot.gestioneau.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SourceApprovEpSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SourceApprovEpSearchRepositoryMockConfiguration {

    @MockBean
    private SourceApprovEpSearchRepository mockSourceApprovEpSearchRepository;
}
