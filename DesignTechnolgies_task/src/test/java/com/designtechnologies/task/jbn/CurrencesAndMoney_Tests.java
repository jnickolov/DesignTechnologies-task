package com.designtechnologies.task.jbn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import com.designtechnologies.task.jbn.model.currences.Currency;
import com.designtechnologies.task.jbn.model.currences.CurrencyConverter;
import com.designtechnologies.task.jbn.model.exceptions.InvalidCurrencyCodeException;
import com.designtechnologies.task.jbn.model.money.Money;

@SpringBootTest
public class CurrencesAndMoney_Tests {

	@BeforeAll
	static void initCurrencyConverter () {
		try {
			CurrencyConverter.init ("EUR:1,BGN:2,USD:1.6");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ParameterizedTest
	@ValueSource (strings = {"BGN", "EUR", "EUR"})
	void currencyCreationTest (String code) throws InvalidCurrencyCodeException {
		Currency c = Currency.of (code);
		assertEquals (code, c.getCode());
	}

	@ParameterizedTest
	@ValueSource (strings = {"BG", "EURO", ""})
	void currencyCreationTest_wrongCodes (String code) {
		assertThrows (InvalidCurrencyCodeException.class, () -> Currency.of (code));
	}
	
	@Test
	void moneyCreationTests () throws InvalidCurrencyCodeException {
		String bgCode = "BGN";
		Money money = Money.of (bgCode, 1.0);
		
		assertEquals (Currency.of (bgCode), money.getCurrency());
		assertEquals (1.0, money.getValue().doubleValue());
		money = Money.of (bgCode, "2");
		assertEquals (Currency.of (bgCode), money.getCurrency());
		assertEquals (2.0, money.getValue().doubleValue());
	}
	
	@Test
	void moneyCreationTests_BadData () throws InvalidCurrencyCodeException {
		String bgCode = "BG";
		assertThrows (InvalidCurrencyCodeException.class, () -> {Money m = Money.of (bgCode, 1.0); });
	}
	
	@Test
	void moneyCalcTests () throws InvalidCurrencyCodeException {
		Money eur = Money.of ("EUR", 10.0);
		Money bgn = Money.of ("BGN", 10.0);
		Money sum = eur.plus (bgn);
		assertEquals (15.0, sum.getValue().doubleValue());
		assertEquals (Currency.of ("EUR"), sum.getCurrency());
		
		Money sum2 = sum.multiply (new BigDecimal (2.0));
		assertEquals (30.0, sum2.getValue().doubleValue());

		bgn = Money.of("BGN", 100.0);
		eur = bgn.toCurrency ("EUR");
		assertEquals (50.0,  eur.getValue().doubleValue());
		
		eur = eur.minus(bgn);
		assertEquals (0.0,  eur.getValue().doubleValue());
	}
	
	@Test
	void exchangeRatesTests () throws InvalidCurrencyCodeException {
		BigDecimal b2b = CurrencyConverter.exchangeRate ("BGN", "BGN");
		BigDecimal b2e = CurrencyConverter.exchangeRate ("BGN", "EUR");
		BigDecimal e2b = CurrencyConverter.exchangeRate ("EUR", "BGN");
		BigDecimal e2d = CurrencyConverter.exchangeRate ("EUR", "USD");
		BigDecimal d2e = CurrencyConverter.exchangeRate ("USD", "EUR");
		
		assertEquals (1.0, b2b.doubleValue());
		assertEquals (0.5, b2e.doubleValue());
		assertEquals (1.6, e2d.doubleValue());
//		System.out.println("---------------  " + e2d.doubleValue());
//		System.out.println("---------------  " + d2e.doubleValue());
		assertEquals (2.0, e2b.doubleValue());
		assertEquals (0.625, d2e.doubleValue());
	}
	
}
