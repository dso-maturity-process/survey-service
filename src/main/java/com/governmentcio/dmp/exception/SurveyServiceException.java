package com.governmentcio.dmp.exception;

public class SurveyServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public SurveyServiceException() {
	}

	public SurveyServiceException(String message) {
		super(message);
	}

	public SurveyServiceException(Throwable cause) {
		super(cause);
	}

	public SurveyServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public SurveyServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
