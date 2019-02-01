package org.miner.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.miner.domain.Customer;
import org.miner.domain.OrderPurchase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderPurchaseDataExtractionService {

    private final Logger log = LoggerFactory.getLogger(OrderPurchaseDataExtractionService.class);

    private OrderPurchaseService orderPurchaseService;
    private CustomerService customerService;


    public OrderPurchaseDataExtractionService(OrderPurchaseService orderPurchaseService, CustomerService customerService) {
        this.orderPurchaseService = orderPurchaseService;
        this.customerService = customerService;
    }

    public List<OrderPurchase> extractOrderPurchaseData(Sheet sheet){
        List<OrderPurchase> orderPurchases = new ArrayList<>();
        int rowNumber = 1;

        while(true){
            Row row = sheet.getRow(rowNumber);

            OrderPurchase orderPurchase = new OrderPurchase();
            orderPurchase.setId(Long.parseLong(row.getCell(0).toString()));
            Optional<Customer> customer = customerService.findOne(Long.parseLong(row.getCell(1).toString()));
            orderPurchase.setCustomer(customer.get());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
            orderPurchase.setOrderDate(LocalDate.parse(row.getCell(2).toString(), formatter));
            orderPurchase.setOrderAmount(BigDecimal.valueOf(Long.parseLong(row.getCell(3).toString())));
            orderPurchases.add(orderPurchase);
            rowNumber+=1;
            if(sheet.getRow(rowNumber).getCell(0)==null)
                break;
        }

        return orderPurchaseService.save(orderPurchases);
    }
}
