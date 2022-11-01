package com.designtechnologies.task.jbn.model.exceptions;

public class MissingDefaultCurrencyException extends Exception {
	private static final long serialVersionUID = 8775717741811878132L;

	public MissingDefaultCurrencyException() {
		super();
	}

	public MissingDefaultCurrencyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingDefaultCurrencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingDefaultCurrencyException(String message) {
		super(message);
	}

	public MissingDefaultCurrencyException(Throwable cause) {
		super(cause);
	}

}
