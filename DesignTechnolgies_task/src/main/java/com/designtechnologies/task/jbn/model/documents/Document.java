package com.designtechnologies.task.jbn.model.documents;

import java.math.BigDecimal;

import com.designtechnologies.task.jbn.dto.DocumentData;
import com.designtechnologies.task.jbn.model.contragents.Client;
import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;
import com.designtechnologies.task.jbn.model.exceptions.InvalidDocumentDataException;
import com.designtechnologies.task.jbn.model.money.Money;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor (access = AccessLevel.PROTECTED)
public class Document {
		public static final int 
			INVOICE = 1,
			CREDIT_NOTE = 2,
			DEBIT_NOTE = 3;
	
	private final String docNo;
	private final Client client;
	private Money value;

	public static Document ofData (@NonNull DocumentData data)
			throws InvalidDocumentDataException, InvalidCurrencyCodeException {
		Client client = new Client (data.getClientName(), data.getClientVat());
		Currency currency = Currency.of (data.getCurrencyCode());
		Money money = Money.of (currency, new BigDecimal (data.getTotal()));
		switch (data.getType()) {
			case INVOICE:
				return createInvoice (data.getDocNo(), client, money);
			case CREDIT_NOTE:
				return createCreditNote (data.getDocNo(), client, money, data.getParentDocNo());
			case DEBIT_NOTE:
				return createDebitNote (data.getDocNo(), client, money, data.getParentDocNo());
			default:
				throw new InvalidDocumentDataException("Invalid document type: " + data.getType());
		}
	}
	
	public static Invoice createInvoice (String docNo, Client client, Money money) {
		return new Invoice (docNo, client, money);
	}
	
	public static CreditNote createCreditNote (String docNo, Client client, Money money,String parentDocNo) {
		return new CreditNote (docNo, client, money, parentDocNo);
	}
	
	public static DebitNote createDebitNote (String docNo, Client client, Money money,String parentDocNo) {
		return new DebitNote (docNo, client, money, parentDocNo);
	}
	
	public static boolean isValidType (int docType) {
		return docType >= INVOICE && docType <= DEBIT_NOTE;
	}

	public static boolean needsParentDocument(@NonNull Integer type) {
		return type > INVOICE;
	}
}
