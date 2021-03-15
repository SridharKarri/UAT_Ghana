
package com.ceva.bank.common.dao.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.utils.Status;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.AgentCommision;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Service;
import com.ceva.db.model.ServiceProducts;
import com.ceva.db.model.Transaction;
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
		log.debug("getting fee from database..:START");
		Criteria criteria = null;
		Fee fee = null;
		String product = null;
		Merchant merchant = null;
		ServiceProducts serviceProducts = null;
		Session session =null;
		try {
			session = sessionFactory.openSession();
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
			//e.printStackTrace();
			log.error("Error while getting fee..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			merchant = null;
			serviceProducts = null;
			product = null;
			session.close();
		}
		log.debug("getting fee from database..:END");
		return fee;
	}

	@Override
	public Fee getbyId(long id) {
		log.debug("getting fee from database..:START");
		Fee fee = null;
		Session session= null;
		try{
			session = sessionFactory.openSession();
			fee = (Fee) session.createCriteria(Fee.class)
					.add(Restrictions.eq("id", id))
					.add(Restrictions.eq("status", Status.A.toString()))
					.uniqueResult();
		}catch(Exception e){
			//e.printStackTrace();
			log.error("Error while Fee..:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.debug("getting fee from database..:END");
		return fee;
	}

	@Override
	public Service getServiceByCode(String serviceCode) {
		log.debug("getting Service from database..:START");
		Service  service= null;
		Session session= null;
		try{
			session = sessionFactory.openSession();
			service = (Service) session.createCriteria(Service.class)
					.add(Restrictions.eq("serviceCode", serviceCode))
					.add(Restrictions.eq("status", Status.A.toString())
							).uniqueResult();
		}catch(Exception e){
			//e.printStackTrace();
			log.error("Error while Fee..:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.debug("getting Service from database..:END");
		return service;
	}

	@Override
	public boolean updateCommision(String merchantId, Fee fee,
			String status, Date date, boolean isAgent) {
		log.debug("updateCommision START");
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
				log.debug("updating agent Commision");
				agentCommision = (AgentCommision) criteria.uniqueResult();
				agentCommision.setAmount(agentCommision.getAmount().add(fee.getAgntCmsn()));
			}else{
				log.debug("updating superagent agent Commision");
				agentCommision = (AgentCommision) criteria.uniqueResult();
				agentCommision.setAmount(agentCommision.getAmount().add(fee.getSupAgentCmsn()));
			}
			session.flush();
			inserted= true;
		}catch(Exception e){
			inserted = false;
			//e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.debug("updateCommision END");
		return inserted;
	}

	@Override
	public boolean saveCommision(AgentCommision commision) {
		log.debug("saveCommision START");
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
				log.debug("agent commision already inserting for today.");
				inserted = false;
			}
			session.flush();
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			inserted = false;
			//e.printStackTrace();
		}finally{
			criteria = null;
			session.close();
			session = null;
		}
		log.debug("saveCommision END");
		return inserted;
	}

	@Override
	public boolean inactiveCommisions(String date, String fetchStatus, String newStatus) {
		log.debug("inactive Commisions START for..:"+date);
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
			//e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.debug("inactive Commisions END for date..:"+date);
		return updated;
	}

	@Override
	public List<AgentCommision> getCommisionsToSettlement(String status, String responseCode) {
		log.debug("getting getCommisionsToSettlement from database..:START");
		Criteria criteria = null;
		List<AgentCommision> commisions = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(AgentCommision.class);
			criteria.add(Restrictions.eq("settlementStatus", status));
			criteria.add(Restrictions.eq("responseCode", responseCode));
			criteria.setMaxResults(10);
			commisions = criteria.list();
			log.debug("commisions.." + commisions.size());

		} catch (Exception e) {
			//e.printStackTrace();
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
		log.debug("update Commision START");
		boolean inserted = false;
		Session session= null;
		try{
			session =sessionFactory.openSession();
			session.merge(commision);
			session.flush();
			inserted = true;
		}catch(Exception e){
			inserted = false;
			//e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.debug("update Commision END");
		return inserted;
	}

	@Override
	public AgentCommision commisionByMerchantId(String merchantId) {
		log.debug("getting getCommisionByMerchantId from database..:START");
		Criteria criteria = null;
		AgentCommision commision = null;
		Session session = null;
		try {
			log.debug("creating session for getCommisionByMerchantId");
			session = sessionFactory.openSession();
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
			//e.printStackTrace();
			log.error("Error while loading commisions..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		return commision;
	}
	
	@Override
	public Service getService(String serviceCode) {
		Service service = null;
		Session session = null;
		Criteria criteria = null;
		try{
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Service.class);
			criteria.add(Restrictions.eq("serviceCode", serviceCode));
			service = (Service) criteria.uniqueResult();
		}catch(Exception e){
			log.error("Error while getting service from db..:" + e.getLocalizedMessage());
		}finally {
			criteria = null;
			session.close();
		}
		return service;
	}
	
	@Override
	public ServiceProducts getServiceProducts(long sid, long prodId) {
		ServiceProducts service = null;
		Session session = null;
		Criteria criteria = null;
		try{
			session = sessionFactory.openSession();
			criteria = session.createCriteria(ServiceProducts.class);
			criteria.add(Restrictions.eq("sid", sid));
			criteria.add(Restrictions.eq("pid", prodId));
			criteria.add(Restrictions.eq("status", "A"));
			service = (ServiceProducts) criteria.uniqueResult();
		}catch(Exception e){
			log.error("Error while getting service from db..:" + e.getLocalizedMessage());
		}finally {
			criteria = null;
			session.close();
		}
		return service;
	}

	@Override
	public boolean addCommision(AgentCommision commision) {
		log.debug("addCommision START");
		log.debug(commision.toString());
		boolean inserted = false;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			session.merge(commision);
			session.flush();
			inserted = true;
		}catch(Exception e){
			inserted = false;
			//e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally {
			session.close();
		}
		log.debug("addCommision END");
		return inserted;
	}

	@Override
	public ServiceProducts getProductService(long serviceId, long productId) {
		log.debug("getting Service from database..:START");
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
			//e.printStackTrace();
			log.error("Error while Fee..:" + e.getLocalizedMessage());
		}finally{
			session.close();
		}
		log.debug("getting Service from database..:END");
		return serviceProducts;
	}

	@Override
	public Fee getFee(long spid, BigDecimal amount) {
		log.debug("getting fee from database based on spid and amount..:START");
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
					log.debug("found fee is F type returning ");
					return fee;
				}else{
					log.debug("found fee is % type caliculating values for %.");
					if(Status.B.toString().equals(fee.getBins())){
						log.debug("Identified Charge to Bank, only Commission will be Caliculated.");
						return caliculateAgentCommission(fee, amount);
					}else{
						log.debug("Identified Charge to Customer.");
						return caliculateCustomerFee(fee, amount);
					}
					
				}
			}else{
				return fee;
			}
		}catch(Exception e){
			log.error("Error while getting fee.. from database");
			//e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
			return null;
		}finally{
			criteria = null;
			session.close();
		}
	}
	
	private Fee caliculateAgentCommission(Fee fee, BigDecimal amount) {
		log.debug("caliculateAgentCommcssion..");
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
					log.debug("caliculating AgentCommcssion..");
					agentcmsn = commision.multiply(fee.getAgntCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating agent commision ..");
				agentcmsn = new BigDecimal(0);
			}
			try{
				if(fee.getSupAgentCmsn() !=null && fee.getSupAgentCmsn().doubleValue() != 0){
					log.debug("caliculating super agent commision..");
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
			//e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
		}
		return fee;
	}

	private Fee caliculateCustomerFee(Fee fee, BigDecimal amount) {
		log.debug("caliculating % fees..");
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
					log.debug("caliculating agent commision..");
					agentcmsn = commision.multiply(fee.getAgntCmsn()).divide(new BigDecimal(100));
				}
			}catch(Exception e){
				log.error("error while caliculating agent commision ..");
				agentcmsn = new BigDecimal(0);
			}
			try{
				if(fee.getSupAgentCmsn() !=null && fee.getSupAgentCmsn().intValue() != 0){
					log.debug("caliculating super agent commision..");
					//supAgentcmsn = commision.multiply(fee.getSupAgentCmsn()).divide(new BigDecimal(100));
					supAgentcmsn = commision.subtract(agentcmsn);
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
			//e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
		}
		return fee;
	}

	@Override
	public Incentive getIncentive(String storeId, long txnCount, Fee fee) {
		log.debug("getting Incentive from database ..:START");
		Incentive incentive = null;
		Session session = null;
		Criteria criteria = null;
		try{
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Incentive.class);
			criteria.add(Restrictions.eq("storeId", storeId));
			criteria.add(Restrictions.le("fromVal", txnCount+1));
			criteria.add(Restrictions.ge("toVal", txnCount+1));
			criteria.add(Restrictions.eq("status", Status.A.toString()));
			incentive = (Incentive) criteria.uniqueResult();
			if(incentive!= null){
				log.debug(incentive.toString());
				session.evict(incentive);
				if("F".equals(incentive.getIncType())){
					log.debug("found Incentive is Fixed type returning Incentive.");
					return incentive;
				}else{
					return caliculateAgentincentive(incentive, fee);
				}
			}else{
				return new Incentive();
			}
		}catch(Exception e){
			log.error("Error while getting Incentive.. from database");
			//e.printStackTrace();
			log.error("Reason ..:"+e.getLocalizedMessage());
			return new Incentive();
		}finally{
			criteria = null;
			session.close();
		}
	}

	private Incentive caliculateAgentincentive(Incentive incentive, Fee fee) {
		BigDecimal incentiveVal = null;
		try{
			incentiveVal = getValueFromPercentage(incentive.getIncVal(), fee.getFeeVal());
			incentive.setIncVal(incentiveVal);
		}catch(Exception e){
			//e.printStackTrace();
			log.error("Error While calculating incentive..:"+e.getLocalizedMessage());
		}finally{
			fee = null;
			incentiveVal=null;
		}
		return incentive;
	}
	
	public BigDecimal getValueFromPercentage(BigDecimal incentive, BigDecimal total) {
		BigDecimal incentiveVal =null;
		try{
			//incentiveVal = (incentive.divide(total)).multiply(new BigDecimal(100));
			incentiveVal = (incentive.divide(new BigDecimal(100))).multiply(total);
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			incentiveVal = new BigDecimal(0);
		}
	    return incentiveVal;
	}
	
	/*public static void main(String[] args) {
	FeeDaoImpl impl = new FeeDaoImpl();
	BigDecimal val = impl.getValueFromPercentage(new BigDecimal(5), new BigDecimal(50));
	System.out.println(val);
	}*/

	@Override
	public AgentCommision commisionByStoreId(String storeId) {
		log.debug("getting getCommisionByStoreId from database..:START");
		Criteria criteria = null;
		AgentCommision commision = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			criteria = session.createCriteria(AgentCommision.class);
			criteria.add(Restrictions.eq("merchantId", storeId));
			criteria.add(Restrictions.eq("settlementStatus", Status.A.toString()));
			commision = (AgentCommision) criteria.uniqueResult();
			log.debug("executed Query..");
			if(commision == null){
				log.debug("commission is null, creating new commission...");
				commision = new AgentCommision(storeId, Status.A.toString(), true);
				commision.setCommisionDate(new Date());
				session.save(commision);
			}

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Error while loading commisions..:" + e.getLocalizedMessage());
		} finally {
			criteria = null;
			session.close();
		}
		return commision;
	}
	@Override
	public BigDecimal getAgentStoreCommission(String storeId) {
		BigDecimal commission= null;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			String sumHql = "select sum(commission.amount) FROM AgentCommision commission where commission.settlementStatus in(:settlementStatus) and commission.merchantId = :merchantId";
	        Query sumQuery = session.createQuery(sumHql);
	        //sumQuery.setParameterList("settlementStatus", new String[]{"A","E","P","I","B"});
	        sumQuery.setParameterList("settlementStatus", new String[]{"E","P","I","B"});
	        sumQuery.setParameter("merchantId", storeId);
	        commission = (BigDecimal) sumQuery.uniqueResult();
		}catch(Exception e){
			//e.printStackTrace();
			log.error("Error while getting service from db..:" + e.getLocalizedMessage());
		}finally {
			session.close();
		}
		return commission;
	}

	@Override
	public BigDecimal getAgentStoreCurrentMonthCommission(String store) {
		BigDecimal commission = null;
		Session session = null;
		try{
			 session = sessionFactory.openSession();
			 Criteria criteria = session.createCriteria(Transaction.class);
			  ProjectionList proList = Projections.projectionList();
			  //proList.add(Projections.sum("agentCmsn"));
			  proList.add(Projections.sqlProjection(" nvl(sum(INCENTIVE + AGENTCMSN), 0) as agentCmsn",new String[]{"agentCmsn"}, new Type[]{StandardBasicTypes.BIG_DECIMAL}));
			  criteria.setProjection(proList);
			  criteria.add(Restrictions.eq("storeId", store));
			  criteria.add(Restrictions.eq("status", TransactionStatus.SUCCESS.toString()));
			  criteria.add(Restrictions.between("txndateTime", getFirstDateOfMonth(), getLastDateOfMonth()));
			  List<BigDecimal> sumResult = criteria.list();
			if(sumResult.size()>0){
				commission=sumResult.get(0);
			}else{
				commission=new BigDecimal(0);
			}
		}catch(Exception e){
			log.error("Error while getting getAgentStoreCurrentMonthCommission.:"+e.getLocalizedMessage());
		}finally {
			session.close();
		}
		return commission;
	}
	@Override
	public BigDecimal getAgentUserCurrentMonthCommission(String userId, String storeId) {
		BigDecimal commission = null;
		Session session = null;
		try{
			 session = sessionFactory.openSession();
			 Criteria criteria = session.createCriteria(Transaction.class);
			  ProjectionList proList = Projections.projectionList();
			  //proList.add(Projections.sum("agentCmsn"));
			  proList.add(Projections.sqlProjection(" nvl(sum(INCENTIVE + AGENTCMSN), 0) as agentCmsn",new String[]{"agentCmsn"}, new Type[]{StandardBasicTypes.BIG_DECIMAL}));
			  criteria.setProjection(proList);
			  criteria.add(Restrictions.eq("approvedBy", userId));
			  criteria.add(Restrictions.eq("storeId", storeId));
			  criteria.add(Restrictions.eq("status", TransactionStatus.SUCCESS.toString()));
			  criteria.add(Restrictions.between("txndateTime", getFirstDateOfMonth(), getLastDateOfMonth()));
			  List<BigDecimal> sumResult = criteria.list();
			if(sumResult.size()>0){
				commission=sumResult.get(0);
			}else{
				commission=new BigDecimal(0);
			}
		}catch(Exception e){
			log.error("Error while getting getAgentStoreCurrentMonthCommission.:"+e.getLocalizedMessage());
		}finally {
			session.close();
		}
		return commission;
	}
	public static Date getLastDateOfMonth() {
		Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = 1;
	    c.set(year, month, day);
	    int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
	    c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
		return c.getTime();
	}
	public static Date getFirstDateOfMonth() {
		Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);
	    int month = c.get(Calendar.MONTH);
	    int day = 1;
	    c.set(year, month, day);
	    int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return c.getTime();
	}
}
