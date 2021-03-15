package com.ceva.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ServiceLimitException extends Exception {
	
	Logger logger=Logger.getLogger(ServiceLimitException.class);
	
	private String errorCode;
	
	public ServiceLimitException() {
		super();
	}

	public ServiceLimitException(String message) {
		super(message);
		logger.info("message..:"+message);
	}
	
	public ServiceLimitException(String errorCode, String message) {
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
