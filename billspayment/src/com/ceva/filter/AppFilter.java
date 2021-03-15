package com.ceva.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AppFilter implements Filter {

	Logger log = Logger.getLogger(AppFilter.class);
	@Override
	public void destroy() {
		log.info("destroy called..");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		log.info("doFilter called..");
		HttpServletRequestWrapper requestWrapper=new RequestWrapper((HttpServletRequest)request);
		  final HttpServletResponse httpResponse=(HttpServletResponse)response;
		  String mimeType=request.getContentType();
		 if(mimeType==null)
			 httpResponse.setContentType("text/plain; charset=utf-8");
		 else if(mimeType.startsWith("application/xml"))
			 httpResponse.setContentType("text/xml; charset=utf-8");
		 else if(mimeType.startsWith("text/html"))
			 httpResponse.setContentType("text/html; charset=utf-8");
		 chain.doFilter(requestWrapper, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.info("init called..");

	}

}
