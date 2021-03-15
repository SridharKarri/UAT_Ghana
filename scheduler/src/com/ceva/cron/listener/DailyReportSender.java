package com.ceva.cron.listener;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.db.model.Alert;
import com.ceva.db.model.DailyReport;
import com.ceva.db.model.Merchant;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.ConfigLoader;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;





public class DailyReportSender
{
	Logger log = Logger.getLogger(DailyReportSender.class);

	@Autowired
	AlertDao alertDao;

	@Autowired
	MerchantDao merchantDao;

	@Autowired
	SessionFactory sessionFactory;


	public void generateDailyReport() {
		log.debug("Startig generateDailyReport.");
		List<DailyReport> dailyReports = alertDao.getDailyReports();
		try {
			if (dailyReports.size() > 0) {
				for (DailyReport dailyReport : dailyReports) {
					try {
						Merchant merchant = merchantDao.getById(dailyReport.getMerchantId());
						log.info("records for dailyReport..:" + 
								dailyReport.getMerchantId());
						String generatedFile = generateDailyFile(dailyReport, merchant);
						dailyReport.setFileLocation(generatedFile);
						dailyReport.setStatus("S");
						alertDao.updateDailyReport(dailyReport);
						insertMailAlert(dailyReport, generatedFile, merchant);
					} catch (Exception e) {
						log.error("Error while generating daily report for :" + dailyReport.getMerchantId());
						log.error("Reason..:" + e.getLocalizedMessage());
						dailyReport.setStatus("E");
						alertDao.updateDailyReport(dailyReport);
						e.printStackTrace();
					} 
				} 
			}
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			dailyReports = null;
		} 
	}
	public void generateDailyReportThreadOne() {
		log.info("Startig generateDailyReportThreadOne.");
		List<DailyReport> dailyReports = alertDao.getDailyReports();
		try {
			if (dailyReports.size() > 0) {
				for (DailyReport dailyReport : dailyReports) {
					try {
						Merchant merchant = merchantDao.getById(dailyReport.getMerchantId());
						log.info("records for dailyReport..:" + 
								dailyReport.getMerchantId());
						String generatedFile = generateDailyFile(dailyReport, merchant);
						dailyReport.setFileLocation(generatedFile);
						dailyReport.setStatus("S");
						alertDao.updateDailyReport(dailyReport);
						insertMailAlert(dailyReport, generatedFile, merchant);
					} catch (Exception e) {
						log.error("Error while generating daily report for :" + dailyReport.getMerchantId());
						log.error("Reason..:" + e.getLocalizedMessage());
						dailyReport.setStatus("E");
						alertDao.updateDailyReport(dailyReport);
						e.printStackTrace();
					} 
				} 
			}
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			dailyReports = null;
		} 
	}
	public void generateDailyReportThreadTwo() {
		log.debug("Startig generateDailyReportThreadTwo.");
		List<DailyReport> dailyReports = alertDao.getDailyReports();
		try {
			if (dailyReports.size() > 0) {
				for (DailyReport dailyReport : dailyReports) {
					try {
						Merchant merchant = merchantDao.getById(dailyReport.getMerchantId());
						log.info("records for dailyReport..:" + 
								dailyReport.getMerchantId());
						String generatedFile = generateDailyFile(dailyReport, merchant);
						dailyReport.setFileLocation(generatedFile);
						dailyReport.setStatus("S");
						alertDao.updateDailyReport(dailyReport);
						insertMailAlert(dailyReport, generatedFile, merchant);
					} catch (Exception e) {
						log.error("Error while generating daily report for :" + dailyReport.getMerchantId());
						log.error("Reason..:" + e.getLocalizedMessage());
						dailyReport.setStatus("E");
						alertDao.updateDailyReport(dailyReport);
						e.printStackTrace();
					} 
				} 
			}
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			dailyReports = null;
		} 
	}
	public void generateDailyReportThreadThree() {
		log.info("Startig generateDailyReportThreadThree.");
		List<DailyReport> dailyReports = alertDao.getDailyReports();
		try {
			if (dailyReports.size() > 0) {
				for (DailyReport dailyReport : dailyReports) {
					try {
						Merchant merchant = merchantDao.getById(dailyReport.getMerchantId());
						log.info("records for dailyReport..:" + 
								dailyReport.getMerchantId());
						String generatedFile = generateDailyFile(dailyReport, merchant);
						dailyReport.setFileLocation(generatedFile);
						dailyReport.setStatus("S");
						alertDao.updateDailyReport(dailyReport);
						insertMailAlert(dailyReport, generatedFile, merchant);
					} catch (Exception e) {
						log.error("Error while generating daily report for :" + dailyReport.getMerchantId());
						log.error("Reason..:" + e.getLocalizedMessage());
						dailyReport.setStatus("E");
						alertDao.updateDailyReport(dailyReport);
						e.printStackTrace();
					} 
				} 
			}
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			dailyReports = null;
		} 
	}
	public void generateDailyReportThreadFour() {
		log.debug("Startig generateDailyReportThreadFour.");
		List<DailyReport> dailyReports = alertDao.getDailyReports();
		try {
			if (dailyReports.size() > 0) {
				for (DailyReport dailyReport : dailyReports) {
					try {
						Merchant merchant = merchantDao.getById(dailyReport.getMerchantId());
						log.info("records for dailyReport..:" + 
								dailyReport.getMerchantId());
						String generatedFile = generateDailyFile(dailyReport, merchant);
						dailyReport.setFileLocation(generatedFile);
						dailyReport.setStatus("S");
						alertDao.updateDailyReport(dailyReport);
						insertMailAlert(dailyReport, generatedFile, merchant);
					} catch (Exception e) {
						log.error("Error while generating daily report for :" + dailyReport.getMerchantId());
						log.error("Reason..:" + e.getLocalizedMessage());
						dailyReport.setStatus("E");
						alertDao.updateDailyReport(dailyReport);
						e.printStackTrace();
					} 
				} 
			}
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			dailyReports = null;
		} 
	}

	public void generateFIReport() {
		String jrxmlLocation = (String)ConfigLoader.convertResourceBundleToMap("config")
				.get("AGENT.SETTLEMENT.FILE");
		String[] biReportEmails = ((String)ConfigLoader.convertResourceBundleToMap("config")
				.get("BI.REPORT.EMAILS")).split(",");
		String[] reportNames = { "TRANSACTION_TYPE_SUMMARY_REPORT", "SUMMARY_TRANSACTION_REPORT", "MERCHANT_SETUP_REPORT", "FINANCIAL_TRANSACTION_REPORT" };

		try {
			for (int i = 0; i < reportNames.length; i++) {
				String filePath = generateReport(jrxmlLocation, reportNames[i]);
				insertNotification(biReportEmails, filePath, reportNames[i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Occured while generating FIS reports..:" + e.getLocalizedMessage());
		} finally {
			jrxmlLocation = null;
		} 
	}

	private void insertNotification(String[] biReportEmails, String filePath, String reportName) {
		log.info("inserting alert..EMAIL");
		try {
			for (int i = 0; i < biReportEmails.length; i++) {
				Alert alert = new Alert();
				StringBuffer message = new StringBuffer();
				message.append("Dear <b>").append(biReportEmails[i].substring(0, biReportEmails[i].indexOf("@"))).append("</b><br/><br/>");
				message.append("<br/> Please view attached.");
				alert.setId((new StringBuilder(String.valueOf(System.nanoTime()))).toString());
				alert.setMessage(message.toString());
				alert.setMobile("");
				alert.setMailBcc("ymehta@gmail.com,suresh@cevaltd.com");
				alert.setMailTo(biReportEmails[i]);
				alert.setAppl("MAIL");
				alert.setTxnType("TRANSACTIONREPORT");
				alert.setSubject(reportName.toLowerCase().replaceAll("_", " "));
				alert.setAttachementLocation(filePath);
				alertDao.save(alert);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while inserting notification for IB reports." + e.getLocalizedMessage());
		} 
	}

	private String generateReport(String jrxmlLocation, String jrxmlName) {
		String reportLocation = "";
		log.info("Generating daily deport..");
		Connection conn = null;
		SessionImpl sessionImpl = null;
		String jrPrint = null;
		JRXlsExporter exporter = null;

		try {
			JasperReport jasperMasterReport = getCompiledReport(String.valueOf(jrxmlLocation) + File.separator + jrxmlName);
			reportLocation = String.valueOf(jrxmlLocation) + File.separator + "FIS" + File.separator + (new SimpleDateFormat("dd-MM-yyyy")).format(new Date()).replaceAll("-", "");
			sessionImpl = (SessionImpl)sessionFactory.openSession();
			conn = sessionImpl.connection();
			File dirs = new File(reportLocation);
			if (!dirs.exists()) {
				dirs.mkdirs();
			}
			reportLocation = String.valueOf(reportLocation) + File.separator + jrxmlName + ".xls";
			File file = new File(reportLocation);
			if (!file.exists()) {
				file.createNewFile();
			}
			Map<String, Object> mapPrams = new HashMap<>();
			jrPrint = String.valueOf(jrxmlLocation) + File.separator + "jasper_report_template.JRprint";
			JasperFillManager.fillReportToFile(jasperMasterReport, jrPrint, mapPrams, conn);
			exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, jrPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, reportLocation);
			exporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error Occured..!" + e.getLocalizedMessage());
			reportLocation = null;
		} finally {
			try {
				conn.close();
				sessionImpl.close();
				jrPrint = null;
				exporter = null;
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} 
		return reportLocation;
	}

	private void insertMailAlert(DailyReport dailyReport, String generatedFile, Merchant merchant) {
		log.info("inserting alert..EMAIL.:" + dailyReport.getMerchantId());
		try {
			Alert alert = new Alert();
			StringBuffer message = new StringBuffer();
			message.append("Dear <b>").append(merchant.getMerchantName()).append("</b><br/><br/>");
			message.append(" Your total transaction amount on :<b>" + (new SimpleDateFormat("dd-MM-yyyy")).format(dailyReport.getGenerationDate()) + ", is :NGN" + dailyReport.getAmount() + "</b>");
			message.append("</b>. <br/> Please view attached transaction report for the breakdown of the commission earned per transaction processed.");
			alert.setMessage(message.toString());
			alert.setMobile(merchant.getMobileNumber());
			alert.setMailTo(merchant.getEmail());
			alert.setAppl("MAIL");
			alert.setTxnType("TRANSACTIONREPORT");
			alert.setSubject("Daily Transaction Report");
			alert.setAttachementLocation(generatedFile);
			alertDao.save(alert);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while inserting notification for IB reports." + e.getLocalizedMessage());
		} 
	}


	private String generateDailyFile(DailyReport dailyReport, Merchant merchant) {
		String path = "";
		log.info("Generating daily deport...");
		Connection conn = null;
		SessionImpl sessionImpl = null;
		String jrxmlLocationFile = null;
		String subReportDir = null;
		String pdfLocation = null;
		String jrPrint = null;
		JRXlsExporter exporter = null;
		try {
			String fromDate = (new SimpleDateFormat("dd-MM-yyyy")).format(dailyReport.getGenerationDate());
			jrxmlLocationFile = (String)ConfigLoader.convertResourceBundleToMap("config")
					.get("AGENT.TXNREPORT.FILE");
			subReportDir = (String)ConfigLoader.convertResourceBundleToMap("config")
					.get("AGENT.TXNSUBREPORT.FILE");
			pdfLocation = (String)ConfigLoader.convertResourceBundleToMap("config")
					.get("AGENT.SETTLEMENT.FILE");
			jrPrint = 
					String.valueOf(ConfigLoader.convertResourceBundleToMap("config").get("AGENT.SETTLEMENT.FILE")) + File.separator + "jasper_report_template.JRprint";

			JasperReport jasperMasterReport = getCompiledReport(jrxmlLocationFile);
			JasperReport jasperSubReport = getCompiledReport(String.valueOf(subReportDir) + "AGENT_SUMMARY_REPORT");

			path = String.valueOf(pdfLocation) + File.separator + fromDate.replace("-", "") + File.separator + dailyReport.getMerchantId() + "_" + System.currentTimeMillis() + ".xls";
			Map<String, Object> mapPrams = new HashMap<>();
			String subSql = "SELECT USERID, COUNT(*) CNT, (SELECT SUM(AMOUNT) FROM TXN_LOG LT WHERE LT.USERID=TL.USERID AND LT.STATUS='SUCCESS' AND TRUNC(TXNDATETIME) = to_date('" + fromDate + "', 'dd-MM-yyyy') AND TXNCODE IN('FTINTERBANK', 'FTINTRABANK', 'CASHDEP', 'DEPWALLET', 'BILLPAYMENT', 'MMO') ) TRNSAMOUNT, (SELECT SUM(AMOUNT) FROM TXN_LOG LT WHERE LT.USERID =TL.USERID AND LT.STATUS='SUCCESS' AND TRUNC(TXNDATETIME) = to_date('" + fromDate + "', 'dd-MM-yyyy') AND TXNCODE IN('CWDBYACT', 'CWDBYOTP', 'CWDBYCRD', 'CWDOTRBNK') ) WITHDRAWAL, SUM(AMOUNT) AMOUNT, SUM(AGENTCMSN) AGENTCMSN FROM TXN_LOG TL WHERE TL.STATUS ='SUCCESS' AND TL.MERCHANTID ='" + merchant.getMerchantID() + "' AND TRUNC(TXNDATETIME) = to_date('" + fromDate + "', 'dd-MM-yyyy') GROUP BY USERID";
			mapPrams.put("QUERY", "SELECT TL.USERID, DECODE(TL.CHANNEL, '1', 'Mobile', '2', 'USSD','5', 'POS', '0', 'Portal', TL.CHANNEL) CHANNEL, TO_CHAR(TL.TXNDATETIME, 'dd-mm-yyyy hh:mm:ss') TXNDATETIME, TL.TOACNUM,TL.FROMACCOUNTNUM FROMACCOUNTNUM, TL.REFNUMBER, DECODE(TL.TXNCODE, 'CASHDEP', 'Cash Deposit', 'FTINTERBANK', 'Other Bank Transfer', 'FTINTRABANK', 'First Bank Tansfer', 'BILLPAYMENT', 'Billpayment', 'CWDBYACT', 'Cash Withdrawal', 'MMO', 'Mobile Recharge', 'AIRTIME', 'Mobile Recharge','CWDBYCRD', 'Card Withdrawal', TL.TXNCODE) TXNCODE, TL.AMOUNT, TL.ERRORCODE, TL.ERRORDESCRIPTION, TL.AGENTCMSN FROM TXN_LOG TL WHERE TL.MERCHANTID='" + merchant.getMerchantID() + "' AND TRUNC(TXNDATETIME) = to_date('" + fromDate + "', 'dd-MM-yyyy')");

			mapPrams.put("SUBQUERY", subSql);
			mapPrams.put("FROM_DATE", fromDate);
			mapPrams.put("TO_DATE", fromDate);
			mapPrams.put("AGENT_ID", merchant.getMerchantID());
			mapPrams.put("AGENT_NAME", merchant.getMerchantName());
			mapPrams.put("SUBREPORT_DIR", subReportDir);
			mapPrams.put("subreport-1", jasperSubReport);

			sessionImpl = (SessionImpl)sessionFactory.openSession();
			conn = sessionImpl.connection();
			File dirs = new File(String.valueOf(pdfLocation) + File.separator + fromDate.replace("-", ""));
			if (!dirs.exists()) {
				dirs.mkdirs();
			}
			JasperFillManager.fillReportToFile(jasperMasterReport, jrPrint, mapPrams, conn);
			exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, jrPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
			exporter.exportReport();
		} catch (Exception e) {
			log.error("Error while generating daily report..:" + dailyReport.getMerchantId());
			log.error("reason..:" + e.getLocalizedMessage());
		} finally {
			try {
				conn.close();
				sessionImpl.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			jrxmlLocationFile = null;
			subReportDir = null;
			pdfLocation = null;
			jrPrint = null;
			exporter = null;
		} 
		return path;
	}


	public JasperReport getCompiledReport(String fileName) throws JRException, Exception {
		log.info(" ABOUT TO COMPILE THE FILE [" + fileName + "]");
		File reportFile = null;
		FileInputStream file = null;

		JasperReport jasperReport = null;
		try {
			reportFile = new File(String.valueOf(fileName) + ".jasper");
			if (!reportFile.exists()) {
				JasperCompileManager.compileReportToFile(String.valueOf(fileName) + ".jrxml", 
						String.valueOf(fileName) + ".jasper");
			}
			file = new FileInputStream(reportFile.getPath());
			jasperReport = (JasperReport)JRLoader.loadObject(file);
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
}