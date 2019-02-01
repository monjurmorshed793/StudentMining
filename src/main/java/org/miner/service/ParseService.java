package org.miner.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
@Transactional
public class ParseService {

    private CustomerDataExtrationService customerDataExtrationService;
    private OrderPurchaseDataExtractionService orderPurchaseDataExtractionService;


    public ParseService(CustomerService customerService, OrderPurchaseService orderPurchaseService, CustomerDataExtrationService customerDataExtrationService, OrderPurchaseDataExtractionService orderPurchaseDataExtractionService) {
        this.customerDataExtrationService = customerDataExtrationService;
        this.orderPurchaseDataExtractionService = orderPurchaseDataExtractionService;
    }

    private final Logger log = LoggerFactory.getLogger(ParseService.class);

    public void extractWorkbook(File file) throws Exception{
        Workbook workbook = WorkbookFactory.create(file);

        for(int i=0; i<2; i++){
            Sheet sheet = workbook.getSheetAt(i);
            if(i==0)
                customerDataExtrationService.extractCustomerData(sheet);
            else if(i==1)
                orderPurchaseDataExtractionService.extractOrderPurchaseData(sheet);
        }
    }

    public void extractSql(File file) throws Exception{

    }

    public void extractTextFile(File file) throws Exception{

    }
}
