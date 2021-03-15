package com.ceva.bank.common.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.LoginDao;
import com.ceva.db.model.UserLoginCredentials;

@Repository("loginDao")
public class LoginDaoImpl implements LoginDao {
	Logger log = Logger.getLogger(LoginDaoImpl.class);
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public UserLoginCredentials getById(String userId) {
		UserLoginCredentials credentials = null;
		Session session = null;
		Criteria criteria = null;
		try{
			session = sessionFactory.openSession();
			criteria = session.createCriteria(UserLoginCredentials.class);
			criteria.add(Restrictions.eq("userId", userId));
			criteria.add(Restrictions.eq("status", "A"));
			credentials = (UserLoginCredentials) criteria.uniqueResult();
		}catch(Exception e){
			log.error("Error while loading user..:");
			e.printStackTrace();
			log.error("Reason.:"+e.getLocalizedMessage());
		}finally{
			criteria=null;
			session.close();
		}
		return credentials;
	}

}
