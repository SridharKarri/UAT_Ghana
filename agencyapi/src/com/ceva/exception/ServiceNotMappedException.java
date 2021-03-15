package com.ceva.exception;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ServiceNotMappedException extends Exception {
	
	Logger logger=Logger.getLogger(ServiceNotMappedException.class);
	
	private String errorCode;
	
	public ServiceNotMappedException() {
		super();
	}

	public ServiceNotMappedException(String message) {
		super(message);
		logger.info("message..:"+message);
	}
	
	public ServiceNotMappedException(String errorCode, String message) {
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
