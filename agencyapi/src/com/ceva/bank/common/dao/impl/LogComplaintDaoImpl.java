package com.ceva.bank.common.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.LogComplaintDao;
import com.ceva.db.model.LogComplaint;

@Repository("logComplaintDao")
public class LogComplaintDaoImpl implements LogComplaintDao{

	@Autowired
	SessionFactory sessionFactory;
	
	Logger log = Logger.getLogger(LogComplaintDao.class);

	@Override
	public LogComplaint getById(long id) {
		log.debug("LogComplaint from database..:START");
		Criteria criteria = null;
		LogComplaint logComplaint = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(LogComplaint.class);
			criteria.add(Restrictions.eq("id", id));
			logComplaint = (LogComplaint) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Loging Complaint..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
		}
		log.debug("LogComplaint from database..:END");
		return logComplaint;
	}

	@Override
	public List<LogComplaint> listAll(String userId) {
		log.debug("list latest 10 from database..:START");
		Criteria criteria = null;
		List<LogComplaint> complaints = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(LogComplaint.class);
			criteria.addOrder(Order.desc("id"));
			criteria.setMaxResults(10);
			complaints = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving loading..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
		}
		log.debug("listAll from database..:END");
		return complaints;
	}

	@Override
	public Long save(LogComplaint complaint) {
		long id;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			id = (long) session.save(complaint);
			session.flush();
		} catch (Exception e) {
			id = 0;
			log.error("cause.:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			session.close();
			session = null;
		}
		return id;
	}

}
