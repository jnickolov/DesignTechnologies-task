package com.designtechnologies.task.jbn.model.exceptions;

public class ExchangeRateInitializationException extends Exception {
	private static final long serialVersionUID = -3308304844824447366L;

	public ExchangeRateInitializationException() {
		super();
	}

	public ExchangeRateInitializationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExchangeRateInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExchangeRateInitializationException(String message) {
		super(message);
	}

	public ExchangeRateInitializationException(Throwable cause) {
		super(cause);
	}
}
