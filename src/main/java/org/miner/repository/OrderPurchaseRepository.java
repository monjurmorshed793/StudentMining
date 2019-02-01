package org.miner.repository;

import org.miner.domain.OrderPurchase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderPurchase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderPurchaseRepository extends JpaRepository<OrderPurchase, Long> {

}
