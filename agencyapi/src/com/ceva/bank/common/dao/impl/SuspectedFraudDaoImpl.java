package com.ceva.bank.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.SuspectedFraudDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.SuspectedFraud;

@Repository("suspectedFraudDao")
public class SuspectedFraudDaoImpl implements SuspectedFraudDao {

	@Autowired
	SessionFactory sessionFactory;

	private Logger logger = Logger.getLogger(SuspectedFraudDaoImpl.class);

	@Override
	public SuspectedFraud getByAccountOrCard(String accountOrCard) {
		logger.debug("SuspectedFraud.:" + accountOrCard);
		SuspectedFraud suspectedFraud = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(SuspectedFraud.class);
			criteria.add(Restrictions.eq("accountOrCard", accountOrCard));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			suspectedFraud = (SuspectedFraud) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("loading SuspectedFraud .:");
			logger.error("Reason.:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			session.close();
		}
		return suspectedFraud;
	}

}
