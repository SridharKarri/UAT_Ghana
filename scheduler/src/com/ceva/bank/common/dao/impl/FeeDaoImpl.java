
package com.ceva.bank.common.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.AgentCommision;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Service;
import com.ceva.db.model.ServiceProducts;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.Constants;

@Repository("FeeDao")
public class FeeDaoImpl implements FeeDao {
	Logger log = Logger.getLogger(FeeDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	MerchantDao merchantDao;

	@Override
	public Fee getFee(String serviceId, long channel, String merchantId, BigDecimal amount, String txnType) {
		log.info("getting fee from database..:START");
		Criteria criteria = null;
		Fee fee = null;
		String product = null;
		Merchant merchant = null;
		ServiceProducts serviceProducts = null;
		Session session =null;
		try {
			log.debug("creating session for fee");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			log.debug("[serviceId:"+serviceId+", channel:"+channel+", merchantId:"+merchantId+", amount:"+amount+", txnType:"+txnType+"]");
			merchant = (Merchant) merchantDao.getById(merchantId);
			if (merchant != null) {
				product = merchant.getProduct();
				serviceProducts = (ServiceProducts) merchantDao
						.getServiceProducts(serviceId, product).getData();
				log.debug(serviceProducts.toString());
				criteria = session.createCriteria(Fee.class);
				criteria.add(Restrictions.eq("channel", channel+""));
				criteria.add(Restrictions.eq("spid", serviceProducts.getSpId()));
				criteria.add(Restrictions.le("fromVal", amount));
				criteria.add(Restrictions.ge("toVal", amount));
				criteria.add(Restrictions.eq("txnType", txnType));
				criteria.add(Restrictions.eq("status", Status.A.toString()));
				log.debug("Criteria created..");
				fee = (Fee) criteria.uniqueResult();
				log.debug("executed Query..");
				if (fee != null) {
					log.debug(fee.toString());
					log.debug("Total Fee :"+fee.getFeeVal());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while getting fee..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
			merchant = null;
			serviceProducts = null;
			product = null;
		}
		log.info("getting fee from database..:END");
		return fee;
	}

	@Override
	public Fee getbyId(long id) {
		log.info("getting fee from database..:START");
		Fee fee = null;
		Session session= null;
		try{
			session = sessionFactory.openSession();
			fee = (Fee) session.createCriteria(Fee.class)
					.add(Restrictions.eq("id", id))
					.add(Restrictions.eq("status", Status.A.toString()))
					.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while Fee..:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("getting fee from database..:END");
		return fee;
	}

	@Override
	public Service getServiceByCode(String serviceCode) {
		log.info("getting Service from database..:START");
		Service  service= null;
		Session session= null;
		try{
			session = sessionFactory.openSession();
			service = (Service) session.createCriteria(Service.class)
					.add(Restrictions.eq("serviceCode", serviceCode))
					.add(Restrictions.eq("status", Status.A.toString())
							).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while Fee..:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("getting Service from database..:END");
		return service;
	}

	@Override
	public boolean updateCommision(String merchantId, Fee fee,
			String status, Date date, boolean isAgent) {
		log.info("updateCommision START");
		AgentCommision agentCommision = null;
		boolean inserted = false;
		Session session= null;
		try{
			session=sessionFactory.openSession();
			Criteria criteria = session.createCriteria(AgentCommision.class);
			criteria.add(Restrictions.eq("merchantId", merchantId));
			criteria.add(Restrictions.eq("settlementStatus", status));
			criteria.add(Restrictions.eq("commisionDate", date));
			if(isAgent){
				log.info("updating agent Commision");
				agentCommision = (AgentCommision) criteria.uniqueResult();
				agentCommision.setAmount(agentCommision.getAmount().add(fee.getAgntCmsn()));
			}else{
				log.info("updating superagent agent Commision");
				agentCommision = (AgentCommision) criteria.uniqueResult();
				agentCommision.setAmount(agentCommision.getAmount().add(fee.getSupAgentCmsn()));
			}
			session.flush();
			inserted= true;
		}catch(Exception e){
			inserted = false;
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("updateCommision END");
		return inserted;
	}

	@Override
	public boolean saveCommision(AgentCommision commision) {
		log.info("saveCommision START");
		boolean inserted = false;
		Criteria criteria = null;
		Session session =null;
		try{
			session =sessionFactory.openSession();
			criteria =session.createCriteria(AgentCommision.class);
			criteria.add(Restrictions.eq("merchantId", commision.getMerchantId()));
			criteria.add(Restrictions.sqlRestriction("TRUNC(commisionDate) = TO_DATE('" + new SimpleDateFormat(Constants.DATE_PATTERN).format(commision.getCommisionDate()) + "','"+Constants.DATE_PATTERN+"')"));
			//criteria.add(Restrictions.eq("commisionDate", new SimpleDateFormat(Constants.DATE_PATTERN).format(commision.getCommisionDate())));
			if(criteria.list().size() == 0){
				commision.setCommissionPayoutId(System.currentTimeMillis()+"");
				session.save(commision);
				inserted = true;
			}else{
				log.info("agent commision already inserting for today.");
				inserted = false;
			}
			session.flush();
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			inserted = false;
			e.printStackTrace();
		}finally{
			session.close();
			criteria = null;
			session = null;
		}
		log.info("saveCommision END");
		return inserted;
	}

	@Override
	public boolean inactiveCommisions(String date, String fetchStatus, String newStatus) {
		log.info("inactive Commisions START for..:"+date);
		boolean updated = false;
		Session session=null;
		try{
			session =sessionFactory.openSession();
			Criteria criteria = session.createCriteria(AgentCommision.class);
			//criteria.add(Restrictions.eq("settlementDate", date));
			criteria.add(Restrictions.eq("settlementStatus", fetchStatus));
			criteria.add(Restrictions.sqlRestriction("TRUNC(COMMISIONDATE) <= TO_DATE('" + date + "','"+Constants.DATE_PATTERN+"')"));
            ScrollableResults items = criteria.scroll();
            while ( items.next() ) {
            	AgentCommision commision = (AgentCommision)items.get(0);
            	commision.setSettlementStatus(newStatus);
                session.merge(commision);
                session.flush();
                session.clear();
            }
		}catch(Exception e){
			updated = false;
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("inactive Commisions END for date..:"+date);
		return updated;
	}

	@Override
	public List<AgentCommision> getCommisionsToSettlement(String status, String responseCode) {
		log.info("getting getCommisionsToSettlement from database..:START");
		Criteria criteria = null;
		List<AgentCommision> commisions = null;
		Session session = null;
		try {
			log.debug("creating session for getCommisionsToSettlement");
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(AgentCommision.class);
			criteria.add(Restrictions.eq("settlementStatus", status));
			criteria.add(Restrictions.eq("responseCode", responseCode));
			criteria.add(Restrictions.le("retryCount", 3));
			criteria.setMaxResults(10);
			log.debug("Criteria created..");
			commisions = criteria.list();
			log.debug("executed Query..");

			log.debug("commisions.." + commisions.size());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while loading commisions..:" + e.getLocalizedMessage());
		} finally {
			session.close();
			criteria = null;
			session = null;
		}
		return commisions;
	}

	@Override
	public boolean update(AgentCommision commision) {
		log.info("update Commision START");
		boolean inserted = false;
		Session session= null;
		try{
			session =sessionFactory.openSession();
			session.merge(commision);
			session.flush();
			inserted = true;
		}catch(Exception e){
			inserted = false;
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("update Commision END");
		return inserted;
	}

	@Override
	public AgentCommision commisionByMerchantId(String merchantId) {
		log.info("getting getCommisionByMerchantId from database..:START");
		Criteria criteria = null;
		AgentCommision commision = null;
		Session session = null;
		try {
			log.debug("creating session for getCommisionByMerchantId");
			//session = sessionFactory.getCurrentSession();
			session = sessionFactory.openSession();
			log.debug("session created..");
			log.debug("criteria creating..");
			criteria = session.createCriteria(AgentCommision.class);
			criteria.add(Restrictions.eq("merchantId", merchantId));
			criteria.add(Restrictions.eq("settlementStatus", Status.A.toString()));
			log.debug("Criteria created..");
			commision = (AgentCommision) criteria.uniqueResult();
			log.debug("executed Query..");
			if(commision == null){
				log.debug("commission is null, creating new commission...");
				if(merchantId.length() >= 10){
					commision = new AgentCommision(merchantId, Status.A.toString(), true);
				}else{
					commision = new AgentCommision(merchantId, Status.A.toString(), false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while loading commisions..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		return commision;
	}

	@Override
	public boolean addCommision(AgentCommision commision) {
		log.info("addCommision START");
		log.info(commision.toString());
		boolean inserted = false;
		Session session = null;
		try{
			session =sessionFactory.openSession();
			session.merge(commision);
			session.flush();
			inserted = true;
		}catch(Exception e){
			inserted = false;
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("addCommision END");
		return inserted;
	}

	@Override
	public ServiceProducts getProductService(long serviceId, long productId) {
		log.info("getting Service from database..:START");
		ServiceProducts  serviceProducts= null;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			serviceProducts = (ServiceProducts) session.createCriteria(ServiceProducts.class)
					.add(Restrictions.eq("sid", serviceId))
					.add(Restrictions.eq("pid", productId))
					.add(Restrictions.eq("status", Status.A.toString())
							).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while Fee..:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.info("getting Service from database..:END");
		return serviceProducts;
	}

	@Override
	public Fee getFee(long spid, BigDecimal amount) {
		log.info("getting fee from database based on spid and amount..:START");
		Fee fee= null;
		Session session = null;
		Criteria criteria = null;
		try{
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Fee.class);
			criteria.add(Restrictions.eq("spid", spid));
			criteria.add(Restrictions.le("fromVal", amount));
			criteria.add(Restrictions.ge("toVal", amount));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			fee = (Fee) criteria.uniqueResult();
			if(fee!= null){
				session.evict(fee);
				if(Status.F.toString().equals(fee.getFeeType())){
					log.info("found fee is F type returning ");
					return fee;
				}else{

					log.info("found fee is % type caliculating values for %.");
					if(Status.B.toString().equals(fee.getBins())){
						log.info("Identified Charge to Bank, only Commission will be Caliculated.");
						return caliculateAgentCommission(fee, amount);
					}else{
						log.info("Identified Charge to Customer.");
						return caliculateCustomerFee(fee, amount);
					}
				
					/*log.info("found fee is % type caliculating values for %.");
					if(Status.C.toString().equals(fee.getBins())){
						log.info("Identified Charge to Customer,");
						fee = caliculateAgentCommission(fee, amount);
					}else{
						log.info("Identified Charge to Bank, only Commission will be Caliculated.");
						fee = caliculateCustomerFee(fee, amount);
					}
					return fee;*/
				}
			}else{
				return fee;
			}
		}catch(Exception e){
			log.error("Error while getting fee.. from database");
			e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
			return null;
		}finally{
			criteria = null;
			session.close();
		}
	}
	
	private Fee caliculateAgentCommission(Fee fee, BigDecimal amount) {
		log.info("caliculateAgentCommcssion..");
		BigDecimal commision = new BigDecimal(0);
		BigDecimal agentcmsn= new BigDecimal(0);
		BigDecimal supAgentcmsn= new BigDecimal(0);
		try{
			try{
				commision = amount.multiply(fee.getFeeVal()).divide(new BigDecimal(100));
			}catch(Exception e){
				log.error("error while AgentCommcssion ..");
			}
			try{
				if(fee.getAgntCmsn() != null && fee.getAgntCmsn().doubleValue() != 0){
					log.info("caliculating AgentCommcssion..");
					agentcmsn = commision.multiply(fee.getAgntCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating agent commision ..");
				agentcmsn = new BigDecimal(0);
			}
			try{
				if(fee.getSupAgentCmsn() !=null && fee.getSupAgentCmsn().doubleValue() != 0){
					log.info("caliculating super agent commision..");
					supAgentcmsn = commision.multiply(fee.getSupAgentCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating super agent commision ..");
				supAgentcmsn = new BigDecimal(0);
			}
			fee.setAgntCmsn(agentcmsn);
			fee.setSupAgentCmsn(supAgentcmsn);
			fee.setFeeVal(new BigDecimal(0));
		}catch(Exception e){
			log.error("Error while caliculating fee..");
			e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
		}
		return fee;
	}

	private Fee caliculateCustomerFee(Fee fee, BigDecimal amount) {
		log.info("caliculating % fees..");
		BigDecimal commision = new BigDecimal(0);
		BigDecimal agentcmsn= new BigDecimal(0);
		BigDecimal supAgentcmsn= new BigDecimal(0);
		try{
			try{
				commision = amount.multiply(fee.getFeeVal()).divide(new BigDecimal(100));
			}catch(Exception e){
				log.error("error while caliculating commision ..");
			}
			try{
				if(fee.getAgntCmsn() !=null && fee.getAgntCmsn().intValue() != 0){
					log.info("caliculating agent commision..");
					agentcmsn = commision.multiply(fee.getAgntCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating agent commision ..");
				agentcmsn = new BigDecimal(0);
			}
			try{
				if(fee.getSupAgentCmsn() !=null && fee.getSupAgentCmsn().intValue() != 0){
					log.info("caliculating super agent commision..");
					supAgentcmsn = commision.multiply(fee.getSupAgentCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating super agent commision ..");
				supAgentcmsn = new BigDecimal(0);
			}
			fee.setAgntCmsn(agentcmsn);
			fee.setSupAgentCmsn(supAgentcmsn);
			fee.setFeeVal(commision);
		}catch(Exception e){
			log.error("Error while caliculating fee..");
			e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
		}
		return fee;
	}

	/*private Fee caliculateAgentCommission(Fee fee, BigDecimal amount) {
		log.info("caliculate AgentCommcssion..");
		BigDecimal commision = new BigDecimal(0);
		BigDecimal agentcmsn= new BigDecimal(0);
		BigDecimal supAgentcmsn= new BigDecimal(0);
		try{
			try{
				commision = amount.multiply(fee.getFeeVal()).divide(new BigDecimal(100));
			}catch(Exception e){
				log.error("error while AgentCommcssion ..");
			}
			try{
				if(fee.getAgntCmsn() != null && fee.getAgntCmsn().doubleValue() != 0){
					log.info("caliculating AgentCommcssion..");
					agentcmsn = commision.multiply(fee.getAgntCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating agent commision ..");
				agentcmsn = new BigDecimal(0);
			}
			try{
				if(fee.getSupAgentCmsn() !=null && fee.getSupAgentCmsn().doubleValue() != 0){
					log.info("caliculating super agent commision..");
					supAgentcmsn = commision.multiply(fee.getSupAgentCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating super agent commision ..");
				supAgentcmsn = new BigDecimal(0);
			}
			fee.setAgntCmsn(agentcmsn);
			fee.setSupAgentCmsn(supAgentcmsn);
			fee.setFeeVal(commision);
			//fee.setFeeVal(new BigDecimal(0));
		}catch(Exception e){
			log.error("Error while caliculating fee..");
			e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
		}
		return fee;
	}

	private Fee caliculateCustomerFee(Fee fee, BigDecimal amount) {
		log.info("caliculating % fees..");
		BigDecimal commision = new BigDecimal(0);
		BigDecimal agentcmsn= new BigDecimal(0);
		BigDecimal supAgentcmsn= new BigDecimal(0);
		try{
			try{
				commision = amount.multiply(fee.getFeeVal()).divide(new BigDecimal(100));
			}catch(Exception e){
				log.error("error while caliculating commision ..");
			}
			try{
				if(fee.getAgntCmsn() !=null && fee.getAgntCmsn().doubleValue() != 0){
					log.info("caliculating agent commision..");
					agentcmsn = commision.multiply(fee.getAgntCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating agent commision ..");
				agentcmsn = new BigDecimal(0);
			}
			try{
				if(fee.getSupAgentCmsn() !=null && fee.getSupAgentCmsn().doubleValue() != 0){
					log.info("caliculating super agent commision..");
					supAgentcmsn = commision.multiply(fee.getSupAgentCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating super agent commision ..");
				supAgentcmsn = new BigDecimal(0);
			}
			fee.setAgntCmsn(agentcmsn);
			fee.setSupAgentCmsn(supAgentcmsn);
			//fee.setFeeVal(commision);
			fee.setFeeVal(new BigDecimal(0));
		}catch(Exception e){
			log.error("Error while caliculating fee..");
			e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
		}
		return fee;
	}*/

}
