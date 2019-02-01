package org.miner.web.rest;

import org.miner.StudentMiningApp;

import org.miner.domain.OrderPurchase;
import org.miner.repository.OrderPurchaseRepository;
import org.miner.repository.search.OrderPurchaseSearchRepository;
import org.miner.service.OrderPurchaseService;
import org.miner.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static org.miner.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderPurchaseResource REST controller.
 *
 * @see OrderPurchaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentMiningApp.class)
public class OrderPurchaseResourceIntTest {

    private static final LocalDate DEFAULT_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_ORDER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ORDER_AMOUNT = new BigDecimal(2);

    @Autowired
    private OrderPurchaseRepository orderPurchaseRepository;

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    /**
     * This repository is mocked in the org.miner.repository.search test package.
     *
     * @see org.miner.repository.search.OrderPurchaseSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrderPurchaseSearchRepository mockOrderPurchaseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restOrderPurchaseMockMvc;

    private OrderPurchase orderPurchase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderPurchaseResource orderPurchaseResource = new OrderPurchaseResource(orderPurchaseService);
        this.restOrderPurchaseMockMvc = MockMvcBuilders.standaloneSetup(orderPurchaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderPurchase createEntity(EntityManager em) {
        OrderPurchase orderPurchase = new OrderPurchase()
            .orderDate(DEFAULT_ORDER_DATE)
            .orderAmount(DEFAULT_ORDER_AMOUNT);
        return orderPurchase;
    }

    @Before
    public void initTest() {
        orderPurchase = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderPurchase() throws Exception {
        int databaseSizeBeforeCreate = orderPurchaseRepository.findAll().size();

        // Create the OrderPurchase
        restOrderPurchaseMockMvc.perform(post("/api/order-purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPurchase)))
            .andExpect(status().isCreated());

        // Validate the OrderPurchase in the database
        List<OrderPurchase> orderPurchaseList = orderPurchaseRepository.findAll();
        assertThat(orderPurchaseList).hasSize(databaseSizeBeforeCreate + 1);
        OrderPurchase testOrderPurchase = orderPurchaseList.get(orderPurchaseList.size() - 1);
        assertThat(testOrderPurchase.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrderPurchase.getOrderAmount()).isEqualTo(DEFAULT_ORDER_AMOUNT);

        // Validate the OrderPurchase in Elasticsearch
        verify(mockOrderPurchaseSearchRepository, times(1)).save(testOrderPurchase);
    }

    @Test
    @Transactional
    public void createOrderPurchaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderPurchaseRepository.findAll().size();

        // Create the OrderPurchase with an existing ID
        orderPurchase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderPurchaseMockMvc.perform(post("/api/order-purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPurchase)))
            .andExpect(status().isBadRequest());

        // Validate the OrderPurchase in the database
        List<OrderPurchase> orderPurchaseList = orderPurchaseRepository.findAll();
        assertThat(orderPurchaseList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrderPurchase in Elasticsearch
        verify(mockOrderPurchaseSearchRepository, times(0)).save(orderPurchase);
    }

    @Test
    @Transactional
    public void getAllOrderPurchases() throws Exception {
        // Initialize the database
        orderPurchaseRepository.saveAndFlush(orderPurchase);

        // Get all the orderPurchaseList
        restOrderPurchaseMockMvc.perform(get("/api/order-purchases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderPurchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getOrderPurchase() throws Exception {
        // Initialize the database
        orderPurchaseRepository.saveAndFlush(orderPurchase);

        // Get the orderPurchase
        restOrderPurchaseMockMvc.perform(get("/api/order-purchases/{id}", orderPurchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderPurchase.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.orderAmount").value(DEFAULT_ORDER_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderPurchase() throws Exception {
        // Get the orderPurchase
        restOrderPurchaseMockMvc.perform(get("/api/order-purchases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderPurchase() throws Exception {
        // Initialize the database
        orderPurchaseService.save(orderPurchase);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOrderPurchaseSearchRepository);

        int databaseSizeBeforeUpdate = orderPurchaseRepository.findAll().size();

        // Update the orderPurchase
        OrderPurchase updatedOrderPurchase = orderPurchaseRepository.findById(orderPurchase.getId()).get();
        // Disconnect from session so that the updates on updatedOrderPurchase are not directly saved in db
        em.detach(updatedOrderPurchase);
        updatedOrderPurchase
            .orderDate(UPDATED_ORDER_DATE)
            .orderAmount(UPDATED_ORDER_AMOUNT);

        restOrderPurchaseMockMvc.perform(put("/api/order-purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrderPurchase)))
            .andExpect(status().isOk());

        // Validate the OrderPurchase in the database
        List<OrderPurchase> orderPurchaseList = orderPurchaseRepository.findAll();
        assertThat(orderPurchaseList).hasSize(databaseSizeBeforeUpdate);
        OrderPurchase testOrderPurchase = orderPurchaseList.get(orderPurchaseList.size() - 1);
        assertThat(testOrderPurchase.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrderPurchase.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);

        // Validate the OrderPurchase in Elasticsearch
        verify(mockOrderPurchaseSearchRepository, times(1)).save(testOrderPurchase);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderPurchase() throws Exception {
        int databaseSizeBeforeUpdate = orderPurchaseRepository.findAll().size();

        // Create the OrderPurchase

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderPurchaseMockMvc.perform(put("/api/order-purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderPurchase)))
            .andExpect(status().isBadRequest());

        // Validate the OrderPurchase in the database
        List<OrderPurchase> orderPurchaseList = orderPurchaseRepository.findAll();
        assertThat(orderPurchaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OrderPurchase in Elasticsearch
        verify(mockOrderPurchaseSearchRepository, times(0)).save(orderPurchase);
    }

    @Test
    @Transactional
    public void deleteOrderPurchase() throws Exception {
        // Initialize the database
        orderPurchaseService.save(orderPurchase);

        int databaseSizeBeforeDelete = orderPurchaseRepository.findAll().size();

        // Delete the orderPurchase
        restOrderPurchaseMockMvc.perform(delete("/api/order-purchases/{id}", orderPurchase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderPurchase> orderPurchaseList = orderPurchaseRepository.findAll();
        assertThat(orderPurchaseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrderPurchase in Elasticsearch
        verify(mockOrderPurchaseSearchRepository, times(1)).deleteById(orderPurchase.getId());
    }

    @Test
    @Transactional
    public void searchOrderPurchase() throws Exception {
        // Initialize the database
        orderPurchaseService.save(orderPurchase);
        when(mockOrderPurchaseSearchRepository.search(queryStringQuery("id:" + orderPurchase.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orderPurchase), PageRequest.of(0, 1), 1));
        // Search the orderPurchase
        restOrderPurchaseMockMvc.perform(get("/api/_search/order-purchases?query=id:" + orderPurchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderPurchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderPurchase.class);
        OrderPurchase orderPurchase1 = new OrderPurchase();
        orderPurchase1.setId(1L);
        OrderPurchase orderPurchase2 = new OrderPurchase();
        orderPurchase2.setId(orderPurchase1.getId());
        assertThat(orderPurchase1).isEqualTo(orderPurchase2);
        orderPurchase2.setId(2L);
        assertThat(orderPurchase1).isNotEqualTo(orderPurchase2);
        orderPurchase1.setId(null);
        assertThat(orderPurchase1).isNotEqualTo(orderPurchase2);
    }
}
