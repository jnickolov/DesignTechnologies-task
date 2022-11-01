package com.designtechnologies.task.jbn.model.exceptions;

public class DuplicatedExchangeRateException extends Exception {
	private static final long serialVersionUID = -3308304844824447366L;

	public DuplicatedExchangeRateException() {
		super();
	}

	public DuplicatedExchangeRateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DuplicatedExchangeRateException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedExchangeRateException(String message) {
		super(message);
	}

	public DuplicatedExchangeRateException(Throwable cause) {
		super(cause);
	}
}
