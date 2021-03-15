package com.ceva.bank.common.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.VisitShopDao;
import com.ceva.db.model.VisitShop;

@Repository("visitShopDao")
public class VisitShopDaoImpl implements VisitShopDao {

	@Autowired
	SessionFactory sessionFactory;
	Logger log = Logger.getLogger(VisitShopDao.class);

	@Override
	public VisitShop getById(long id) {
		log.debug("getting VisitShop from database..:START");
		Criteria criteria = null;
		VisitShop visitShop = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(VisitShop.class);
			criteria.add(Restrictions.eq("id", id));
			visitShop = (VisitShop) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving loading..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
		}
		log.debug("getting VisitShop from database..:END");
		return visitShop;
	}

	@Override
	public List<VisitShop> listAll(String userId) {
		log.debug("getting listAll from database..:START");
		Criteria criteria = null;
		List<VisitShop> visitShops = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(VisitShop.class);
			visitShops = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving loading..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
		}
		log.debug("getting listAll from database..:END");
		return visitShops;
	}

	@Override
	public Long save(VisitShop visitShop) {
		long id;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			id = (long) session.save(visitShop);
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

	@Override
	public boolean update(VisitShop visitShop) {
		boolean saved = false;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.update(visitShop);
			saved = true;
		} catch (Exception e) {
			saved = false;
			log.error("cause.:" + e.getLocalizedMessage());
		}finally {
			session.close();
			session = null;
		}
		return saved;
	}

}
