package org.miner.web.rest;
import org.miner.domain.OrderPurchase;
import org.miner.service.OrderPurchaseService;
import org.miner.web.rest.errors.BadRequestAlertException;
import org.miner.web.rest.util.HeaderUtil;
import org.miner.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing OrderPurchase.
 */
@RestController
@RequestMapping("/api")
public class OrderPurchaseResource {

    private final Logger log = LoggerFactory.getLogger(OrderPurchaseResource.class);

    private static final String ENTITY_NAME = "orderPurchase";

    private final OrderPurchaseService orderPurchaseService;

    public OrderPurchaseResource(OrderPurchaseService orderPurchaseService) {
        this.orderPurchaseService = orderPurchaseService;
    }

    /**
     * POST  /order-purchases : Create a new orderPurchase.
     *
     * @param orderPurchase the orderPurchase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderPurchase, or with status 400 (Bad Request) if the orderPurchase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-purchases")
    public ResponseEntity<OrderPurchase> createOrderPurchase(@RequestBody OrderPurchase orderPurchase) throws URISyntaxException {
        log.debug("REST request to save OrderPurchase : {}", orderPurchase);
        if (orderPurchase.getId() != null) {
            throw new BadRequestAlertException("A new orderPurchase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderPurchase result = orderPurchaseService.save(orderPurchase);
        return ResponseEntity.created(new URI("/api/order-purchases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-purchases : Updates an existing orderPurchase.
     *
     * @param orderPurchase the orderPurchase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderPurchase,
     * or with status 400 (Bad Request) if the orderPurchase is not valid,
     * or with status 500 (Internal Server Error) if the orderPurchase couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-purchases")
    public ResponseEntity<OrderPurchase> updateOrderPurchase(@RequestBody OrderPurchase orderPurchase) throws URISyntaxException {
        log.debug("REST request to update OrderPurchase : {}", orderPurchase);
        if (orderPurchase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderPurchase result = orderPurchaseService.save(orderPurchase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderPurchase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-purchases : get all the orderPurchases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderPurchases in body
     */
    @GetMapping("/order-purchases")
    public ResponseEntity<List<OrderPurchase>> getAllOrderPurchases(Pageable pageable) {
        log.debug("REST request to get a page of OrderPurchases");
        Page<OrderPurchase> page = orderPurchaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-purchases");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /order-purchases/:id : get the "id" orderPurchase.
     *
     * @param id the id of the orderPurchase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderPurchase, or with status 404 (Not Found)
     */
    @GetMapping("/order-purchases/{id}")
    public ResponseEntity<OrderPurchase> getOrderPurchase(@PathVariable Long id) {
        log.debug("REST request to get OrderPurchase : {}", id);
        Optional<OrderPurchase> orderPurchase = orderPurchaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderPurchase);
    }

    /**
     * DELETE  /order-purchases/:id : delete the "id" orderPurchase.
     *
     * @param id the id of the orderPurchase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-purchases/{id}")
    public ResponseEntity<Void> deleteOrderPurchase(@PathVariable Long id) {
        log.debug("REST request to delete OrderPurchase : {}", id);
        orderPurchaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-purchases?query=:query : search for the orderPurchase corresponding
     * to the query.
     *
     * @param query the query of the orderPurchase search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/order-purchases")
    public ResponseEntity<List<OrderPurchase>> searchOrderPurchases(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrderPurchases for query {}", query);
        Page<OrderPurchase> page = orderPurchaseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-purchases");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
