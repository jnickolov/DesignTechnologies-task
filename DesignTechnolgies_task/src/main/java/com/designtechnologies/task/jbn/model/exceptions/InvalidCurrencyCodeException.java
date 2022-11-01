package com.designtechnologies.task.jbn.model.exceptions;

public class InvalidCurrencyCodeException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidCurrencyCodeException() {
		super();
	}

	public InvalidCurrencyCodeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidCurrencyCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCurrencyCodeException(String message) {
		super(message);
	}

	public InvalidCurrencyCodeException(Throwable cause) {
		super(cause);
	}

}
