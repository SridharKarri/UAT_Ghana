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

import com.ceva.bank.common.dao.NotificationDao;
import com.ceva.db.model.Notification;

@Repository("notificationDao")
public class NotificationDaoImpl implements NotificationDao {

	Logger log = Logger.getLogger(NotificationDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Notification> getAll(String applCode) {
		List<Notification> notifications = null;
		Criteria criteria = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Notification.class);
			criteria.add(Restrictions.eq("appl", applCode));
			criteria.add(Restrictions.eq("fetchStatus", "P"));
			criteria.add(Restrictions.le("retryCount", 3));
			notifications = criteria.list();
		} catch (Exception e) {
			log.error("Error while loading notifications");
			log.error("Reason ..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			session.close();
			criteria = null;
			session = null;
		}
		return notifications;
	}

	@Override
	public void update(Notification alert) {
		Session session = null;
		try {
			log.debug("updating notification in DB :" + alert.getId());
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.merge(alert);
			session.getTransaction().commit();
			session.flush();
			log.debug("updating notification in DB done:" + alert.getId());
		} catch (Exception e) {
			log.error("Error while updating  txn pin in db");
			log.error("Reason ..:" + e.getLocalizedMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void save(Notification alert) {
		log.debug("inserting notification for user.."+alert.getTxnType());
		Session session = null;
		try{
			session=sessionFactory.openSession();
			session.beginTransaction();
			alert.setFetchStatus("P");
			alert.setMessageDate(new Date());
			alert.setId(System.currentTimeMillis()+"");
			alert.setRetryCount(0);
			session.save(alert);
			session.getTransaction().commit();
			session.flush();
		}catch(Exception e){
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		log.debug("alert notification inserted, wil be triggered from pooler later."+alert.getId());
	}

}
