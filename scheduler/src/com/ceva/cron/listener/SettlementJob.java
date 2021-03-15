package com.ceva.cron.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.services.SettlementService;
import com.ceva.bank.services.SettlementSoap;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.AgentCommision;
import com.ceva.db.model.Alert;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Store;
import com.ceva.db.model.SuperAgent;
import com.ceva.dto.ResponseDTO;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

public class SettlementJob {
	Logger log = Logger.getLogger(SettlementJob.class);

	@Autowired
	MerchantDao merchantDao;

	@Autowired
	FeeDao feeDao;
	
	@Autowired
	AlertDao alertDao;

	@Autowired
	SettlementService settlementService;
	
	@Autowired
	SettlementSoap settlementSoap;
	
	@Autowired
	SessionFactory sessionFactory;

	public void createNewSettlementRecord(){
		log.info("createNewSettlementRecord JOB START");
		List<Store> stores= null;
		List<SuperAgent> superAgents = null;
		AgentCommision commision = null;
		try{
			/*merchants = merchantDao.getAgents();
			for(Merchant merchant: merchants){
				log.info("inserting agent for capturing commision month..:"+merchant.getMerchantID());
				commision = new AgentCommision(merchant.getMerchantID(), "A", true);
				feeDao.saveCommision(commision);
			}*/
			stores = merchantDao.getStores();
			for(Store store: stores){
				log.info("inserting agent for capturing commision month..:"+store.getStoreId());
				commision = new AgentCommision(store.getStoreId(), "A", true);
				feeDao.saveCommision(commision);
			}
			/*superAgents = merchantDao.getSuperAgents();
			for(SuperAgent agent: superAgents){
				log.info("inserting super agent for capturing commision month..:"+agent.getId());
				commision = new AgentCommision(agent.getId()+"", "A", false);
				feeDao.saveCommision(commision);
			}*/

		}catch(Exception e){
			log.error("Error While inserting new records for settlement");
			log.error("reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			stores = null;
			superAgents = null;
			commision = null;
		}
		deactiveOldSettlementRecords();
		log.info("createNewSettlementRecord JOB END");
	}

	public void deactiveOldSettlementRecords(){
		log.info("deactivating Previous active SettlementRecords JOB START");
		String date = null;
		Calendar cal=null;
		try{
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			date = new SimpleDateFormat(Constants.DATE_PATTERN).format(cal.getTime());
			log.info("date..:"+date);
			log.debug("deactiveOldSettlementRecords DB");
			feeDao.inactiveCommisions(date, "A", "P");
			log.debug("deactiveOldSettlementRecords END");
		}catch(Exception e){
			log.error("Error While inserting new records for settlement");
			log.error("reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			cal = null;
			date = null;
		}
		log.info("deactivating Previous active SettlementRecords JOB END");
	}
	
	public void ftSettlementAmount(){
		log.info("SettlementAmount JOB START");
		List<AgentCommision> commisions = null;
		String date = null;
		Calendar cal=null;
		ResponseDTO dto = null;
		try{
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			date = new SimpleDateFormat(Constants.DATE_PATTERN).format(cal.getTime());
			log.info("date..:"+date);
			log.debug("ftSettlement commisions from DB");
			commisions = feeDao.getCommisionsToSettlement(Status.P.toString(), Constants.SUCCESS_RESP_CODE);
			log.info("records size..:"+commisions.size());
				if(commisions.size()>0){
					for(AgentCommision agentCommision : commisions){
						agentCommision.setSettlementStatus(Status.I.toString());
						feeDao.update(agentCommision);
					}
					dto = settlementService.processSettlement(commisions);
					if("111".equals(dto.getResponseCode())){
						for(AgentCommision agentCommision : commisions){
							agentCommision.setSettlementDate(new Date());
							agentCommision.setSettlementStatus(Status.B.toString());
							agentCommision.setBatchNumber(dto.getData()+"");
							agentCommision.setResponseMessage(dto.getMessage());
							agentCommision.setResponseCode(dto.getResponseCode());
							feeDao.update(agentCommision);
							agentCommision = null;
						}
					}else{
						for(AgentCommision agentCommision : commisions){
							if(agentCommision.getRetryCount() <= 2){
								agentCommision.setSettlementDate(new Date());
								agentCommision.setSettlementStatus(Status.P.toString());
								agentCommision.setBatchNumber(dto.getData()+"");
								agentCommision.setRetryCount(agentCommision.getRetryCount()+1);
								feeDao.update(agentCommision);
								agentCommision = null;
							}else{
								agentCommision.setSettlementDate(new Date());
								agentCommision.setSettlementStatus(Status.E.toString());
								agentCommision.setBatchNumber(dto.getData()+"");
								agentCommision.setRetryCount(agentCommision.getRetryCount()+1);
								agentCommision.setResponseMessage(dto.getMessage());
								agentCommision.setResponseCode(dto.getResponseCode());
								insertMailForCommissionFail(agentCommision, "");
								feeDao.update(agentCommision);
							}
						}
					}
					
				}
			log.debug("ftSettlementAmount END");
		}catch(Exception e){
			log.error("Error While ftSettlementAmount");
			log.error("reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			cal = null;
			date = null;
			commisions = null;
		}
		log.info("ftSettlementAmount JOB END");
	}

	/*
	 * commented for store level account and commission posting to store level
	 * 
	 * public void ftSettlementAmount(){
		log.info("SettlementAmount JOB START");
		List<AgentCommision> commisions = null;
		String date = null;
		Calendar cal=null;
		ResponseDTO dto = null;
		try{
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			date = new SimpleDateFormat(Constants.DATE_PATTERN).format(cal.getTime());
			log.info("date..:"+date);
			log.debug("ftSettlement commisions from DB");
			commisions = feeDao.getCommisionsToSettlement(Status.P.toString(), Constants.SUCCESS_RESP_CODE);
			log.info("records size..:"+commisions.size());
				if(commisions.size()>0){
					for(AgentCommision agentCommision : commisions){
						agentCommision.setSettlementStatus(Status.I.toString());
						feeDao.update(agentCommision);
					}
					dto = settlementService.processSettlement(commisions);
					for(AgentCommision agentCommision : commisions){
						agentCommision.setSettlementDate(new Date());
						agentCommision.setSettlementStatus(Status.E.toString());
						agentCommision.setBatchNumber(dto.getData()+"");
						agentCommision.setResponseMessage(dto.getMessage());
						agentCommision.setResponseCode(dto.getResponseCode());
						feeDao.update(agentCommision);
						agentCommision = null;
					}
					
				}
			log.debug("ftSettlementAmount END");
		}catch(Exception e){
			log.error("Error While ftSettlementAmount");
			log.error("reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			cal = null;
			date = null;
			commisions = null;
		}
		log.info("ftSettlementAmount JOB END");
	}*/
	
	public void querySettlement(){
		log.info("querySettlement job START");
		List<AgentCommision> commisions = null;
		ResponseDTO responseDTO =null;
		try{
			commisions = feeDao.getCommisionsToSettlement(Status.B.toString(), "111");
			if(commisions != null && commisions.size()>0){
				for(AgentCommision agentCommision : commisions){
					agentCommision.setSettlementStatus(Status.I.toString());
					feeDao.update(agentCommision);
				}
				log.info("commissions for requery...!");
				for(AgentCommision commision : commisions){
					responseDTO = settlementSoap.getCommissionStatusByPayoutId(commision.getCommissionPayoutId());
					commision.setResponseCode(responseDTO.getResponseCode());
					commision.setResponseMessage(responseDTO.getMessage());
					if(Constants.SUCCESS_RESP_CODE.equals(responseDTO.getResponseCode())){
					//if(true){
						String path = generateSettlementFile(commision);
						commision.setSettlementStatus(Status.S.toString());
						insertMailAlert(commision, path);
					}else{
						int retryCount = commision.getRetryCount();
						log.info("Retry Count.:"+retryCount);
						if(retryCount < 3){
							log.debug("retry count lessthan 3, status and commission payout Id will be updated to trigger in next interwal.");
							commision.setRetryCount(retryCount+1);
							commision.setResponseCode(Constants.SUCCESS_RESP_CODE);
							commision.setSettlementStatus(Status.P.toString());
							commision.setCommissionPayoutId(System.currentTimeMillis()+"");
							
						}else{
							log.info("Exceeded maximum retrycount, changing status to failure.");
							commision.setRetryCount(retryCount+1);
							String path = generateSettlementFile(commision);
							insertMailForCommissionFail(commision, path);
							commision.setSettlementStatus(Status.F.toString());
						}
						//commision.setSettlementStatus(Status.F.toString());
					}
					log.info(commision.toString());
					feeDao.update(commision);
				}
			}
		}catch(Exception e){
			log.error("Error While inserting new records for querySettlement");
			log.error("reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void insertMailForCommissionFail(AgentCommision commission,
			String path) {
		log.info("inserting alert..EMAIL");
		Alert alert = new Alert();
		if(commission.isAgent()){
			Store store= merchantDao.getStoreById(commission.getMerchantId());
			String toAddress = ConfigLoader.convertResourceBundleToMap("config").get("mail.support");
			StringBuffer message = new StringBuffer();
			message.append("Dear Support User,").append("</b><br/><br/>");
			message.append("Commission Settlement failed for the store: ").append(store.getStoreName()).append("</b><br/>");
			
			message.append("Reason for failure is : ").append(commission.getResponseMessage()).append("</b><br/><br/>");
			
			message.append("Total commission for the month of :<b>"+theMonth(commission.getCommisionDate().getMonth())+"</b>, is NGN :"+commission.getAmount());
			message.append("<br/> and this has not been paid into Agent operating account number <b>");
			message.append(store.getAccountNumber());
			
			//alert.setMobile(store.getMobileNumber());
			if(path != null){
				message.append("</b>. <br/> Please view attached transaction report for the breakdown of the commission earned per transaction processed>");
				alert.setAttachementLocation(path);
			}
			alert.setMailTo(toAddress);
			alert.setAppl("MAIL");
			alert.setTxnType("SETTLEMENT");
			alert.setSubject("Commission Posting Failed");
			alert.setMessage(message.toString());
			//insertSMSAlert(merchant, commission);
		}
		alertDao.save(alert);
	}

	private void insertMailAlert(AgentCommision commission, String path) {
		log.info("inserting alert..EMAIL");
		Alert alert = new Alert();
		if(commission.isAgent()){
			Merchant merchant = merchantDao.getById(commission.getMerchantId());
			StringBuffer message = new StringBuffer();
			message.append("Dear <b>").append(merchant.getMerchantName()).append("</b><br/><br/>");
			
			message.append(" Your total commission for the month of :<b>"+theMonth(commission.getCommisionDate().getMonth())+"</b>, is NGN :"+commission.getAmount());
			message.append(", this has been paid into your operating account number : <b>");
			message.append(merchant.getAccountNumber());
			message.append("</b>. <br/> Please view attached transaction report for the breakdown of the commission earned per transaction processed.");
			
			alert.setMessage(message.toString());
			alert.setMobile(merchant.getMobileNumber());
			alert.setMailTo(merchant.getEmail());
			alert.setAppl("MAIL");
			alert.setTxnType("SETTLEMENT");
			alert.setSubject("Commission Posting");
			alert.setAttachementLocation(path);
			insertSMSAlert(merchant, commission);
		}
		alertDao.save(alert);
	}
	
	private void insertSMSAlert(Merchant merchant, AgentCommision commission) {
		log.info("inserting alert..SMS");
		Alert alert = new Alert();
		if(commission.isAgent()){
			StringBuffer message = new StringBuffer();
			message.append("Dear ").append(merchant.getMerchantName());
			String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(commission.getCommisionDate());
			String toDate = getLastDate((commission.getCommisionDate().getMonth()+1), (commission.getCommisionDate().getYear()+1900));
			message.append(" Your commission for the transactions from ").append(fromDate).append(" to ").append(toDate);
			message.append(" has been paid to A/C ").append(merchant.getAccountNumber().substring(0, 3)).append("****").append(merchant.getAccountNumber().substring(7, (merchant.getAccountNumber()+"").length()));
			message.append(" AMT : NGN");
			message.append(commission.getAmount());
			message.append(" Ref Num : ");
			message.append(commission.getCommissionPayoutId());
			message.append(" Do More Get More..!");
			
			alert.setMessage(message.toString());
			alert.setMobile(merchant.getMobileNumber());
			alert.setMailTo(merchant.getEmail());
			alert.setAppl("SMS");
			alert.setTxnType("SETTLEMENT");
			alert.setSubject("Commission Posting");
		}
		alertDao.save(alert);
	}

	@SuppressWarnings("deprecation")
	private String generateSettlementFile(AgentCommision commision) {
		String path = "";
		log.info("Generating settlement file..");
		byte[] output = null;
		FileOutputStream outputStream = null;
		try{
			String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(commision.getCommisionDate());
			//String toDate = getLastDate((commision.getCommisionDate().getMonth()+1), (commision.getCommisionDate().getYear()+1900));
			String toDate = new SimpleDateFormat("dd-MM-yyyy").format(commision.getSettlementDate());
			String jrxmlLocationFile = ConfigLoader.convertResourceBundleToMap("config")
					.get("AGENT.JRXML.FILE");
			String pdfLocation = ConfigLoader.convertResourceBundleToMap("config")
					.get("AGENT.SETTLEMENT.FILE");
			path= pdfLocation+File.separator+fromDate.replace("/", "")+File.separator+commision.getMerchantId()+".pdf";
			Map<String, String> mapPrams = new HashMap<String, String>();
			//mapPrams.put("QUERY", "SELECT TL.MERCHANTID, MM.B_OWNER_NAME, MM.ACCOUNT_NUMBER, MM.MOBILE_NO, MM.TELCOTYPE, MM.MAKER_DTTM, TL.TOACNUM, TL.TXNDATETIME, TL.TXNCODE, TL.AMOUNT,MERCHANTID, FEE, TL.AGENTCMSN, TL.REFNUMBER, DECODE(TL.CHANNEL, '1', 'Mobile', '2', 'USSD', '0', 'Portal', TL.CHANNEL) CHANNEL, TL.STATUS  FROM TXN_LOG TL, MERCHANT_MASTER MM WHERE TL.MERCHANTID=MM.MERCHANT_ID AND TL.MERCHANTID='"+commision.getMerchantId()+"' AND TL.STATUS='SUCCESS' AND TRUNC(TXNDATETIME) BETWEEN TO_DATE('"+fromDate+"','dd-mm-yyyy') AND TO_DATE('"+toDate+"','dd-mm-yyyy')");
			mapPrams.put("QUERY", "SELECT TL.MERCHANTID,  MM.B_OWNER_NAME,  TL.USERID,  MM.MOBILE_NO,  MM.MAKER_DTTM,  TL.TOACNUM,  TL.FROMACCOUNTNUM FROMACCOUNTNUM,  TO_CHAR(TL.TXNDATETIME, 'dd-mm-yyyy hh:mm:ss') TXNDATETIME,  DECODE(TL.TXNCODE, 'CASHDEP', 'Cash Deposit', 'FTINTERBANK', 'Other Bank Transfer', 'FTINTRABANK', 'First Bank Tansfer', 'BILLPAYMENT', 'Billpayment', 'CWDBYACT', 'Cash Withdrawal', 'MMO', 'Mobile Recharge', 'AIRTIME', 'Mobile Recharge', TL.TXNCODE) TXNCODE,  TL.AMOUNT,  FEE,  TL.AGENTCMSN,  TL.REFNUMBER,  DECODE(TL.CHANNEL, '1', 'Mobile', '2', 'USSD','5', 'POS', '0', 'Portal', TL.CHANNEL) CHANNEL,  TL.STATUS FROM TXN_LOG TL,  MERCHANT_MASTER MM WHERE TL.MERCHANTID=MM.MERCHANT_ID AND TL.MERCHANTID='"+commision.getMerchantId()+"' AND TL.STATUS = 'SUCCESS' AND TRUNC(TXNDATETIME) BETWEEN TO_DATE('"+fromDate+"','dd-mm-yyyy') AND TO_DATE('"+toDate+"','dd-mm-yyyy')");
			mapPrams.put("FROM_DATE", fromDate);
			mapPrams.put("TO_DATE", toDate);
			SessionImpl sessionImpl = (SessionImpl) sessionFactory.openSession();
			Connection conn = sessionImpl.connection();
			JasperReport jasperReport = getCompiledReport(jrxmlLocationFile);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
					mapPrams, conn);
			output = JasperExportManager.exportReportToPdf(jasperPrint);
			File dirs= new File(pdfLocation+File.separator+fromDate.replace("/", ""));
			if(!dirs.exists()){
				dirs.mkdirs();
			}
			outputStream = new FileOutputStream(path);
			outputStream.write(output, 0, output.length);
			outputStream.flush();
			outputStream.close();
			conn.close();
		}catch(Exception e){
			log.error("Error while generating settlement file..:"+commision.getMerchantId());
			log.error("reason..:"+e.getLocalizedMessage());
		}
		return path;
	}
	
	public JasperReport getCompiledReport(String fileName) throws JRException,
	Exception {
		log.debug(" ABOUT TO COMPILE THE FILE [" + fileName + "]");
		File reportFile = null;
		FileInputStream file = null;
		
		JasperReport jasperReport = null;
		try {
		
			reportFile = new File(fileName + ".jasper");
		
			if (!reportFile.exists()) {
				JasperCompileManager.compileReportToFile(fileName + ".jrxml",
						fileName + ".jasper");
			}
			file = new FileInputStream(reportFile.getPath());
			jasperReport = (JasperReport) JRLoader.loadObject(file);
			log.debug(" COMPLETION OF METHOD");
		} catch (JRException jre) {
			log.error(" JREXCEPTION  " + jre.getMessage());
			throw jre;
		} catch (Exception e) {
			log.error(" EXCEPTION  " + e.getMessage());
			throw e;
		} finally {
			reportFile = null;
			fileName = null;
			if (file != null) {
				file.close();
			}
		}
		return jasperReport;
	}
	

	public void startSettlement(){
		log.info("startSettlement job ");
		try{

		}catch(Exception e){
			log.error("Error While inserting new records for settlement");
			log.error("reason ..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	private static String getLastDate(int month, int year) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month - 1, 1);
	    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
	    Date date = calendar.getTime();
	    DateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy");
	    return DATE_FORMAT.format(date);
	}
	
	public static String theMonth(int month){
	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    return monthNames[month];
	}
	
}

