package com.designtechnologies.task.jbn;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.designtechnologies.task.jbn.dto.CustomerBalance;
import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.currences.CurrencyConverter;
import com.designtechnologies.task.jbn.model.documents.Invoice;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;
import com.designtechnologies.task.jbn.model.exceptions.InvalidDocumentDataException;
import com.designtechnologies.task.jbn.services.DocumentDataService;
import com.designtechnologies.task.jbn.services.InvoiceService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class Documents_Tests {

	@Autowired
	DocumentDataService docDataService;

	@Autowired
	InvoiceService invoiceService;
	
	
	@BeforeAll
	static void initCurrencyConverter () {
		try {
			CurrencyConverter.init ("EUR:1,BGN:2,USD:0.878,GBP:0.987");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testInvoiceCreation () {
		String rawData = 
				"Header row (will be skipped)\n"+
//				"Vendor 1,123456789,1000000257,1,,EUR,400\n"+
//				"Vendor 2,987654321,1000000258,1,,EUR,900\n"+
//				"Vendor 3,123465123,1000000259,1,,GBP,1300\n"+
//				"Vendor 1,123456789,1000000260,2,1000000257,BGN,100\n"+
//				"Vendor 1,123456789,1000000261,3,1000000257,EUR,50\n"+
//				"Vendor 2,987654321,1000000262,2,1000000258,EUR,200\n"+
//				"Vendor 3,123465123,1000000263,3,1000000259,EUR,100\n"+
//				"Vendor 1,123456789,1000000264,1,,EUR,1600";
		// Original test data
			"Vendor 1,123456789,1000000257,1,,USD,400\n"+
			"Vendor 2,987654321,1000000258,1,,EUR,900\n"+
			"Vendor 3,123465123,1000000259,1,,GBP,1300\n"+
			"Vendor 1,123456789,1000000260,2,1000000257,EUR,100\n"+
			"Vendor 1,123456789,1000000261,3,1000000257,GBP,50\n"+
			"Vendor 2,987654321,1000000262,2,1000000258,USD,200\n"+
			"Vendor 3,123465123,1000000263,3,1000000259,EUR,100\n"+
			"Vendor 1,123456789,1000000264,1,,EUR,1600";
		
		try {
			List<Invoice> invoices = docDataService.processRawData (rawData);
//			invoices.forEach (inv -> 
//					System.out.println(inv.getDocNo() + ": " + inv.total())
//			);

			List<CustomerBalance> res = invoiceService.proceessInvoices(invoices, Currency.of("EUR"), "");
			res.forEach(System.out::println);
			
			res = invoiceService.proceessInvoices(invoices, Currency.of("BGN"), "");
			res.forEach(System.out::println);
			
			
		} catch (InvalidDocumentDataException | InvalidCurrencyCodeException e) {
			System.out.println("FANAH TE: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	// TODO: ako v dannite ima currency, koeto da nqma exchange rate - gurmi
}
