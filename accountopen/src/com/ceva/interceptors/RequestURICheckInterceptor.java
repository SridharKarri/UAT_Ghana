package com.ceva.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestURICheckInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(RequestURICheckInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("|PreHandle| Request URL::" + request.getRequestURL().toString()
				+ ":: Start Time=" + System.currentTimeMillis());

		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.addHeader("Cache-Control", "pre-check=0,post-check=0");
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.

		request.setAttribute("startTime", startTime);
		// if returned false, we need to make sure 'response' is sent
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("Request URL::" + request.getRequestURL().toString()
				+ " Sent to Handler :: Current Time="
				+ System.currentTimeMillis());
		// we can add attributes in the modelAndView and use that in the view
		// page
		try {
			logger.info("The Model View is ::: " + modelAndView.getModel());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long startTime = (Long) request.getAttribute("startTime");
		logger.info("|afterCompletion| Request URL::" + request.getRequestURL().toString()
				+ ":: End Time=" + System.currentTimeMillis());
		logger.info("|afterCompletion| Request URL::" + request.getRequestURL().toString()
				+ ":: Time Taken=" + (System.currentTimeMillis() - startTime));
	}

}