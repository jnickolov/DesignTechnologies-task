package com.designtechnologies.task.jbn.model.money;

import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;

import lombok.NonNull;

public class MoneyBuffer {
	private Money buffer = null;
			
	public MoneyBuffer (String currencyCode) throws InvalidCurrencyCodeException {
		 this.buffer = Money.of (Currency.of (currencyCode), 0.0);
	}

	public MoneyBuffer (Currency currency) {
		 this.buffer = Money.of (currency, 0.0);
	}
	
	public Money value() {
		return this.buffer;
	}
	
	public MoneyBuffer add (@NonNull Money money) throws InvalidCurrencyCodeException {
		this.buffer = this.buffer.plus (money);
		
		return this;
	}

	public MoneyBuffer subtract (@NonNull Money money) throws InvalidCurrencyCodeException {
		this.buffer = this.buffer.minus (money);
		
		return this;
	}

	public MoneyBuffer multiply (double factor) {
		this.buffer = this.buffer.multiply (factor);
		
		return this;
	}
}
