package com.ceva.bean.processor;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.ceva.bank.utils.DESEncryption;
import com.zaxxer.hikari.HikariDataSource;

public class BoneCpBeanProcessor implements BeanPostProcessor {

	private Logger log = Logger.getLogger(BoneCpBeanProcessor.class);

	@Override
	public Object postProcessAfterInitialization(Object bean, String name)
			throws BeansException {
		try {
			log.debug("--- Inside postProcessAfterInitialization ---");
			log.debug("			name is ::: " + name);

			if (bean instanceof HikariDataSource) {
				log.debug("--- Inside BoneCpBeanProcessor ---");

				HikariDataSource hikarDataSource = (HikariDataSource) bean;

				log.debug("Username ::: " + hikarDataSource.getUsername());
			}

		} catch (Exception e) {
		}

		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String name)
			throws BeansException {
		Object obj = bean;
		HikariDataSource hikarDataSource = null;
		try {
			log.debug("--- Inside postProcessBeforeInitialization ---");
			log.debug("			name is ::: " + name);

			if (bean instanceof HikariDataSource) {
				log.debug("--- Inside HikariDataSource ---");

				hikarDataSource = (HikariDataSource) bean;

				log.debug("Username ::: " + hikarDataSource.getUsername());
				hikarDataSource.setUsername(new DESEncryption()
						.decrypt(hikarDataSource.getUsername()));
				hikarDataSource.setPassword(new DESEncryption()
						.decrypt(hikarDataSource.getPassword()));
			}

			if (hikarDataSource != null)
				obj = hikarDataSource;

		} catch (Exception e) {
		}

		return obj;
	}
}
