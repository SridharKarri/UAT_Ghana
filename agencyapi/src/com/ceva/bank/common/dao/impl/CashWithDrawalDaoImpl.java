package com.ceva.bank.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.CashWithDrawalDao;
import com.ceva.db.model.Cashwithdrawal;

@Repository("cashWithDrawalDao")
public class CashWithDrawalDaoImpl implements CashWithDrawalDao {

	Logger log = Logger.getLogger(CashWithDrawalDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;;

	@Override
	public long save(Cashwithdrawal cashwithdrawal) {
		log.debug("saving cash withdrawal service");
		long id = 0;
		Session session=null;
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
			id = (Long) session.save(cashwithdrawal);
			session.getTransaction().commit();
			session.flush();
		}catch(Exception e){
			log.error("Error While saving cashwithdrawal txn");
			log.error("Reason..:"+e.getLocalizedMessage());
		}finally{
			session.close();
			session = null;
		}
		return id;
	}

	@Override
	public Cashwithdrawal update(Cashwithdrawal cashwithdrawal) {
		log.debug("updating cash withdrawal service");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			return (Cashwithdrawal) session.merge(cashwithdrawal);
		}catch(Exception e){
			log.error("Error While updating cashwithdrawal txn");
			log.error("Reason..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
			session = null;
		}
	}

	@Override
	public Cashwithdrawal getById(long ref) {
		log.debug("cash withdrawal txn loading from db");
		Session session= null;
		Criteria criteria = null;
		try{
			session=sessionFactory.openSession();
			criteria = session.createCriteria(Cashwithdrawal.class);
			criteria.add(Restrictions.eq("id", ref));
			return (Cashwithdrawal) criteria.uniqueResult();
		}catch(Exception e){
			log.error("error while loading transaction..");
			return null;
		}finally{
			session.close();
		}

	}

}
