package com.designtechnologies.task.jbn.model.documents;

import java.util.HashSet;
import java.util.Set;

import com.designtechnologies.task.jbn.model.contragents.Client;
import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;
import com.designtechnologies.task.jbn.model.money.Money;
import com.designtechnologies.task.jbn.model.money.MoneyBuffer;

import lombok.NonNull;

public class Invoice extends Document {
	private final Set<DebitNote> debitNotes = new HashSet<>();
	private final Set<CreditNote> creditNotes = new HashSet<>();

	public Invoice (@NonNull String docNo, @NonNull Client client, @NonNull Money money) {
		super (docNo, client, money);
	}

	public Invoice addDebitNote (@NonNull DebitNote debitNote) {
		this.debitNotes.add (debitNote);
		return this;
	}

	public Invoice removeDebitNote (@NonNull DebitNote debitNote) {
		this.debitNotes.remove (debitNote);
		return this;
	}

	public boolean containsDebitNote (@NonNull String docNo) {
		return this.debitNotes.stream()
			.filter (d -> d.getDocNo().equals(docNo))
			.findFirst()
			.isPresent();
	}

	public Invoice addCreditNote (@NonNull CreditNote creditNote) {
		this.creditNotes.add (creditNote);
		return this;
	}

	public Invoice removeCreditNote (@NonNull CreditNote creditNote) {
		this.creditNotes.remove (creditNote);
		return this;
	}

	public boolean containsCreditNote (@NonNull String docNo) {
		return this.creditNotes.stream()
			.filter (d -> d.getDocNo().equals (docNo))
			.findFirst()
			.isPresent();
	}
	
	/**
	 * Calculates total invoice value on its own currency. 
	 * All attached debits and credits are included, 
	 * corrected by their respective currency exchange rates.
	 * 
	 * @return the total value of invoice
	 * @throws InvalidCurrencyCodeException 
	 */
	public Money total () {
		final Currency invoiceCurrency = this.getValue().getCurrency();
		
		return this.getValue()
			.plusSafe (totalDebits (invoiceCurrency))
			.minusSafe (totalCredits (invoiceCurrency));
	}
	
	public Money totalDebits (Currency invoiceCurrency) {
		return this.debitNotes.stream()
			.map (DebitNote::getValue)
			.reduce (Money.of (invoiceCurrency, 0.0), (tot, m) -> tot.plusSafe (m)
		);
	}
	
	public Money totalCredits (Currency invoiceCurrency) {
		return this.creditNotes.stream()
			.map (CreditNote::getValue)
			.reduce (Money.of (invoiceCurrency, 0.0), (tot, m) -> tot.plusSafe (m));
	}
}
