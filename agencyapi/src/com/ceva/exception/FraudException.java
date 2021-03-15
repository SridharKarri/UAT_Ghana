package com.ceva.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class FraudException extends Exception {
	
	Logger logger=Logger.getLogger(FraudException.class);
	
	private String errorCode;
	
	public FraudException() {
		super();
	}

	public FraudException(String message) {
		super(message);
		logger.info("message..:"+message);
	}
	
	public FraudException(String errorCode, String message) {
		super(message);
		this.errorCode=errorCode;
		logger.info("message..:"+message);
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	public String getErrorCode() {
		return errorCode;
	}
	

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	

}
