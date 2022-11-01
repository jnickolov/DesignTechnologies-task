package com.designtechnologies.task.jbn.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.designtechnologies.task.jbn.dto.CustomerBalance;
import com.designtechnologies.task.jbn.dto.InvoiceResponseData;
import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.currences.CurrencyConverter;
import com.designtechnologies.task.jbn.model.documents.Invoice;
import com.designtechnologies.task.jbn.services.DocumentDataService;
import com.designtechnologies.task.jbn.services.InvoiceService;

@RestController
@RequestMapping("/api/v1")
public class InvoiceRestController {

	@Autowired
	DocumentDataService docDataService;

	@Autowired
	InvoiceService invoiceService;

	@PostMapping (path="/sumInvoices", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
	public ResponseEntity<InvoiceResponseData> calcBalance (
		@RequestPart (value = "exchangeRates", required = true) String exchangeRates,
        @RequestPart (value = "file", required = true) MultipartFile file,
		@RequestPart (value = "outputCurrency", required = true) String outputCurrency,
		@RequestPart (value = "customerVat", required = false) String customerVat) {
		
		try {
			CurrencyConverter.init (exchangeRates);
			
			String sCSV = new String (file.getBytes());
			List<Invoice> invoices = docDataService.processRawData (sCSV);
		
			List<CustomerBalance> balances = invoiceService.proceessInvoices (
					invoices, Currency.of(outputCurrency), customerVat);
			
			return ResponseEntity.ok (new InvoiceResponseData (outputCurrency, balances));
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().header ("Error-message", e.getMessage()).build();
		}
	}
}
