package com.ceva.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DBListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//ConnectionHelper.shutDown();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//ConnectionHelper.init();
	}

}
