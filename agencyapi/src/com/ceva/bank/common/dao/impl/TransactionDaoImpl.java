package com.ceva.bank.common.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.db.model.AgentCommision;
import com.ceva.db.model.BanksAndWallets;
import com.ceva.db.model.ErrorLog;
import com.ceva.db.model.Fee;
import com.ceva.db.model.NonFinTransaction;
import com.ceva.db.model.Store;
import com.ceva.db.model.Transaction;

@Repository("transactionDao")
public class TransactionDaoImpl implements TransactionDao {

	Logger log = Logger.getLogger(TransactionDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	FeeDao feeDao;

	@Override
	public long save(Transaction transaction) {
		log.debug("Saving transaction");
		long id;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			id = (long) session.save(transaction);
			session.flush();
			log.debug("Saved transaction Successfully.");

		}catch(Exception e){
			id = 0;
			log.warn("Error While Saving Transaction.");
			log.error("cause.:"+e.getLocalizedMessage());
		}finally{
			session.close();
			session = null;
		}
		return id;
	}
	
	@Override
	public int getStoreTxnCount(String storeId) {
		Session session =null;
		Long count = new Long(0);
		try {
			log.debug("getStoreTxnCount...");
			session = sessionFactory.openSession();
			Query query = session.createQuery("select count(*) from Transaction where storeId= :storeId and txnCode IN (:txnCode) and status = :status and trunc(txndatetime)=trunc(sysdate)");
			query.setParameter("storeId", storeId);
			query.setParameterList("txnCode", new String[]{"CASHDEP","FTINTERBANK","FTINTRABANK","BILLPAYMENT","DEPWALLET","CWDBYACT","CWDBYCRD"});
			query.setParameter("status", "SUCCESS");
			count = (long) query.uniqueResult();
		}catch(Exception e){
			log.warn("Error While getStoreTxnCount");
			log.error("cause.:" + e.getLocalizedMessage());
			return 0;
		}finally{
			session.close();
		}
		return count.intValue();
	}

	@Override
	public boolean update(Transaction transaction) {
		log.debug("Update transaction.:"+transaction.getId());
		boolean saved = false;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			//session.saveOrUpdate(transaction);
			session.update(transaction);
			session.flush();
			
			saved = true;
			log.debug("Updated transaction Successfully");
		} catch (Exception e) {
			saved = false;
			log.warn("Error While Updating Transaction");
			log.error("cause.:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		return saved;
	}

	@Override
	public Transaction getById(long id) {
		log.debug("getById transaction START");
		Criteria criteria = null;
		Transaction transaction = null;
		Session session = null;
		try {
			session=sessionFactory.openSession();
			criteria = session.createCriteria(
					Transaction.class);
			criteria.add(Restrictions.eq("id", id));
			transaction = (Transaction) criteria.uniqueResult();
		} catch (Exception e) {
			log.warn("Error While Updating Transaction");
			log.error("cause.:" + e.getLocalizedMessage());
			transaction = null;
		} finally {
			session.close();
			criteria = null;
		}
		log.debug("getById transaction END");
		return transaction;
	}

	@Override
	public List<Transaction> getAll(Map<String, Object> condtions) {
		log.debug("transaction START");
		Criteria criteria = null;
		List<Transaction> transactions = null;
		Session session = null;
		try {
			session= sessionFactory.openSession();
			criteria = session.createCriteria(
					Transaction.class);
			if(condtions.containsKey("ID")){
				criteria.add(Restrictions.eq("id", condtions.get("ID")));
			}
			if(condtions.containsKey("USERID")){
				criteria.add(Restrictions.eq("approvedBy", condtions.get("USERID")));
			}
			if(condtions.containsKey("MERCHANTID")){
				criteria.add(Restrictions.eq("merchantId", condtions.get("MERCHANTID")));
			}
			transactions = criteria.list();
		} catch (Exception e) {
			log.warn("Error While Updating Transaction");
			log.error("cause.:" + e.getLocalizedMessage());
			transactions = null;
		} finally {
			session.close();
			criteria = null;
		}
		log.debug("transaction END");
		return transactions;
	}

	@Override
	public void logError(String error, String errorCode, String merchantId,
			String narration, String userId) {
		log.debug("inserting error log START");
		ErrorLog elog = null;
		Session session = null;
		try {
			elog = new ErrorLog();
			elog.setErrorCode(errorCode);
			elog.setErrorDescription(error);
			elog.setMerchantId(merchantId);
			elog.setUserId(userId);
			elog.setNarration(narration);
			session = sessionFactory.openSession();
			session.save(elog);
			log.debug("Error Inserted Successfully.");
		} catch (Exception e) {
			log.warn("Error While Saving Errorlog.");
			log.error("cause.:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.debug("inserting error log END");
	}

	@Override
	public List<BanksAndWallets> getBanks(String id) {
		log.debug("geting BanksAndWallets START");
		Criteria criteria = null;
		List<BanksAndWallets> banksAndWallets = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(
					BanksAndWallets.class);
			criteria.add(Restrictions.eq("category", id));
			criteria.setProjection( Projections.projectionList()
					.add( Projections.property("id"), "id")
					.add( Projections.property("instName"), "instName")
					.add( Projections.property("category"), "category")
					.add( Projections.property("bankCode"), "bankCode"))
					.setResultTransformer(Transformers.aliasToBean(BanksAndWallets.class));
			banksAndWallets = (List<BanksAndWallets>) criteria.list();
		} catch (Exception e) {
			log.warn("Error While inserting Transaction");
			log.error("cause.:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
			criteria = null;
		}
		return banksAndWallets;
	}
	
	@Override
	public BanksAndWallets getBank(String bankCode) {
		log.debug("geting BanksAndWallets START..:"+bankCode);
		Criteria criteria = null;
		BanksAndWallets banksAndWallets = null;
		Session session =null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(
					BanksAndWallets.class);
			criteria.add(Restrictions.eq("bankCode", bankCode));
			banksAndWallets = (BanksAndWallets) criteria.uniqueResult();
		} catch (Exception e) {
			log.warn("Error BanksAndWallets");
			log.error("cause.:" + e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
			criteria = null;
		}
		return banksAndWallets;
	}
	
	@Override
	public void updateCommisions(Store store, Fee fee) {
		log.debug("updating commision for agents");
		AgentCommision commision = null;
		try{
			log.debug("updating agent commision");
			commision = feeDao.commisionByStoreId(store.getStoreId());
			log.debug(commision.toString());
			commision.setAmount(commision.getAmount().add(fee.getAgntCmsn()));
			//commision.setAmount(fee.getAgntCmsn());
			try{
				log.debug("==============================================");
				feeDao.addCommision(commision);
				log.debug("==============================================");
			}catch(Exception e){
				log.error("Error "+e.getLocalizedMessage());
			}
			
		}catch(Exception e){
			log.warn("Error while updating commision.");
			log.error("Reason..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			//commision = null;
			store = null;
			fee=null;
		}
	
	}
	
	@Override
	public boolean save(NonFinTransaction transaction) {
		boolean inserted= false;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			session.save(transaction);
			session.flush();
			log.debug("Saved NonFinTransaction Successfully.");
			inserted = true;
		}catch(Exception e){
			log.warn("Error While Saving NonFinTransaction.");
			log.error("cause.:"+e.getLocalizedMessage());
			inserted= false;
		}finally{
			session.close();
		}
		return inserted;
	}

}
