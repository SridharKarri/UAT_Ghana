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
import com.ceva.db.model.Merchant;
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
		log.info("getting merchant from database..:START");
		Criteria criteria = null;
		Merchant merchant = null;
		Session session = null;
		try {
			log.debug("creating session for merchant");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(Merchant.class);
			criteria.add(Restrictions.eq("merchantID", merchantId));
			criteria.add(Restrictions.eq("Status", "A"));
			log.debug("Criteria created..");
			merchant = (Merchant) criteria.uniqueResult();
			log.debug("executed Query..");

			log.debug("merchant.." + merchant.toString());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving otp..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.info("getting merchant from database..:END");
		return merchant;
	}


	@Override
	public ResponseDTO getServiceProducts(String serviceId, String productId) {
		log.info("getting ServiceProducts from database..:START");
		ResponseDTO dto = new ResponseDTO();
		Criteria criteria = null;
		ServiceProducts bean = null;
		Session session =null;
		try {
			log.debug("creating session for ServiceProducts");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(ServiceProducts.class);
			criteria.add(Restrictions.eq("pid", new Long(productId)));
			criteria.add(Restrictions.eq("sid", new Long(serviceId)));
			log.debug("Criteria created..");
			bean = (ServiceProducts) criteria.uniqueResult();
			log.debug("executed Query..");
			if (bean != null) {
				log.debug(bean.toString());
				dto.setData(bean);
				dto.setMessage(Constants.SUCESS_CAPS);
			}
			log.debug("dto.." + dto.toString());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving otp..:" + e.getLocalizedMessage());
		} finally {
			bean = null;
			criteria = null;
			session.close();
		}
		log.info("getting fee from database..:END");
		return dto;
	}

	@Override
	public List<Merchant> getAgents() {
		log.info("getting merchants from database..:START");
		Criteria criteria = null;
		List<Merchant> merchants = null;
		Session session = null;
		try {
			log.debug("creating session for merchants");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(Merchant.class);
			log.debug("Criteria created..");
			merchants = criteria.list();
			log.debug("executed Query..");

			log.debug("merchant.." + merchants.size());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while loading merchant..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		return merchants;
	}


	@Override
	public List<SuperAgent> getSuperAgents() {
		log.info("getting SuperAgents from database..:START");
		Criteria criteria = null;
		List<SuperAgent> merchants = null;
		Session session = null;
		try {
			log.debug("creating session for SuperAgents");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(SuperAgent.class);
			log.debug("Criteria created..");
			merchants = criteria.list();
			log.debug("executed Query..");

			log.debug("SuperAgents.." + merchants.size());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while loading SuperAgent..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		return merchants;
	}


	@Override
	public SuperAgent superAgentById(String id) {
		log.info("getting superAgentBy from database..:START");
		Criteria criteria = null;
		SuperAgent superAgent= null;
		Session session = null;
		try {
			log.debug("creating session for superAgent");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(SuperAgent.class);
			criteria.add(Restrictions.eq("id", Long.parseLong(id)));
			log.debug("Criteria created..");
			superAgent = (SuperAgent) criteria.uniqueResult();
			log.debug("executed Query..");

			log.debug("superAgent.." + superAgent.toString());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while superAgentBy..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.info("getting superAgent from database..:END");
		return superAgent;
	}
	
	@Override
	public SuperAgent superAgentByName(String accountName) {
		log.info("getting superAgentBy from database..:START");
		Criteria criteria = null;
		SuperAgent superAgent= null;
		Session session = null;
		try {
			log.debug("creating session for superAgent");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(SuperAgent.class);
			criteria.add(Restrictions.eq("accountName", accountName));
			log.debug("Criteria created..");
			superAgent = (SuperAgent) criteria.uniqueResult();
			log.debug("executed Query..");

			log.debug("superAgent.." + superAgent.toString());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while superAgentBy..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.info("getting superAgent from database..:END");
		return superAgent;
	}


	@Override
	public List<Store> getStores() {
		log.info("getting stores from database..:START");
		Criteria criteria = null;
		List<Store> stores= null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Store.class);
			stores = criteria.list();
			log.debug("stores.." + stores.size());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while loading stores..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		return stores;
	}


	@Override
	public Store getStoreById(String id) {
		log.info("getting Store from database..:START");
		Criteria criteria = null;
		Store store = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Store.class);
			criteria.add(Restrictions.eq("storeId", id));
			criteria.add(Restrictions.eq("status", "A"));
			store = (Store) criteria.uniqueResult();
			log.debug("merchant.." + store.toString());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while Saving otp..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		log.info("getting merchant from database..:END");
		return store;
	}

}
