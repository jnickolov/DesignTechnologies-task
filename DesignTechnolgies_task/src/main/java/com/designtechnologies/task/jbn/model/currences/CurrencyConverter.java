package com.designtechnologies.task.jbn.model.currences;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.designtechnologies.task.jbn.model.exceptions.ExchangeRateInitializationException;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyConverter {
	
	private static Map<Currency, BigDecimal> rates = new HashMap<>();
	
	public static void init (@NonNull String src) 
			throws InvalidCurrencyCodeException, ExchangeRateInitializationException {
		final String pattern = "^([\\w]){3}:\\d*(.\\d+)*$";
		final String separ = ",";
		final String delim = ":";
		
		String [] items = src.split (separ);
		clearRates ();
		
		for (String item: items) {
			item = item.trim();
			
			if (! Pattern.matches (pattern, item)) 
				throw new ExchangeRateInitializationException ("Invalid exchange rate format: " + item);
			
			String [] elems = item.split (delim);
			if (elems.length != 2)
				throw new ExchangeRateInitializationException ("Invalid exchange rate format: " + item);
			
			Currency cur = Currency.of (elems[0]);
			BigDecimal val = new BigDecimal (elems[1]);
			
			if (val.doubleValue() <= 0.0)
				throw new ExchangeRateInitializationException ("Invalid exchange rate value: " + item);
			
			if (rates.get (cur) != null)
				throw new ExchangeRateInitializationException ("Duplicated exchange rate value: " + item);
			
			rates.put (cur, val);
		}
		
		rates.values().stream()
			.filter (v -> v.doubleValue() == 1.0)
			.findFirst()
			.orElseThrow (() -> new ExchangeRateInitializationException ("Missing default currency"));
	}
	
	public static void addCurrencyRate (@NonNull Currency ccy, @NonNull BigDecimal rate) {
		rates.put (ccy, rate);
	}
	
	private static void clearRates () {
		rates = new HashMap<>();
	}
	
	public static BigDecimal exchangeRate (@NonNull String fromCode, @NonNull String toCode) throws InvalidCurrencyCodeException {
		return exchangeRate (Currency.of (fromCode), Currency.of (toCode));
	}
	
	public static BigDecimal exchangeRate (@NonNull Currency from, @NonNull Currency to) throws InvalidCurrencyCodeException {
		BigDecimal nom = rates.get (to);
		BigDecimal denom = rates.get (from);

		try {
			return nom.divide (denom);
		} catch (ArithmeticException e) {
			return new BigDecimal (nom.doubleValue() / denom.doubleValue());
		} catch (NullPointerException e) {
			throw new InvalidCurrencyCodeException ("Unknown currency: [" + from.getCode() + "] or ["+ to.getCode() + "]");
		}
	}
	
	public static boolean isKnownCurrency (Currency cur) {
		return rates.keySet().contains(cur);
	}
	
	public static boolean isKnownCurrencyCode (String curCode) {
		try {
			return isKnownCurrency (Currency.of (curCode));
		} catch (InvalidCurrencyCodeException e) {
			return false;
		}
	}
}



