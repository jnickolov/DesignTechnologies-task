package com.designtechnologies.task.jbn.model.currences;

import java.util.regex.Pattern;

import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class Currency {
	private static String pattern = "^([\\w]){3}$";

	public static void checkCode (String aCode) throws InvalidCurrencyCodeException {
		if (! Pattern.matches (pattern, aCode))
			throw new InvalidCurrencyCodeException ("Illegal currency code: " + aCode);
	}
	
	public static Currency of (String code) throws InvalidCurrencyCodeException {
		checkCode (code);
		return new Currency (code);
	}
	
	private String code;
	
	private Currency (String code) {
		this.code = code;
	}
	
	public String toString () {
		return this.code;
	}
}
