package com.designtechnologies.task.jbn.model.money;

import java.math.BigDecimal;

import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.currences.CurrencyConverter;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Money {
	private final @NonNull Currency currency;
	private final @NonNull BigDecimal value;

	public static Money of(@NonNull Currency currency, @NonNull BigDecimal value) {
		return new Money(currency, value);
	}

	public static Money of(@NonNull Currency currency, @NonNull Double value) {
		return Money.of(currency, new BigDecimal(value));
	}

	public static Money of(@NonNull Currency currency, @NonNull String value) {
		return Money.of(currency, new BigDecimal(value));
	}

	public static Money of(@NonNull String currencyCode, @NonNull Double value) throws InvalidCurrencyCodeException {
		return Money.of(Currency.of(currencyCode), new BigDecimal(value));
	}

	public static Money of(@NonNull String currencyCode, @NonNull String value) throws InvalidCurrencyCodeException {
		return Money.of(Currency.of (currencyCode), new BigDecimal(value));
	}

	public static Money of(@NonNull String currencyCode, @NonNull BigDecimal value)
			throws InvalidCurrencyCodeException {
		return Money.of(Currency.of (currencyCode), value);
	}

	public Money toCurrency (@NonNull String currencyCode) throws InvalidCurrencyCodeException {
		return toCurrency (Currency.of (currencyCode));
	}
	
	public Money toCurrency (@NonNull Currency newCurrency) throws InvalidCurrencyCodeException {
		if (this.currency.equals (newCurrency)) {
			return this;
		}

		BigDecimal rate = CurrencyConverter.exchangeRate (this.currency, newCurrency);
		return new Money (newCurrency, this.value.multiply (rate));
	}

	public Money plus(@NonNull Money adder) throws InvalidCurrencyCodeException {
		return new Money(this.currency, 
			this.value.add (adder.toCurrency(this.currency).getValue()));
	}

	public Money minus(@NonNull Money adder) throws InvalidCurrencyCodeException {
		return new Money(this.currency, 
			this.value.subtract (adder.toCurrency(this.currency).getValue()));
	}

	public Money plus(@NonNull BigDecimal adder) {
		return new Money(this.currency, this.value.add(adder));
	}

	public Money minus(@NonNull BigDecimal adder) {
		return new Money(this.currency, this.value.subtract(adder));
	}

	public Money multiply (@NonNull BigDecimal factor) {
		return new Money(this.currency, this.value.multiply(factor));
	}

	public Money multiply (@NonNull Double factor) {
		return new Money (this.currency, this.value.multiply(new BigDecimal(factor)));
	}

	// Safe methods (ignoring exceptions)

	public static Money ofSafe(@NonNull String currencyCode, @NonNull Double value) {
		try  {
			return Money.of(Currency.of(currencyCode), new BigDecimal(value));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Money ofSafe(@NonNull String currencyCode, @NonNull String value)  {
		try {
			return Money.of(Currency.of (currencyCode), new BigDecimal(value));
		} catch (InvalidCurrencyCodeException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Money ofSafe(@NonNull String currencyCode, @NonNull BigDecimal value) {
		try {
			return Money.of(Currency.of (currencyCode), value);
		} catch (InvalidCurrencyCodeException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Money toCurrencySafe (@NonNull Currency outputCurrency) {
		try {
			return toCurrency (outputCurrency);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Money plusSafe (@NonNull Money adder) {
		try {
			return plus (adder);
		} catch (InvalidCurrencyCodeException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Money minusSafe (@NonNull Money adder) {
		try {
			return minus (adder);
		} catch (InvalidCurrencyCodeException e) {
			e.printStackTrace();
			return null;
		}
	}
}
