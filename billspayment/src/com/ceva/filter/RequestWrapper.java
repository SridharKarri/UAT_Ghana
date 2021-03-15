package com.ceva.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

public class RequestWrapper extends HttpServletRequestWrapper {
	Logger log = Logger.getLogger(RequestWrapper.class);
	HttpServletRequest request = null;

	public RequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
		log.info(request.getParameterMap());
	}

}
