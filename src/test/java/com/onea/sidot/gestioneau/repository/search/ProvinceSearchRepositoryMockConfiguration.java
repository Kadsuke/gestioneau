package com.onea.sidot.gestioneau.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ProvinceSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProvinceSearchRepositoryMockConfiguration {

    @MockBean
    private ProvinceSearchRepository mockProvinceSearchRepository;
}
