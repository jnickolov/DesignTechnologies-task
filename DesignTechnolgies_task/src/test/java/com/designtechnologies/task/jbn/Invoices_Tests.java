package com.designtechnologies.task.jbn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.designtechnologies.task.jbn.model.contragents.Client;
import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.currences.CurrencyConverter;
import com.designtechnologies.task.jbn.model.documents.CreditNote;
import com.designtechnologies.task.jbn.model.documents.DebitNote;
import com.designtechnologies.task.jbn.model.documents.Document;
import com.designtechnologies.task.jbn.model.documents.Invoice;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;
import com.designtechnologies.task.jbn.model.exceptions.InvalidDocumentDataException;
import com.designtechnologies.task.jbn.model.money.Money;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class Invoices_Tests {

	@BeforeAll
	static void initCurrencyConverter () {
		try {
			CurrencyConverter.init ("EUR:1,BGN:2,USD:1.5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void InvoiceTests () throws InvalidDocumentDataException, InvalidCurrencyCodeException {
		Client client = new Client("The Client", "12345");
		Invoice inv = Document.createInvoice ("10001", client, Money.of ("BGN", 1000.0));
		
		assertEquals (
			Money.of ("BGN", new BigDecimal (1000.0)).getValue().doubleValue(), 
			inv.total().getValue().doubleValue());
		
		// Add 100 EUR debit (exchange rate: EUR:BGN = 2)
		DebitNote dn1 = Document.createDebitNote ("20001", client, Money.of ("EUR", 100.0), "10001");
		inv.addDebitNote (dn1);

		// Add 30 USD debit (exchange rate: EUR:USD = 1.5: 20 USD = 20 EUR = 40 BGN)
		DebitNote dn2 = Document.createDebitNote ("20002", client, Money.of ("USD", 30.0), "10001");
		inv.addDebitNote (dn2);
		log.info ("Debits EUR: " + inv.totalDebits(Currency.of ("EUR")));
		log.info ("Debits USD: " + inv.totalDebits(Currency.of ("USD")));
		log.info ("Debits BGN: " + inv.totalDebits(Currency.of ("BGN")));
		log.info ("Total  BGN: " + inv.total());

		assertTrue (Math.abs (240.0 - inv.totalDebits(Currency.of ("BGN")).getValue().doubleValue()) < 1.0e-6);
		assertTrue (Math.abs (1240.0 - inv.total().getValue().doubleValue()) < 1.0e-6);

		CreditNote cn1 = Document.createCreditNote("30001", client, Money.of ("EUR", 500.0), "10001");
		inv.addCreditNote (cn1);

		CreditNote cn2 = Document.createCreditNote("30002", client, Money.of ("USD", 30.0), "10001");
		inv.addCreditNote (cn2);
		log.info ("Credits EUR: " + inv.totalCredits (Currency.of ("EUR")));
		log.info ("Credits USD: " + inv.totalCredits (Currency.of ("USD")));
		log.info ("Credits BGN: " + inv.totalCredits (Currency.of ("BGN")));
		log.info ("Total   BGN: " + inv.total());

		assertTrue (Math.abs (1040.0 - inv.totalCredits(Currency.of ("BGN")).getValue().doubleValue()) < 1.0e-6);
		assertTrue (Math.abs (200.0 - inv.total().getValue().doubleValue()) < 1.0e-6);
	}
}
