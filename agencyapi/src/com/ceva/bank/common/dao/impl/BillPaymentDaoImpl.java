package com.ceva.bank.common.dao.impl;

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

import com.ceva.bank.common.beans.Services;
import com.ceva.bank.common.dao.BillPaymentDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.BillerCategory;
import com.ceva.db.model.BillerMakets;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.QuickTellerTxnLog;
import com.ceva.db.model.Service;
import com.nbk.util.ConfigLoader;
@Repository("billPaymentDao")
public class BillPaymentDaoImpl implements BillPaymentDao {

	Logger logger = Logger.getLogger(BillPaymentDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Billers> getBillers() {
		logger.debug("loading billers from db.");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			logger.debug("session created.");
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("status", "A"));

			logger.debug("criteria created and executing.");
			return criteria.list();
		}catch(Exception e){
			//e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
		
	}

	@Override
	public List<Billers> getBillers(long serviceId) {
		logger.debug("loading billers from db.");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			logger.debug("session created.");
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("sid", serviceId));
			criteria.add(Restrictions.eq("status", "A"));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("id"),"id")
					.add(Projections.property("acountNumber"),"acountNumber")
					.add(Projections.property("billerId"),"billerId")
					.add(Projections.property("billerName"),"billerName")
					.add(Projections.property("address"),"customerField")
					.add(Projections.property("billerDesc"),"billerDesc")
					.add(Projections.property("channel"),"channel"));
			criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			logger.debug("criteria created and executing.");
			return criteria.list();
		}catch(Exception e){
			//e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public Billers getBiller(long id) {
		logger.debug("loading biller from db with id."+id);
		Session session = null;
		try{
			session = sessionFactory.openSession();
			logger.debug("session created.");
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("id", id));
			criteria.add(Restrictions.eq("status", "A"));
			logger.debug("criteria created and executing.");
			return (Billers) criteria.list().get(0);
		}catch(Exception e){
			//e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public List<BillerPackages> getBillerPackages(long billerId) {
		logger.debug("loading biller packages from db with billerId."+billerId);
		Session session = null;
		try{
			session = sessionFactory.openSession();
			logger.debug("session created.");
			Criteria criteria = session.createCriteria(BillerPackages.class);
			criteria.add(Restrictions.eq("billerId", billerId+""));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			logger.debug("criteria created and executing.");
			return criteria.list();
		}catch(Exception e){
//			e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public List<Service> getServices(Services services) {
		logger.debug("loading getServices from db.");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerCategory.class);
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("id"),"id")
					.add(Projections.property("description"),"serviceCode")
					.add(Projections.property("name"),"serviceName"));
			criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			logger.debug("criteria created and executing.");
			return criteria.list();
		}catch(Exception e){
			//e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public void saveQuicktellerTxn(String id, String request, String response, String staus) {
		logger.debug("saveQuicktellerTxn to db. START");
		Session session  = null;
		try{
			session = sessionFactory.openSession();
			logger.debug("session created.");
			QuickTellerTxnLog qtxnLog= new QuickTellerTxnLog();
			qtxnLog.setTxnRef(id);
			qtxnLog.setRequest(request);
			qtxnLog.setResponse(response);
			session.save(qtxnLog);

		}catch(Exception e){
			logger.equals("Error while logging quicktellre transaction.");
			logger.equals("Reason..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			session.close();
			session = null;
		}
		logger.info("saveQuicktellerTxn to db. END");
	}

	@Override
	public List<Billers> MMObillers(String serviceCode) {
		logger.debug("loading biller from db with code."+serviceCode);
		String mmoCatId = ConfigLoader.convertResourceBundleToMap("config").get("airtime.mmo.catiId");
		Session session =null;
		logger.debug("session created.");
		Service service = null;
		try{
			session = sessionFactory.openSession();
			Criteria scriteria = session.createCriteria(Service.class);
			scriteria.add(Restrictions.eq("serviceCode", serviceCode));
			service = (Service) scriteria.uniqueResult();
			logger.debug(service.toString());
			Criteria criteria = session.createCriteria(Billers.class);
			//criteria.add(Restrictions.eq("sid", service.getsId()));
			criteria.add(Restrictions.eq("sid", Long.parseLong(mmoCatId)));
			criteria.add(Restrictions.eq("status", "A"));
			criteria.setProjection(Projections.projectionList()
					    .add(Projections.property("id").as("id"))
					    .add(Projections.property("billerName").as("billerName"))
					    .add(Projections.property("sid").as("sid")));
			criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

			logger.debug("criteria created and executing.");
			return (List<Billers>) criteria.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
			session = null;
		}
	}
	public BillerPackages getBillerPackage(String billerId, String packId) {
		logger.debug("loading BillerPackages from db.");
		Session session = null;
		BillerPackages pack = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerPackages.class);
			criteria.add(Restrictions.eq("packId", packId));
			criteria.add(Restrictions.eq("billerId", billerId));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			pack = (BillerPackages) criteria.uniqueResult();
			return pack;
		}catch(Exception e){
			logger.warn("Error while loading BillerPackages.");
			//e.printStackTrace();
			logger.error("Reason..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public List<BillerMakets> getBillerMarkets(long billerId) {
		logger.debug("loading BillerMarkets from db.");
		Session session = null;
		List<BillerMakets> markets = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerMakets.class);
			criteria.add(Restrictions.eq("billerId", billerId+""));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("id"),"id")
					.add(Projections.property("marketName"),"marketName"));
			criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			markets = criteria.list();
			return markets;
		}catch(Exception e){
			logger.warn("Error while loading BillerMarkets.");
			//e.printStackTrace();
			logger.error("Reason..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public boolean save(com.ceva.db.model.BillPayment billPayment) {
		logger.debug("save billpayment from db.");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
			logger.debug("session created.");
			session.save(billPayment);
			session.flush();
			session.getTransaction().commit();
			return true;
		}catch(Exception e){
			logger.error("Error While saving billpayment..:"+e.getLocalizedMessage());
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}
	}
	

}
