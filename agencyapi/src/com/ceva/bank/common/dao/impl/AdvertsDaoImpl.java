package com.ceva.bank.common.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.bank.common.dao.AdvertsDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.Adverts;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.UserLoginCredentials;
import com.nbk.util.Constants;

@Repository("advertsDao")
public class AdvertsDaoImpl implements AdvertsDao {

	private Logger logger = Logger.getLogger(AdvertsDao.class);
	
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	LoginDao loginDao;

	@Autowired
	MerchantDao merchantDao;

	@Override
	public List<Adverts> listAdverts(BalanceEnquirey enquirey) {
		logger.info("loading Adverts for user.:" + enquirey.getUserId());
		List<Adverts> Adverts = new ArrayList<Adverts>();
		UserLoginCredentials credentials = null;
		Merchant merchant = null;
		try {
			credentials = loginDao.getById(enquirey.getUserId());
			merchant = merchantDao.getById(credentials.getAgent());
			logger.info("load specific images for day");
			Adverts.add(getImage(merchant.getProduct(), "HOME"));
			Adverts.add(getImage(merchant.getProduct(), "FOOTER"));
			logger.info(Adverts.size());
		} catch (Exception e) {
			logger.error("loading Adverts for user.:");
			logger.error("Reason.:" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return Adverts;
	}

	@Override
	public boolean update(Adverts add) {
		boolean isUpdated = false;
		logger.debug("=============== updateStatus START===============");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.merge(add);
			session.flush();
			isUpdated = true;
		} catch (Exception e) {
			isUpdated = false;
			logger.error("updating Adverts for user.:");
			logger.error("Reason.:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		logger.debug("=============== updateStatus END===============");
		return isUpdated;
	}

	@Override
	public Adverts getById(long id) {
		logger.info("loading Adverts for .:" + id);
		Adverts Adverts = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Adverts.class);
			criteria.add(Restrictions.eq("id", id));
			Adverts = (Adverts) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("loading Adverts for user.:");
			logger.error("Reason.:" + e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			session.close();
		}
		return Adverts;
	}

	public Adverts getImage(String productId, String typeOfImage) {

		logger.info("load specific images of type" + typeOfImage);
		Adverts Adverts = null;
		Criteria criteria = null;
		Criteria criteria1 = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Adverts.class);
			criteria.add(Restrictions.eq("productId", productId));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			criteria.add(Restrictions.eq("imgLoc", typeOfImage));
			criteria.add(Restrictions.eq("imgType", "P"));
			criteria.add(Restrictions.sqlRestriction("TRUNC(fromDate) >= TO_DATE('"+ new SimpleDateFormat(Constants.DATE_PATTERN).format(new Date()) + "','"+ Constants.DATE_PATTERN + "')"));
			criteria.add(Restrictions.sqlRestriction("TRUNC(toDate) <= TO_DATE('"+ new SimpleDateFormat(Constants.DATE_PATTERN).format(new Date()) + "','"+ Constants.DATE_PATTERN + "')"));
			criteria.setProjection(
					Projections
							.projectionList()
							.add(Projections.property("id"), "id")
							.add(Projections.property("name"), "name")
							.add(Projections.property("url"), "url")
							.add(Projections.property("imgLoc"), "imgLoc"))
							.setResultTransformer(Transformers.aliasToBean(Adverts.class));
			Adverts = (Adverts) criteria.uniqueResult();
			if (Adverts == null) {
				logger.info("no specific images for today loading default images");
				criteria1 = session.createCriteria(Adverts.class);
				criteria1.add(Restrictions.eq("productId", productId));
				criteria1.add(Restrictions.eq("status", Status.A.toString()));
				criteria1.add(Restrictions.eq("imgLoc", typeOfImage));
				criteria1.add(Restrictions.eq("imgType", "D"));
				criteria1.setProjection(
						Projections
								.projectionList()
								.add(Projections.property("id"), "id")
								.add(Projections.property("name"), "name")
								.add(Projections.property("url"), "url")
								.add(Projections.property("imgLoc"),"imgLoc"))
								.setResultTransformer(Transformers.aliasToBean(Adverts.class));
				Adverts = (Adverts) criteria1.uniqueResult();
			}
		} catch (Exception e) {
			logger.error("loading Adverts for product.:");
			logger.error("Reason.:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			criteria1 = null;
			criteria = null;
			session.clear();
			session.close();
		}
		return Adverts;
	}

}
