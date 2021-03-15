package com.ceva.bank.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.OTPGetDao;
import com.ceva.db.model.Service;

@Repository("otpGetDao")
public class OTPGetDaoImpl implements OTPGetDao{

	Logger log = Logger.getLogger(OTPGetDaoImpl.class);

	private static final String otpQuery =  ResourceBundle.getBundle("config").getString("otp.get.query");
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<String> getOTP(String userId) {
		
		Session session = null;
		String otp=null;
		List<String> rows= new ArrayList<String>();
		try{
			session = sessionFactory.openSession();
			String otpHql = otpQuery;
			SQLQuery otpQuery = session.createSQLQuery(otpHql);
	        otpQuery.setParameter("userid", userId);
	        otpQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        //otp = (String) otpQuery.uniqueResult();
	        rows = otpQuery.list();
		}catch(Exception e){
			log.error("Error while getting service from db..:" + e.getLocalizedMessage());
		}finally {
			session.close();
		}
		return rows;
	}

}
