package com.ceva.bean.processor;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class DeRegisterDrivers {
	private Logger log = Logger.getLogger(DeRegisterDrivers.class);

	public void deRegisterDrivers() {

		ClassLoader cl = null;
		Enumeration<Driver> drivers = null;
		Driver driver = null;
		try {

			cl = Thread.currentThread().getContextClassLoader();
			drivers = DriverManager.getDrivers();

			while (drivers.hasMoreElements()) {
				driver = drivers.nextElement();
				if (driver.getClass().getClassLoader() == cl) {
					try {
						log.info(" |DeregisterDrivers| Deregistering JDBC driver {"
								+ driver + "}");
						DriverManager.deregisterDriver(driver);
					} catch (SQLException ex) {
						log.info(" |DeregisterDrivers| Error deregistering JDBC driver {"
								+ driver + "} :: " + ex.getMessage());
					}
				} else {
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
