package com.ceva.bank.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.AcctOpenMasterDao;
import com.ceva.db.model.AccountOpeningMaster;

@Repository("acctOpenMasterDao")
public class AcctOpenMasterDaoImpl implements AcctOpenMasterDao {

	Logger log = Logger.getLogger(AcctOpenMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public AccountOpeningMaster getJsonDataforService(String serviceName) {

		log.info("loading getJsonDataforService from db with serviceName." + serviceName);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(AccountOpeningMaster.class);
			criteria.add(Restrictions.eq("accountServiceName", serviceName));
			criteria.add(Restrictions.eq("status", "A"));
			return (AccountOpeningMaster)criteria.uniqueResult();
		} catch (Exception e) {
			log.error("Error while loading AccountOpeningMaster.");
			log.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}
}