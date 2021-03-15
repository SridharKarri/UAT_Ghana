package com.ceva.bank.common.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Product;
import com.ceva.db.model.ServiceProducts;
import com.ceva.db.model.Store;
import com.ceva.db.model.SuperAgent;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

@Repository("merchantDao")
public class MerchantDaoImpl implements MerchantDao{
	Logger log = Logger.getLogger(MerchantDaoImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Merchant getById(String merchantId) {
		log.debug("getting merchant from database..:START");
		Criteria criteria = null;
		Merchant merchant = null;
		Session session=null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Merchant.class);
			criteria.add(Restrictions.eq("merchantID", merchantId));
			criteria.add(Restrictions.eq("Status", "A"));
			merchant = (Merchant) criteria.uniqueResult();
			//log.debug("merchant.." + merchant.toString());

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Error while loading..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.debug("getting merchant from database..:END");
		return merchant;
	}


	@Override
	public Product getProducts(Long id) {
		log.debug("getting ServiceProducts from database..:START");
		Criteria criteria = null;
		Product bean = null;
		Session session=null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Product.class);
			criteria.add(Restrictions.eq("id", id));
			bean = (Product) criteria.uniqueResult();

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Error while loading otp..:" + e.getLocalizedMessage());
		} finally {
			bean = null;
			criteria = null;
			session.close();
		}
		log.debug("getting fee from database..:END");
		return bean;
	}


	@Override
	public List<Merchant> getMerchants(String property, String value) {
		log.debug("getting merchants from database..:START");
		Criteria criteria = null;
		List<Merchant> merchants = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Merchant.class);
			criteria.add(Restrictions.eq(property, value));
			merchants = (List<Merchant>)criteria.list();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Error while loading merchatns ..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.debug("getting merchants from database..:END");
		return merchants;
	}


	@Override
	public Store getStoreById(String Id) {
		log.debug("getting Store from database..:START"+Id);
		Criteria criteria = null;
		Store store = null;
		Session session=null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Store.class);
			criteria.add(Restrictions.eq("storeId", Id));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			store = (Store) criteria.uniqueResult();
			log.debug("Store.." + store.toString());

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Error while loading store..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.debug("getting Store from database..:END");
		return store;
	}
	
	@Override
	public ResponseDTO getServiceProducts(String serviceId, String productId) {
		log.debug("getting ServiceProducts from database..:START");
		ResponseDTO dto = new ResponseDTO();
		Criteria criteria = null;
		ServiceProducts bean = null;
		Session session =null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(ServiceProducts.class);
			criteria.add(Restrictions.eq("pid", new Long(productId)));
			criteria.add(Restrictions.eq("sid", new Long(serviceId)));
			bean = (ServiceProducts) criteria.uniqueResult();
			if (bean != null) {
				log.debug(bean.toString());
				dto.setData(bean);
				dto.setMessage(Constants.SUCESS_CAPS);
			}
			log.debug("dto.." + dto.toString());

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Error while loading ServiceProducts..:" + e.getLocalizedMessage());
		} finally {
			bean = null;
			criteria = null;
			session.close();
		}
		log.debug("getting fee from database..:END");
		return dto;
	}


	@Override
	public SuperAgent getSuperAgentById(String id) {
		log.debug("getting SuperAgent from database..:"+id);
		Criteria criteria = null;
		SuperAgent superAgent= null;
		Session session=null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(SuperAgent.class);
			criteria.add(Restrictions.eq("superAgentId", id));
			criteria.add(Restrictions.eq("status", "A"));
			superAgent = (SuperAgent) criteria.uniqueResult();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Error while Saving loading..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.debug("getting superAgent from database..:END");
		return superAgent;
	}

}
