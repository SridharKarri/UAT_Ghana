package com.ceva.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class URLRequestWrapper extends HttpServletRequestWrapper{

    private static final String AMP = "&";
    private String queryString = null;
    private String requestURI=null;
    private StringBuffer requestURL=null;

    public URLRequestWrapper(final HttpServletRequest request,
            String queryString,StringBuffer requestURL,String requestURI)//final Map<String, String[]> additionalParams,
	{
		super(request);
		this.queryString = queryString;
		this.requestURL = requestURL;
		this.requestURI = requestURI;
	}

    @Override
    public String getQueryString()
    {
    	String query = null;

    	 query = ((HttpServletRequest)getRequest( )).getQueryString( );
         if (query != null)
             return query +AMP+queryString;
         else
             return queryString;
    }
    @Override
    public StringBuffer getRequestURL()
    {
    	return requestURL;
    }

    @Override
    public String getRequestURI()
    {
    	//StringBuffer str
    	return requestURI;
    }
}
