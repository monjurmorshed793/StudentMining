package org.miner.repository.search;

import org.miner.domain.OrderPurchase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrderPurchase entity.
 */
public interface OrderPurchaseSearchRepository extends ElasticsearchRepository<OrderPurchase, Long> {
}
