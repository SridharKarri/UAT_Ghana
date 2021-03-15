package com.ceva.dto;

import java.util.List;

public class ResponseDTO {

	private List<String> errors = null;
	private String message = null;
	private String responseCode = null;
	private Object data = null;
	private Object fee = "0.0";
	private Object commission = "0.0";


	public ResponseDTO() {
		super();
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Object getFee() {
		return fee;
	}

	public void setFee(Object fee) {
		this.fee = fee;
	}

	public Object getCommission() {
		return commission;
	}

	public void setCommission(Object commission) {
		this.commission = commission;
	}

	@Override
	public String toString() {
		return "ResponseDTO [errors=" + errors + ", message=" + message
				+ ", responseCode=" + responseCode + ", data=" + data
				+ ", fee=" + fee.toString() + ", commission=" + commission + "]";
	}



}
