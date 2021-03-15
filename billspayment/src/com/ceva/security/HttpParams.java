package com.ceva.security;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class HttpParams {
	HttpServletRequest request;
	HttpServletResponse response;
	boolean parseStatusFlag = false;

	public HttpParams(){

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public boolean isParseStatusFlag() {
		return parseStatusFlag;
	}

	public void setParseStatusFlag(boolean parseStatusFlag) {
		this.parseStatusFlag = parseStatusFlag;
	}



}
