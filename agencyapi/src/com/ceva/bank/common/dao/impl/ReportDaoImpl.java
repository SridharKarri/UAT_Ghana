package com.ceva.bank.common.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.beans.TransactionReport;
import com.ceva.bank.common.dao.ReportDao;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.Transaction;
import com.nbk.util.Constants;

@Repository("reportDao")
public class ReportDaoImpl implements ReportDao {

	Logger log = Logger.getLogger(ReportDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	enum ReportCodes {
		TXNRPT,CMSNRPT,SUPCMSNRPT,BANKCMSNRPT,INBOX
		}

	@Override
	public List<Transaction> sucessTransactionReport(TransactionReport report) {
		log.debug("report for ..:"+report.getReportType());
		List<Transaction> transactions = null;
		Criteria criteria = null;
		boolean execute = true;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			criteria = session.createCriteria(Transaction.class);

			criteria.add(Restrictions.eq("merchantId", report.getMerchantId()));
			criteria.add(Restrictions.sqlRestriction("TRUNC(TXNDATETIME) between TO_DATE('" + report.getFromDate() + "','"+Constants.SHORT_DATE_PATTERN+"') AND TO_DATE('" + report.getToDate() + "','"+Constants.SHORT_DATE_PATTERN+"')"));
			criteria.addOrder(Order.desc("id"));

			if(ReportCodes.TXNRPT.toString().equals(report.getReportType())){
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("id"),"id")
						.add(Projections.property("approvedBy"),"approvedBy")
						.add(Projections.property("txnCode"),"txnCode")
						.add(Projections.property("fee"),"fee")
						.add(Projections.property("refNumber"),"refNumber")
						.add(Projections.property("customerAccountNumber"),"custAccNum")
						.add(Projections.property("agentAccountNumber"),"agentAccNum")
						.add(Projections.property("amount"),"amount")
						.add(Projections.property("txnCommission"),"txnCommission")
						//.add(Projections.property("currency"),"currency")
						//.add(Projections.sqlProjection("to_date(txndateTime, '"+Constants.SHORT_DATE_PATTERN+"') as txndateTime", new String[] { "txndateTime" }, new Type[] { StandardBasicTypes.TIMESTAMP }))
						.add(Projections.property("txndateTime"),"txndateTime")
						.add(Projections.property("status"),"status"));
				criteria.add(Restrictions.eq("approvedBy", report.getUserId()));

			}else if(ReportCodes.CMSNRPT.toString().equals(report.getReportType())){
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("id"),"id")
						.add(Projections.property("approvedBy"),"approvedBy")
						.add(Projections.property("txnCode"),"txnCode")
						.add(Projections.property("fee"),"fee")
						.add(Projections.property("refNumber"),"refNumber")
						.add(Projections.property("customerAccountNumber"),"custAccNum")
						.add(Projections.property("agentAccountNumber"),"agentAccNum")
						//.add(Projections.property("agentCmsn"),"agentCmsn")
						.add(Projections.property("txnCommission"),"agentCmsn")
						//.add(Projections.property("currency"),"currency")
						.add(Projections.sqlProjection("to_date(txndateTime, '"+Constants.SHORT_DATE_PATTERN+"') as txndateTime", new String[] { "txndateTime" }, new Type[] { StandardBasicTypes.DATE }))
						.add(Projections.property("status"),"status"));

				criteria.add(Restrictions.eq("approvedBy", report.getUserId()));
				criteria.add(Restrictions.in("responseCode", new String[]{"0", "00"}));

			}else if(ReportCodes.SUPCMSNRPT.toString().equals(report.getReportType())){
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("id"),"id")
						.add(Projections.property("approvedBy"),"approvedBy")
						.add(Projections.property("txnCode"),"txnCode")
						.add(Projections.property("fee"),"fee")
						.add(Projections.property("refNumber"),"refNumber")
						.add(Projections.property("customerAccountNumber"),"custAccNum")
						.add(Projections.property("agentAccountNumber"),"agentAccNum")
						.add(Projections.property("supAgentCmsn"),"supAgentCmsn")
						//.add(Projections.property("currency"),"currency")
						.add(Projections.sqlProjection("to_date(txndateTime, '"+Constants.SHORT_DATE_PATTERN+"') as txndateTime", new String[] { "txndateTime" }, new Type[] { StandardBasicTypes.DATE }))
						.add(Projections.property("status"),"status"));

				criteria.add(Restrictions.in("responseCode", new String[]{"0", "00"}));

			}else if(ReportCodes.BANKCMSNRPT.toString().equals(report.getReportType())){
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("id"),"id")
						.add(Projections.property("approvedBy"),"approvedBy")
						.add(Projections.property("txnCode"),"txnCode")
						.add(Projections.property("fee"),"fee")
						.add(Projections.property("refNumber"),"refNumber")
						.add(Projections.property("customerAccountNumber"),"custAccNum")
						.add(Projections.property("agentAccountNumber"),"agentAccNum")
						.add(Projections.property("agentCmsn"),"agentCmsn")
						.add(Projections.property("incentive"),"incentive")
						.add(Projections.property("supAgentCmsn"),"supAgentCmsn")
						.add(Projections.property("bankCmsn"),"bankCmsn")
						//.add(Projections.property("currency"),"currency")
						.add(Projections.sqlProjection("to_date(txndateTime, '"+Constants.SHORT_DATE_PATTERN+"') as txndateTime", new String[] { "txndateTime" }, new Type[] { StandardBasicTypes.DATE }))
						.add(Projections.property("status"),"status"));

				criteria.add(Restrictions.eq("merchantId", report.getMerchantId()));
				criteria.add(Restrictions.in("responseCode", new String[]{"0", "00"}));

			}else if(ReportCodes.INBOX.toString().equals(report.getReportType())){
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("id"),"id")
						.add(Projections.property("approvedBy"),"maker")
						.add(Projections.property("txnCode"),"txnCode")
						.add(Projections.property("amount"),"amount")
						.add(Projections.property("refNumber"),"refNumber")
						.add(Projections.property("customerAccountNumber"),"custAccNum")
						.add(Projections.property("agentAccountNumber"),"agentAccNum")
						.add(Projections.property("terminalNumber"),"terminal")
						.add(Projections.property("currency"),"currency")
						.add(Projections.property("status"),"status"));
				criteria.add(Restrictions.eq("approvedBy", report.getUserId()));
			}else{
				execute = false;
				criteria.setProjection(null);
				log.debug("transaction code not matched.");
			}
			if(execute){
				criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				transactions = (List<Transaction>)criteria.list();
			}else{
				session.clear();
				//session.close();
			}
		}catch(Exception e){
			log.error("error while generating report..:"+e.getLocalizedMessage());
		}finally{
			criteria = null;
			session.close();
		}
		log.debug("getAll transaction END");

		return transactions;
	}

	@Override
	public List<Transaction> summaryReport(TransactionReport report) {
		List<Transaction> summary = null;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Transaction.class);
			criteria.setProjection(Projections.projectionList()
					.add(Projections.groupProperty("txnCode"), "txnCode")
					.add(Projections.groupProperty("status"), "status")
					//.add(Projections.sum("agentCmsn"), "agentCmsn")
					//.add(Projections.sum("txnCommission"), "agentCmsn")
					.add(Projections.sqlProjection(" nvl(sum(INCENTIVE + AGENTCMSN), 0) as agentCmsn",new String[]{"agentCmsn"}, new Type[]{StandardBasicTypes.BIG_DECIMAL}))
					.add(Projections.sum("amount"), "amount"));
			criteria.add(Restrictions.eq("merchantId", report.getMerchantId()));
			criteria.add(Restrictions.eq("approvedBy", report.getUserId()));
			criteria.add(Restrictions.eq("status", TransactionStatus.SUCCESS.toString()));
			criteria.add(Restrictions.sqlRestriction("TRUNC(TXNDATETIME) between TO_DATE('" + report.getFromDate() + "','"+Constants.SHORT_DATE_PATTERN+"') AND TO_DATE('" + report.getToDate() + "','"+Constants.SHORT_DATE_PATTERN+"')"));
				criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			summary = (List<Transaction>) criteria.list();
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			session.close();
		}
		return summary;
	}
}
