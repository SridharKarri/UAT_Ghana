package com.ceva.bank.common.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.StateDao;
import com.ceva.db.model.City;
import com.ceva.db.model.States;

@Repository("stateDao")
public class StateDaoImpl implements StateDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	Logger log = Logger.getLogger(StateDao.class);

	@Override
	public List<States> listStates() {
		log.info("getting States from database..:START");
		Criteria criteria = null;
		List<States> states = null;
		Session session=null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(States.class);
			log.debug("Criteria created..");
			states = criteria.list();
			log.debug("executed Query..");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving loading..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
		}
		log.info("getting States from database..:END");
		return states;
	}

	@Override
	public List<City> listCities(String stateid) {
		log.info("getting City from database..:START");
		Criteria criteria = null;
		List<City> cities = null;
		Session session=null;
		try {
			log.debug("creating session for merchant");
			session = sessionFactory.openSession();
			criteria = session.createCriteria(City.class);
			/*if(stateid !=null && stateid != "" && stateid != "0"){
			criteria.add(Restrictions.eq("stateId", stateid));	
			}*/
			cities = criteria.list();
			log.debug("executed Query..");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving loading..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
		}
		log.info("getting City from database..:END");
		return cities;
	}

}
