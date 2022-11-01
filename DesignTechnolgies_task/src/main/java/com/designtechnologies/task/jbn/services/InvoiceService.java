package com.designtechnologies.task.jbn.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.designtechnologies.task.jbn.dto.CustomerBalance;
import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.documents.Invoice;
import com.designtechnologies.task.jbn.model.money.Money;

import lombok.NonNull;

@Service
public class InvoiceService {
	
	public List<CustomerBalance> proceessInvoices (
			@NonNull List<Invoice> allInvoices, 
			@NonNull Currency outputCurrency, 
			String customerVat) {
		List<CustomerBalance> customerBalances = new LinkedList<>();  // the result
		
		List<Invoice> invoices;
		
		//  Filter invoices if need
		if (customerVat != null && customerVat.length() > 0) {
			invoices = allInvoices.stream()
				.filter (inv -> inv.getClient().getVatCode().equals (customerVat))
				.collect (Collectors.toList());
		} else {
			invoices = allInvoices;
		}

		// Separate invoices by customer name
		Map<String, List<Invoice>> customerMap = invoices.stream().
			collect (Collectors.groupingBy (inv -> inv.getClient().getName()));

		// Sum invoice's totals in each invoice list
		customerMap.keySet().forEach (customerName -> {
			Money tot = customerMap.get (customerName).stream()  // stream of Invoices
				.map (inv -> inv.total().toCurrencySafe (outputCurrency)) // stream of Money 
				.reduce (Money.of (outputCurrency, 0.0), (sum, val) -> sum.plusSafe (val)   // sum of Money
			);
			customerBalances.add (new CustomerBalance (customerName, tot.getValue().doubleValue()));
		});
		
		return customerBalances;
	}
}
