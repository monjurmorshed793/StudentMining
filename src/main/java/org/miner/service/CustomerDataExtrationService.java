package org.miner.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.miner.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerDataExtrationService {

    private final Logger log = LoggerFactory.getLogger(CustomerDataExtrationService.class);

    private CustomerService customerService;

    public CustomerDataExtrationService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public List<Customer> extractCustomerData(Sheet sheet){
        List<Customer> customers = new ArrayList<>();
        int rowNumber = 1;
        while(true){
            Row row = sheet.getRow(rowNumber);

            Customer customer = new Customer();
            customer.setId(Long.parseLong(row.getCell(0).toString()));
            customer.setLastName(row.getCell(1).toString());
            customer.setFirstName(row.getCell(2).toString());
            customer.setAreaCode(Integer.parseInt(row.getCell(3).toString()));
            customer.setAddress(row.getCell(4).toString());
            customer.setPhone(row.getCell(5).toString());
            customers.add(customer);
            rowNumber+=1;
            if(sheet.getRow(rowNumber).getCell(0)==null)
                break;
        }

        return customerService.save(customers);
    }
}
