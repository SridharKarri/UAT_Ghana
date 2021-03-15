package com.ceva.bank.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.FinacleDao;
import com.ceva.db.model.TransferRequest;

@Repository("finacleDao")
public class FinacleDaoImpl implements FinacleDao {

	private Logger log = Logger.getLogger(FinacleDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public long save(TransferRequest request) {
		log.info("logginn transaction in table");
		long id = 0;
		try{
			Session	session = sessionFactory.openSession();
			id = (Long) session.save(request);
			session.flush();
			session.close();
			log.info("trnsaction inserted successfully");
		}catch(Exception e){
			log.error("Error while saving transaction into database.");
			log.error("Reason..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public long update(TransferRequest request) {
		log.info("updateing transaction in table");
		long id = 0;
		try{
			Session	session = sessionFactory.openSession();
			session.merge(request);
			session.flush();
			session.close();
			id =1;
			log.info("trnsaction inserted successfully");
		}catch(Exception e){
			log.error("Error while updateing transaction into database.");
			log.error("Reason..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public TransferRequest getById(long id) {
		Session	session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(TransferRequest.class);
			criteria.add(Restrictions.eq("id", id));
			return (TransferRequest) criteria.uniqueResult();
		}catch(Exception e){
			log.error("Error while loading transfers from db.");
			log.error("Reason..:"+e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}


	}

}
