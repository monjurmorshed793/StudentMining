package org.miner.service;

import org.miner.domain.OrderPurchase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing OrderPurchase.
 */
public interface OrderPurchaseService {

    /**
     * Save a orderPurchase.
     *
     * @param orderPurchase the entity to save
     * @return the persisted entity
     */
    OrderPurchase save(OrderPurchase orderPurchase);

    List<OrderPurchase> save(List<OrderPurchase> orderPurchases);

    /**
     * Get all the orderPurchases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderPurchase> findAll(Pageable pageable);


    /**
     * Get the "id" orderPurchase.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrderPurchase> findOne(Long id);

    /**
     * Delete the "id" orderPurchase.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderPurchase corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrderPurchase> search(String query, Pageable pageable);
}
