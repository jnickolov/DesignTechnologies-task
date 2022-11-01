package com.designtechnologies.task.jbn.dto;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class InvoiceResponseData {
	private String currency;
	private List<CustomerBalance> customers;
}
