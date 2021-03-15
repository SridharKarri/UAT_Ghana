package com.ceva.bank.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.ReversalDao;
import com.ceva.db.model.Reversals;

@Repository("reversalDao")
public class ReversalDaoImple implements ReversalDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Long save(Reversals reversals) {
		long id;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			id = (Long) session.save(reversals);
			session.flush();
		}catch(Exception e){
			id = 0;
			e.printStackTrace();
		}finally{
			session.close();
			session = null;
		}
		return id;
	}

	@Override
	public Reversals getByRefNumber(String refNumber) {
	    Session session= null;
	    try
	    {
	      session = sessionFactory.openSession();
	      Criteria criteria = session.createCriteria(Reversals.class);
	      criteria.add(Restrictions.eq("refNumber", refNumber));
	      criteria.add(Restrictions.eq("status", "A"));
	      return (Reversals) criteria.uniqueResult();
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	return null;
	    }finally{
	    	session.close();
	    }
	  }

	@Override
	public void update(Reversals reversals) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.update(reversals);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	@Override
	public List<Reversals> getByStatus(String status) {
		List<Reversals> reversals = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query sql = session.createQuery("Select a from Reversals a where a.status=:status");
			sql.setParameter("status", status);
			sql.setMaxResults(75);
			reversals = sql.list();
			reversals = updateStatatus(reversals);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			session = null;
		}
		return reversals;
	}

	private List<Reversals> updateStatatus(List<Reversals> alerts) {
		Session session = null;
		List<Reversals> newList=  new ArrayList<Reversals>();
		Query query = null;
		try {
			session = sessionFactory.openSession();
			for(Reversals alert:alerts){
				alert.setStatus("I");
				newList.add(alert);
				String qryString = "update Reversals a set a.status='I' where a.id=:id";
		        query = session.createQuery(qryString);
		        query.setParameter("id", alert.getId());
		        query.executeUpdate();
			}
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			alerts = null;
			session.close();
			query=null;
		}
		return newList;
	}
}
