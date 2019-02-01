package org.miner.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of OrderPurchaseSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class OrderPurchaseSearchRepositoryMockConfiguration {

    @MockBean
    private OrderPurchaseSearchRepository mockOrderPurchaseSearchRepository;

}
