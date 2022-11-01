package com.designtechnologies.task.jbn.model.exceptions;

public class MissingOutputCurrencyException extends Exception {
	private static final long serialVersionUID = 8775717741811878132L;

	public MissingOutputCurrencyException() {
		super();
	}

	public MissingOutputCurrencyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingOutputCurrencyException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingOutputCurrencyException(String message) {
		super(message);
	}

	public MissingOutputCurrencyException(Throwable cause) {
		super(cause);
	}

}
