package com.ceva.bank.common.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.db.model.Alert;
import com.ceva.db.model.DailyReport;

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
			criteria = session.createCriteria(Alert.class);
			criteria.add(Restrictions.eq("appl", applCode));
			criteria.add(Restrictions.eq("fetchStatus", "P"));
			criteria.setMaxResults(10);
			alerts = criteria.list();
		} catch (Exception e) {
			log.error("Error while loading txn pin records");
			log.error("Reason ..:" + e.getLocalizedMessage());
			e.printStackTrace();
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
			session.beginTransaction();
			session.merge(alert);
			session.getTransaction().commit();
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
			session.beginTransaction();
			alert.setFetchStatus("P");
			alert.setMessageDate(new Date());
			alert.setId(System.nanoTime()+"");
			alert.setRetryCount(0);
			session.save(alert);
			session.getTransaction().commit();
			session.flush();
		}catch(Exception e){
			log.error("Error Occured while save alert..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		log.debug("alert notification inserted, wil be triggered from pooler later."+alert.getId());
	}

	@Override
	public DailyReport getDailyReport() {
		DailyReport dailyReport = null;
		Criteria criteria = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(DailyReport.class);
			criteria.add(Restrictions.eq("status", "P"));
			criteria.setMaxResults(1);
			dailyReport = (DailyReport) criteria.uniqueResult();
		} catch (Exception e) {
			log.error("Error while loading txn pin records");
			log.error("Reason ..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			session.close();
			criteria = null;
			session = null;
		}
		return dailyReport;
	}

	@Override
	public void updateDailyReport(DailyReport dailyReport) {
		Session session = null;
		try {
			log.debug("updating dailyReport in DB :" + dailyReport.getId());
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.merge(dailyReport);
			session.getTransaction().commit();
			session.flush();
			log.debug("updation dailyReport status done:");
		} catch (Exception e) {
			log.error("Error while updating dailyReport in DB");
			log.error("Reason ..:" + e.getLocalizedMessage());
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DailyReport> getDailyReports() {
		List<DailyReport> dailyReports = null;
		Criteria criteria = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(DailyReport.class);
			criteria.add(Restrictions.eq("status", "P"));
			criteria.setMaxResults(1);
			dailyReports = (List<DailyReport>) criteria.list();
			
			for (DailyReport dailyReport : dailyReports) {
				dailyReport.getMerchantId();
				dailyReport.setStatus("I");
				session.update(dailyReport);
				session.flush();
			}
		} catch (Exception e) {
			log.error("Error while loading txn pin records");
			log.error("Reason ..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			session.close();
			criteria = null;
			session = null;
		}
		return dailyReports;
	}

	@Override
	public List<Alert> getAllUserPendingEmail(String transactionType) {
		List<Alert> alerts = null;
		Criteria criteria = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Alert.class);
			criteria.add(Restrictions.in("txnType", (Object[])transactionType.split(",")));
			criteria.add(Restrictions.eq("fetchStatus", "P"));
			criteria.addOrder(Order.asc("messageDate"));
			criteria.setMaxResults(10);
			alerts = criteria.list();
		} catch (Exception e) {
			log.error("Error while getPinEmail");
			log.error("Reason ..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			session.close();
			criteria = null;
			session = null;
		}
		return alerts;
	}

}
