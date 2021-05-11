package com.onea.sidot.gestioneau.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link LotSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LotSearchRepositoryMockConfiguration {

    @MockBean
    private LotSearchRepository mockLotSearchRepository;
}
