package com.ceva.bank.common.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.db.model.Alert;

@Repository("alertDao")
public class AlertDaoImpl implements AlertDao {

	Logger log = Logger.getLogger(AlertDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Alert> getAll(String applCode) {
		List<Alert> alerts = null;
		Criteria criteria = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(
					Alert.class);
			//criteria.add(Restrictions.eq("txnType", txnType));
			criteria.add(Restrictions.eq("appl", applCode));
			criteria.add(Restrictions.eq("fetchStatus", "P"));
			alerts = criteria.list();
		} catch (Exception e) {
			log.error("Error while loading txn pin records");
			log.error("Reason ..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
			session = null;
		}
		return alerts;
	}

	@Override
	public void update(Alert alert) {
		Session session = null;
		try {
			log.debug("updating sms in DB :" + alert.getId());
			session = sessionFactory.openSession();
			session.merge(alert);
			session.flush();
			log.debug("updation sms status in db done :" + alert.getId());
		} catch (Exception e) {
			log.error("Error while updating  txn pin in db");
			log.error("Reason ..:" + e.getLocalizedMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void save(Alert alert) {
		log.debug("inserting notification for user.."+alert.getTxnType());
		Session session = null;
		try{
			session=sessionFactory.openSession();
			alert.setFetchStatus("P");
			alert.setMessageDate(new Date());
			alert.setId(System.currentTimeMillis()+"");
			alert.setRetryCount(0);
			session.save(alert);
			session.flush();
		}catch(Exception e){
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		log.info("alert notification inserted, wil be triggered from pooler later."+alert.getId());
	}

}
