package com.ceva.bank.common.dao.impl;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.AccountDao;
import com.ceva.db.model.AccountBean;
import com.ceva.db.model.AccountImage;

@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {

	Logger log = Logger.getLogger(AccountDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public AccountImage getById(Long id) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			return (AccountImage)session.get(AccountImage.class, id);
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
		//return (AccountImage) sessionFactory.getCurrentSession().get(AccountImage.class, id);
	}

	@Override
	public AccountBean getByKey(Long id) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			return (AccountBean)session.get(AccountBean.class, id);
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
		//return (AccountBean) sessionFactory.getCurrentSession().get(AccountBean.class, id);
	}

	@Override
	public List<AccountBean> listAll() {
		return null;
	}

	@Override
	public boolean save(AccountBean accountBean) {
		log.info("inserting account for user.."+accountBean.getId());
		Session session = null;
		boolean isInserted =false;
		try{
			session=sessionFactory.openSession();
			session.save(accountBean);
			session.flush();
			isInserted = true;
		}catch(Exception e){
			isInserted = false;
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		return isInserted;
	}

	@Override
	public boolean update(AccountBean accountBean) {
		log.info("updating account for user.."+accountBean.getId());
		Session session = null;
		boolean isInserted =false;
		try{
			session=sessionFactory.openSession();
			session.saveOrUpdate(accountBean);
			session.flush();
			isInserted = true;
		}catch(Exception e){
			isInserted = false;
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		return isInserted;
	}

	@Override
	public boolean getByPhone(String phone) {
		log.info("getByPhone..:"+phone);
		Session session = null;
		boolean hasRecord =false;
		try{
			session=sessionFactory.openSession();
			Criteria criteria = session.createCriteria(AccountBean.class);
			criteria.add(Restrictions.eq("phone", phone));
			criteria.add(Restrictions.eq("status", "SUCCESS"));
			if(criteria.list().size()>0){
				hasRecord = true;
			}
		}catch(NonUniqueResultException nure){
			hasRecord = true;
			log.error("Error Occured..:"+nure.getLocalizedMessage());
			nure.printStackTrace();
		}catch(Exception e){
			hasRecord = false;
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		return hasRecord;
	}

	@Override
	public Long save(AccountImage accountImage) {
		log.info("inserting image for user.."+accountImage.getId());
		Session session = null;
		Long id = new Long(0);
		try{
			session=sessionFactory.openSession();
			id = (Long) session.save(accountImage);
			session.flush();
		}catch(Exception e){
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		return id;
	}

	@Override
	public boolean update(AccountImage accountImage) {
		log.info("updating idimage for user.."+accountImage.getId());
		Session session = null;
		boolean isInserted =false;
		try{
			session=sessionFactory.openSession();
			session.saveOrUpdate(accountImage);
			session.flush();
			isInserted = true;
		}catch(Exception e){
			isInserted = false;
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally {
			session.close();
		}
		return isInserted;
	}

}
