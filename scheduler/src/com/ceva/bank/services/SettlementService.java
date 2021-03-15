package com.ceva.bank.services;

import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.dataaccess.settlement.ArrayOfCommissionPayout;
import org.datacontract.schemas._2004._07.dataaccess.settlement.CommissionPayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.db.model.AgentCommision;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Store;
import com.ceva.db.model.SuperAgent;
import com.ceva.dto.ResponseDTO;

@Service("settlementService")
public class SettlementService {


	Logger logger = Logger.getLogger(SettlementService.class);

	@Autowired
	MerchantDao merchantDao=null;
	
	@Autowired
	SettlementSoap settlementSoap;

	/*public ResponseDTO processSettlement(List<AgentCommision> commisions){
		logger.info("processSettlement...START:");
		Merchant merchant = null;
		SuperAgent superAgent= null;
		ResponseDTO dto = null;
		long batchId =System.currentTimeMillis();
		try{
			ArrayOfCommissionPayout arrayOfCommissionPayout = new ArrayOfCommissionPayout();
			for(AgentCommision commision : commisions){
				logger.info("settlement for agent..:"+commision.getMerchantId());
				try{
				CommissionPayout payout= new CommissionPayout();
				if(commision.isAgent()){
					merchant = merchantDao.getById(commision.getMerchantId());
					payout.setAgentAccountNumber(new JAXBElement<String>(new QName("http://schemas.datacontract.org/2004/07/DataAccess.BO", "AgentAccountNumber"), String.class, merchant.getAccountNumber()));
				}else{
					//superAgent = merchantDao.superAgentById(commision.getMerchantId());
					superAgent = merchantDao.superAgentByName(commision.getMerchantId());
					payout.setAgentAccountNumber(new JAXBElement<String>(new QName("http://schemas.datacontract.org/2004/07/DataAccess.BO", "AgentAccountNumber"), String.class, superAgent.getAccountNumber()));
				}
				
				payout.setAgentAccruedIncome(commision.getAmount());
				payout.setBatchNumber(new JAXBElement<String>(new QName("http://schemas.datacontract.org/2004/07/DataAccess.BO", "BatchNumber"), String.class, batchId+""));
				payout.setCommissionPayoutId(new JAXBElement<String>(new QName("http://schemas.datacontract.org/2004/07/DataAccess.BO", "CommissionPayoutId"), String.class, commision.getCommissionPayoutId()));
				payout.setTransactionDate(getXMLGregorianCalendar(new Date()));
				logger.info("constructing object completed.");
				arrayOfCommissionPayout.getCommissionPayout().add(payout);
				}catch(Exception e){
					logger.error("Error while creating object");
					logger.error("Reason ..:"+e.getLocalizedMessage());
				}
			}
			dto = settlementSoap.postAgentCommission(arrayOfCommissionPayout);
			dto.setData(batchId+"");
			logger.info(dto.toString());
		}catch(Exception e){
			logger.error("Error while processSettlement");
			logger.error("Reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			merchant = null;
			superAgent = null;
		}
		logger.info("processSettlement...END:");
		return dto;
	}*/
	
	public ResponseDTO processSettlement(List<AgentCommision> commisions){
		logger.info("processSettlement...START:");
		Merchant merchant = null;
		Store store = null;
		SuperAgent superAgent= null;
		ResponseDTO dto = null;
		long batchId =System.currentTimeMillis();
		try{
			ArrayOfCommissionPayout arrayOfCommissionPayout = new ArrayOfCommissionPayout();
			for(AgentCommision commision : commisions){
				logger.info("settlement for store..:"+commision.getMerchantId());
				try{
				CommissionPayout payout= new CommissionPayout();
				store = merchantDao.getStoreById(commision.getMerchantId());
				payout.setAgentAccountNumber(new JAXBElement<String>(new QName("http://schemas.datacontract.org/2004/07/DataAccess.BO", "AgentAccountNumber"), String.class, store.getAccountNumber()));
				
				payout.setAgentAccruedIncome(commision.getAmount());
				payout.setBatchNumber(new JAXBElement<String>(new QName("http://schemas.datacontract.org/2004/07/DataAccess.BO", "BatchNumber"), String.class, batchId+""));
				payout.setCommissionPayoutId(new JAXBElement<String>(new QName("http://schemas.datacontract.org/2004/07/DataAccess.BO", "CommissionPayoutId"), String.class, commision.getCommissionPayoutId()));
				payout.setTransactionDate(getXMLGregorianCalendar(new Date()));
				logger.info("constructing object completed.");
				arrayOfCommissionPayout.getCommissionPayout().add(payout);
				}catch(Exception e){
					logger.error("Error while creating object");
					logger.error("Reason ..:"+e.getLocalizedMessage());
				}
			}
			dto = settlementSoap.postAgentCommission(arrayOfCommissionPayout);
			dto.setData(batchId+"");
			logger.info(dto.toString());
		}catch(Exception e){
			logger.error("Error while processSettlement");
			logger.error("Reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			store = null;
			superAgent = null;
		}
		logger.info("processSettlement...END:");
		return dto;
	}
	
	public static XMLGregorianCalendar getXMLGregorianCalendar(Date date) {
	    java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();        
	    calDate.setTime(date);
	    javax.xml.datatype.XMLGregorianCalendar calendar = null;
	        try {
	            javax.xml.datatype.DatatypeFactory factory = javax.xml.datatype.DatatypeFactory.newInstance();
	            calendar = factory.newXMLGregorianCalendar(
	            calDate.get(java.util.GregorianCalendar.YEAR),
	            calDate.get(java.util.GregorianCalendar.MONTH) + 1,
	            calDate.get(java.util.GregorianCalendar.DAY_OF_MONTH),
	            calDate.get(java.util.GregorianCalendar.HOUR_OF_DAY),
	            calDate.get(java.util.GregorianCalendar.MINUTE),
	            calDate.get(java.util.GregorianCalendar.SECOND),
	            calDate.get(java.util.GregorianCalendar.MILLISECOND), 0);
	        } catch (DatatypeConfigurationException dce) {
	            //handle or throw
	        }   
	    return calendar;
	}

}
