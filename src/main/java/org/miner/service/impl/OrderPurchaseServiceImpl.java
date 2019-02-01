package org.miner.service.impl;

import org.miner.service.OrderPurchaseService;
import org.miner.domain.OrderPurchase;
import org.miner.repository.OrderPurchaseRepository;
import org.miner.repository.search.OrderPurchaseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrderPurchase.
 */
@Service
@Transactional
public class OrderPurchaseServiceImpl implements OrderPurchaseService {

    private final Logger log = LoggerFactory.getLogger(OrderPurchaseServiceImpl.class);

    private final OrderPurchaseRepository orderPurchaseRepository;

    private final OrderPurchaseSearchRepository orderPurchaseSearchRepository;

    public OrderPurchaseServiceImpl(OrderPurchaseRepository orderPurchaseRepository, OrderPurchaseSearchRepository orderPurchaseSearchRepository) {
        this.orderPurchaseRepository = orderPurchaseRepository;
        this.orderPurchaseSearchRepository = orderPurchaseSearchRepository;
    }

    /**
     * Save a orderPurchase.
     *
     * @param orderPurchase the entity to save
     * @return the persisted entity
     */
    @Override
    public OrderPurchase save(OrderPurchase orderPurchase) {
        log.debug("Request to save OrderPurchase : {}", orderPurchase);
        OrderPurchase result = orderPurchaseRepository.save(orderPurchase);
        orderPurchaseSearchRepository.save(result);
        return result;
    }

    @Override
    public List<OrderPurchase> save(List<OrderPurchase> orderPurchases) {
        log.debug("Request to save a list of order purchases");
        List<OrderPurchase> results = orderPurchaseRepository.saveAll(orderPurchases);
        orderPurchaseSearchRepository.saveAll(results);
        return results;
    }

    /**
     * Get all the orderPurchases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderPurchase> findAll(Pageable pageable) {
        log.debug("Request to get all OrderPurchases");
        return orderPurchaseRepository.findAll(pageable);
    }


    /**
     * Get one orderPurchase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderPurchase> findOne(Long id) {
        log.debug("Request to get OrderPurchase : {}", id);
        return orderPurchaseRepository.findById(id);
    }

    /**
     * Delete the orderPurchase by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderPurchase : {}", id);        orderPurchaseRepository.deleteById(id);
        orderPurchaseSearchRepository.deleteById(id);
    }

    /**
     * Search for the orderPurchase corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderPurchase> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderPurchases for query {}", query);
        return orderPurchaseSearchRepository.search(queryStringQuery(query), pageable);    }
}
