package com.ceva.exception;

@SuppressWarnings("serial")
public class ErrorHandler extends Exception {

	private String code;

	private String message;

	public ErrorHandler(String message) {
		this.message = message;
	}

	public ErrorHandler(String message, String code) {
		this.code = code;
		this.message = message;
	}

	public ErrorHandler(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public ErrorHandler(Throwable cause) {
		super(cause);
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
