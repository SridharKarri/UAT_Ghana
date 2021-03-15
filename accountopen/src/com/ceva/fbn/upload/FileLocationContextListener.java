package com.ceva.fbn.upload;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

@WebListener
public class FileLocationContextListener implements ServletContextListener {
Logger logger = Logger.getLogger(FileLocationContextListener.class);
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	String rootPath = System.getProperty("catalina.home");
    	ServletContext ctx = servletContextEvent.getServletContext();
    	String relativePath = ctx.getInitParameter("tempfile.dir");
    	File file = new File(relativePath);
    	if(!file.exists()) file.mkdirs();
    	logger.info("File Directory created to be used for storing files");
    	ctx.setAttribute("FILES_DIR_FILE", file);
    	ctx.setAttribute("FILES_DIR", relativePath);
    }

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		//do cleanup if needed
	}
	
}
