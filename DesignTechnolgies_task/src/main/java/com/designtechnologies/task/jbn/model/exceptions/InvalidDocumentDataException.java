package com.designtechnologies.task.jbn.model.exceptions;

public class InvalidDocumentDataException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidDocumentDataException() {
		super();
	}

	public InvalidDocumentDataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidDocumentDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDocumentDataException(String message) {
		super(message);
	}

	public InvalidDocumentDataException(Throwable cause) {
		super(cause);
	}

	
}
