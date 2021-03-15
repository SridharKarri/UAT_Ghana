package com.ceva.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class CustomResponseWrapper extends HttpServletResponseWrapper{

	ByteArrayOutputStream output;
	FilterServletOutputStream filterOutput;
	HttpSession session = null;
	private Logger logger = Logger.getLogger(CustomResponseWrapper.class);
    public CustomResponseWrapper(HttpServletResponse response, HttpSession session) {
        super(response);
        output = new ByteArrayOutputStream();
        this.session = session;
    }

	@Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (filterOutput == null) {
            filterOutput = new FilterServletOutputStream(output);
        }
        return filterOutput;
    }

    public byte[] getDataStream() {
        return output.toByteArray();
    }

}
