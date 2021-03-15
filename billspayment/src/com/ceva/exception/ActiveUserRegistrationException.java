package com.ceva.exception;

import org.apache.log4j.Logger;

public class ActiveUserRegistrationException extends Exception {
	
	Logger logger=Logger.getLogger(ActiveUserRegistrationException.class);
	
	public ActiveUserRegistrationException() {
		super();
	}

	public ActiveUserRegistrationException(String message) {
		super(message);
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

}
