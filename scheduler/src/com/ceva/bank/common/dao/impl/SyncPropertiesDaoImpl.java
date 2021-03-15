package com.ceva.bank.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.SyncPropertiesDao;
import com.ceva.db.model.SyncProperties;

@Repository("syncPropertiesDao")
public class SyncPropertiesDaoImpl implements SyncPropertiesDao {
	Logger log =Logger.getLogger(SyncPropertiesDaoImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public SyncProperties getByKey(String key) {
		log.info("getting SyncProperties from database..:START");
		SyncProperties properties= null;
		Session session= null;
		try{
			session = sessionFactory.openSession();
			properties = (SyncProperties) session.createCriteria(SyncProperties.class)
					.add(Restrictions.eq("propertyName", key)).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while SyncProperties..:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("getting SyncProperties from database..:END");
		return properties;
	}

	@Override
	public Boolean update(SyncProperties properties) {
		log.info("SyncProperties UPDATE START");
		boolean inserted = false;
		Session session= null;
		try{
			session=sessionFactory.openSession();
			session.saveOrUpdate(properties);
			session.flush();
			inserted= true;
		}catch(Exception e){
			inserted = false;
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("SyncProperties UPDATE END");
		return inserted;
	}

}
