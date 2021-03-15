package com.ceva.bean.processor;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class DeRegisterDrivers {
	private Logger log = Logger.getLogger(DeRegisterDrivers.class);

	public void deRegisterDrivers() {

		// First close any background tasks which may be using the DB ...
		// Then close any DB connection pools ...
		// Now De-register JDBC drivers in this context's ClassLoader:
		// Get the webapp's ClassLoader
		ClassLoader cl = null;
		// Loop through all drivers
		Enumeration<Driver> drivers = null;
		Driver driver = null;
		try {

			cl = Thread.currentThread().getContextClassLoader();
			drivers = DriverManager.getDrivers();

			while (drivers.hasMoreElements()) {
				driver = drivers.nextElement();
				if (driver.getClass().getClassLoader() == cl) {
					// This driver was registered by the webapp's ClassLoader,
					// so
					// deregister it:
					try {
						log.info(" |DeregisterDrivers| Deregistering JDBC driver {"
								+ driver + "}");
						DriverManager.deregisterDriver(driver);
					} catch (SQLException ex) {
						log.info(" |DeregisterDrivers| Error deregistering JDBC driver {"
								+ driver + "} :: " + ex.getMessage());
					}
				} else {
					// driver was not registered by the webapp's ClassLoader and
					// may
					// be in use elsewhere
					log.info(" |DeregisterDrivers| Not deregistering JDBC driver {} as "
							+ "it does not belong to this webapp's ClassLoader..."
							+ driver);
				}
			}

		} catch (Exception e) {
			log.info("|DeregisterDrivers| Got Error While Destorying Connection .... "
					+ e.getMessage());
		} finally {
			cl = null;
			drivers = null;
			driver = null;
		}
	}
}
