package com.ceva.bank.common.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
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
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.PackageFormItem;
import com.ceva.db.model.Service;
import com.nbk.util.ConfigLoader;
@Repository("billPaymentDao")
public class BillPaymentDaoImpl implements BillPaymentDao {

	private static Logger logger = Logger.getLogger(BillPaymentDaoImpl.class);
	private static final String languageQuery =  ResourceBundle.getBundle("config").getString("language.query");

	@Autowired
	SessionFactory sessionFactory;

	
	@Override
	public List<Service> getServices(Services services) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerCategory.class);
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("id"),"id")
					.add(Projections.property("name"),"serviceName")
					.add(Projections.property("frname"),"frserviceName"));
			criteria.add(Restrictions.ne("id", new Long(1)));
			criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
	public List<Billers> getBillers() {
		logger.debug("loading billers from db.");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("status", "A"));
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
					.add(Projections.property("billerName"),"billerName")
					.add(Projections.property("frbillerName"),"frbillerName"));
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
			Criteria criteria = session.createCriteria(Billers.class);
			criteria.add(Restrictions.eq("id", id));
			criteria.add(Restrictions.eq("status", "A"));
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
			Criteria criteria = session.createCriteria(BillerPackages.class);
			criteria.add(Restrictions.eq("billerId", billerId+""));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
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
	public boolean save(com.ceva.db.model.BillPayment billPayment) {
		logger.debug("save billpayment from db.");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
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

	@Override
	public List<PackageFormItem> getBillerFormItems(long packId) {
		logger.debug("loading biller FormItems from db with packId."+packId);
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(PackageFormItem.class);
			criteria.add(Restrictions.eq("packId", packId));
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
	public PackageFormItem getBillerFormItemByPackId(Long packId) {
		logger.debug("loading biller FormItems from db with id.:"+packId);
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(PackageFormItem.class);
			criteria.add(Restrictions.eq("id", packId));
			return (PackageFormItem) criteria.uniqueResult();
		}catch(Exception e){
//			e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public BillerPackages getBillerPackageById(long id) {
		logger.debug("loading BillerPackages from db with id.:"+id);
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(BillerPackages.class);
			criteria.add(Restrictions.eq("id", id));
			return (BillerPackages) criteria.uniqueResult();
		}catch(Exception e){
//			e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
			return null;
		}finally{
			session.close();
		}
	}

	@Override
	public String getLanguage(String userId) {
		Session session = null;
		List<String> rows= new ArrayList<String>();
		String lang = null;
		try{
			session = sessionFactory.openSession();
			String otpHql = "SELECT LANGUAGE FROM CHANNEL_USERS CU WHERE COMMONID IN "
					+ "(SELECT COMMON_ID FROM USER_LOGIN_CREDENTIALS WHERE LOGIN_USER_ID=:userId) "
					+ "AND MAKER_DTTM=(SELECT MAX(MAKER_DTTM) FROM CHANNEL_USERS ICU WHERE ICU.COMMONID=CU.COMMONID)"
					+ "AND STATUS=:status";
			SQLQuery otpQuery = session.createSQLQuery(languageQuery);
	        otpQuery.setParameter("userId", userId);
	        otpQuery.setParameter("status", "A");
	        otpQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        rows = otpQuery.list();
	        for(Object record : rows){
				Map<String,String> row = (Map<String,String>)record;
				if(String.valueOf(row.get("LANGUAGE"))!=null){
					lang = String.valueOf(row.get("LANGUAGE"));
				}else{
					lang = "en";
				}
			}
	    }catch(Exception e){
			logger.error("Error while getting service from db..:" + e.getLocalizedMessage());
			lang="en";
		}finally {
			session.close();
		}
		return lang;
	}
}
