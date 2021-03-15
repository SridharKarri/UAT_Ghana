package com.ceva.bank.common.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.BillPaymentDao;
import com.ceva.db.model.BillPayment;
import com.ceva.db.model.BillerCategory;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.PackageFormItem;

@Repository("billPaymentDao")
public class BillPaymentDaoImpl implements BillPaymentDao {

	Logger logger = Logger.getLogger(BillPaymentDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Billers> getBillers() {
		logger.info("loading billers from db.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("status", "A"));
			return criteria.list();
		} catch (Exception e) {
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public List<Billers> getBillers(long serviceId) {
		logger.info("loading billers from db.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("sid", serviceId));
			criteria.add(Restrictions.eq("status", "A"));
			return criteria.list();
		} catch (Exception e) {
			logger.error("Error while loading billers.");
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public Billers getBiller(String billerId) {
		logger.info("loading biller from db with billerId." + billerId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("billerId", billerId));
			criteria.add(Restrictions.eq("status", "A"));
			return (Billers) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while loading billers.");
			e.printStackTrace();
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public Billers getBiller(long id) {
		logger.info("loading biller from db with id." + id);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("id", Long.valueOf(id)));
			criteria.add(Restrictions.eq("status", "A"));
			return (Billers) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while loading billers.");
			e.printStackTrace();
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public List<BillerPackages> getBillerPackages(String billerId) {
		logger.info("loading biller packages from db with billerId." + billerId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerPackages.class);
			criteria.add(Restrictions.eq("billerId", billerId));
			return criteria.list();
		} catch (Exception e) {
			logger.error("Error while loading billers.");
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public List<BillPayment> getBillPayment(String status, Date date) {
		logger.info("loading BillPayments from db with status.:" + status);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillPayment.class);
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("txndate", date));
			criteria.add(Restrictions.le("retryCount", new Long(2L)));
			return criteria.list();
		} catch (Exception e) {
			logger.error("Error while loading billers.");
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public void update(BillPayment payment) {
		logger.info("update billpayment in db.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.merge(payment);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Error While update billpayment..:" + e.getLocalizedMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(BillerCategory category) {
		logger.info("update BillerCategory in db.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.merge(category);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Error While update billpayment..:" + e.getLocalizedMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public BillerCategory getCategorybyId(String id) {
		logger.info("loading Category from db with Id." + id);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerCategory.class);
			criteria.add(Restrictions.eq("catId", id));
			return (BillerCategory)criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while loading billers.");
			e.printStackTrace();
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public void update(Billers biller) {
		logger.info("update Biller in db.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.merge(biller);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Error While update Biller..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public BillerPackages getBillerPackagbyId(String id) {
		logger.info("loading BillerPackage from db with pack Id." + id);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerPackages.class);
			criteria.add(Restrictions.eq("packId", id));
			return (BillerPackages)criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while loading billers.");
			e.printStackTrace();
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public List<BillerCategory> getBillerCategories() {
		logger.info("loading Biller Categories from db with billerId.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerCategory.class);
			return criteria.list();
		} catch (Exception e) {
			logger.error("Error while loading Biller Category.");
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public void update(BillerPackages packages) {
		logger.info("update BillerPackages in db.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.merge(packages);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Error While update BillerPackages..:" + e.getLocalizedMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public List<BillerPackages> getBillerPackages() {
		logger.info("loading Biller Packages from db .");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerPackages.class);
			return criteria.list();
		} catch (Exception e) {
			logger.error("Error while loading Biller Packages.");
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}

	}

	public void update(PackageFormItem formItem) {
		logger.info("update PackageFormItem in db.");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.merge(formItem);
			session.flush();
			session.clear();
		} catch (Exception e) {
			logger.error("Error While update BillerPackages..:" + e.getLocalizedMessage());
		} finally {
			session.close();
		} 
	}

	@Override
	public PackageFormItem getPackageFormItem(Long id) {
		logger.info("loading PackageFormItem from db with pack Id." + id);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(PackageFormItem.class);
			criteria.add(Restrictions.eq("packId", id));
			return (PackageFormItem)criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("Error while loading PackageFormItem.");
			logger.error("Reason..:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		} 
	}

}
