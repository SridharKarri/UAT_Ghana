package com.ceva.cron.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.utils.CreateTemplate;
import com.ceva.db.model.Alert;
import com.ceva.service.MailService;
import com.ceva.vo.AbstractVO;
import com.ibm.icu.text.SimpleDateFormat;

public class InternalReport {
	
	Logger log = Logger.getLogger(InternalReport.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	MailService mailService;

	@Autowired
	AlertDao alertDao;
	
	private static ResourceBundle configBundle;

	static {
		configBundle = ResourceBundle.getBundle("config");
	}
	
	
	public void generateInternalReport() {
		String lineBreak="<br/><br/>";
		String singleLineBreak="<br/>";
		log.debug("Startig generateInternalReport.");
		StringBuffer messageString=new StringBuffer();
		try {
			messageString.append("Total transactions since inception. ").append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append("| TXN COUNT  | TXN SUM       |  AVG TXN AMOUNT |").append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(getTotalTransactionsTillToday()).append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("Total daily since 30 Days. ").append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+----------------+").append(singleLineBreak);
			messageString.append("|   DATE     | TXN SUM       | TXN COUNT | AVG TXN AMOUNT |").append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(getLastThirtyDaysAverages()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+");
			messageString.append(lineBreak);
			
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append("Total Agents Created :").append(getTotalAgents()).append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append("Total Active Agents :").append(getTotalActiveAgents()).append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("Agents creation in last 7 days ").append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+----------------+").append(singleLineBreak);
			messageString.append("|   DATE     |  COUNT   |").append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(getNewAgentsCreatedThisWeek()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("Transaction summary on "+ getDate()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+----------------+").append(singleLineBreak);
			messageString.append("|   TXN TYPE       |  SUCC CNT   |  SUCC AMT   |    FAIL CNT   |    FAIL AMT   |   TXN COUNT  |   TOTAL AMT |").append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(getTransactionSummaryOfYesterday()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("Top 5 Agents with highest transactions on "+getDate()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+----------------+").append(singleLineBreak);
			messageString.append("|   AGENT ID     |  TXN COUNT    | AMOUNT |").append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(getAgentWithHighestTransactionsToday()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append("Agents with morethan 50 txns :"+getAgentsWithMorethanFiftyTransactions(50)).append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append("Agents with morethan 30 txns :"+getAgentsWithMorethanThirtyTransactions(30)).append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(lineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append("Agents with morethan 10 txns :"+getAgentsWithMorethanTenTransactions()).append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(lineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append("Agents with lessthan 10 txns :"+getAgentsWithLessthanTenTransactions()).append(singleLineBreak);
			messageString.append("-----------------------------------------------------------").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("Failure Response Analysis "+ getDate()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+----------------+").append(singleLineBreak);
			messageString.append("|  RESPONSECODE   |  COUNT | RESPONSE MESSAGE |").append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(getFailureResponseAnalysis()).append(singleLineBreak);
			messageString.append("+------------+---------------+-----------+---------------+").append(singleLineBreak);
			messageString.append(lineBreak);
			
			messageString.append("Thanks & Best Regards.");
			
			postAlert(messageString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			messageString=null;
			lineBreak=null;
		}
	}

	private Object getTotalActiveAgents() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT COUNT(DISTINCT MERCHANTID) FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1)";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1));
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getTotalAgents..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	private String getAgentsWithLessthanTenTransactions() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT COUNT(*) FROM (SELECT MERCHANTID, COUNT(*) FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND  STATUS='SUCCESS' GROUP BY MERCHANTID HAVING COUNT(*) <= 10)";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1));
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getNewAgentsCreatedThisWeek..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	private void postAlert(StringBuffer messageString) {
		String[] emails= null;
		try{
			emails = configBundle.getString("894.REPORT.EMAILS").split(",");
			if(emails.length>0){
				String email = "";
				for(int i=0;i<emails.length; i++){
					email=emails[i];
					log.info("sending 894 email to .:"+email);
					Alert alert= new Alert();
					alert.setMailTo(email);
					alert.setFetchStatus("P");
					alert.setMessage(messageString+"");
					alert.setMessageDate(new Date());
					alert.setRetryCount(0);
					alert.setTxnType("NOTIFICATION");
					alert.setId(System.nanoTime()+"");
					alert.setSubject("Agent Banking Report on :"+getDate());
					
					AbstractVO vo =CreateTemplate.createTemplateVO(alert);
					boolean sent = mailService.sendMail(vo);
					log.info("894 email sernt...?"+sent);
					if(sent){
						alert.setFetchStatus("S");
					}else{
						alert.setFetchStatus("E");
					}
					alert.setMessage("");
					alertDao.save(alert);
				}
			}else{
				log.error("Please configure 894.REPORT.EMAILS in config.");
			}
		}catch(Exception e){
			log.error("Please configure 894.REPORT.EMAILS in config.");
			log.error("error..:"+e.getLocalizedMessage());
		}finally{
			emails=null;
		}
		
	}

	private String getAgentsWithMorethanTenTransactions() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT COUNT(*) FROM (SELECT MERCHANTID, COUNT(*) FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND  STATUS='SUCCESS' GROUP BY MERCHANTID HAVING COUNT(*) > 10 AND COUNT(*) <=30)";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1));
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getNewAgentsCreatedThisWeek..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	private String getAgentsWithLessthanThirtyTransactions(int i) throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT COUNT(*) FROM (SELECT MERCHANTID, COUNT(*) FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND  STATUS='SUCCESS' GROUP BY MERCHANTID HAVING COUNT(*) >=10 and count(*) <30)";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1));
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getNewAgentsCreatedThisWeek..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	private String getAgentsWithMorethanFiftyTransactions(int count) throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT COUNT(*) FROM (SELECT MERCHANTID, COUNT(*) FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND  STATUS='SUCCESS' GROUP BY MERCHANTID HAVING COUNT(*)  > 50)";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1));
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getNewAgentsCreatedThisWeek..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}
	
	private String getAgentsWithMorethanThirtyTransactions(int count) throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT COUNT(*) FROM (SELECT MERCHANTID, COUNT(*) FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND  STATUS='SUCCESS' GROUP BY MERCHANTID HAVING COUNT(*) >=30 AND COUNT(*) <= 50)";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1));
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getNewAgentsCreatedThisWeek..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	public String getDate() {
	       SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	       Calendar cal = Calendar.getInstance();
	       cal.add(Calendar.DATE, -1);
	       return dateFormat.format(cal.getTime());
	  }

	private String getAgentWithHighestTransactionsToday() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT MERCHANTID, CNT, AMT FROM ( SELECT MERCHANTID, COUNT(*) AS CNT, SUM(AMOUNT) AMT FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND STATUS='SUCCESS'  GROUP BY MERCHANTID ORDER BY CNT desc)  where rownum <=5 ORDER BY AMT desc";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1)).append(" | ");
				message.append(rs.getString(2)).append("    | ");
				message.append(rs.getString(3)).append(" | <br/>");
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getAgentWithHighestTransactionsToday..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	private String getFailureResponseAnalysis() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT RESPONSECODE, COUNT(*), ERRORDESCRIPTION FROM TXN_LOG WHERE STATUS='FAILURE' AND TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) GROUP BY RESPONSECODE,ERRORDESCRIPTION";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1)).append(" | ");
				message.append(rs.getString(2)).append("      | ");
				message.append(rs.getString(3)).append(" | <br/>");
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getFailureResponseAnalysis..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}



	private String getTransactionSummaryOfYesterday() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			//String qry="SELECT decode(TXNCODE, 'CWDOTRBNK', 'Otherbank Cash withdrawal', 'BILLPAYMENT', 'Billpayment', 'FTINTRABANK', 'Fbn Transfer','FTINTERBANK', 'Other Bank Transfer', 'CASHDEP', 'Cash Deposit', 'MMO', 'Mobile Recharge', 'CWDBYACT', 'Cash withdrawal using Account', TXNCODE), COUNT(*), SUM(AMOUNT) FROM TXN_LOG WHERE TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND STATUS='SUCCESS' GROUP BY TXNCODE";
			String qry = "select  decode(TXNCODE, 'CWDOTRBNK', 'Otherbank Cash withdrawal', 'BILLPAYMENT', 'Billpayment', 'FTINTRABANK', 'Fbn Transfer','FTINTERBANK', 'Other Bank Transfer', 'CASHDEP', 'Cash Deposit', 'MMO', 'Mobile Recharge', 'CWDBYACT', 'Cash withdrawal using Account', TXNCODE), sum(SUCCNT), sum(SUCCSUM), SUM(FAILCNT), sum(FAILSUM), sum(SUCCNT+FAILCNT), sum(SUCCSUM+FAILSUM) from ( "
			+" SELECT TXNCODE, COUNT(*) SUCCNT, 0 as FAILCNT, 0 as FAILSUM, SUM(AMOUNT) SUCCSUM FROM TXN_LOG where TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND STATUS='SUCCESS' GROUP BY STATUS, TXNCODE "
			+" union all SELECT TXNCODE, 0 as SUCCNT, COUNT(*) FAICNT, SUM(AMOUNT) FAILSUM, 0 as SUCCSUM FROM TXN_LOG where TRUNC(TXNDATETIME)=TRUNC(SYSDATE-1) AND STATUS='FAILURE' GROUP BY STATUS, TXNCODE) group by TXNCODE";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1)).append(" | ");
				message.append(rs.getString(2)).append("          | ");
				message.append(rs.getString(3)).append("          | ");
				message.append(rs.getString(4)).append("          | ");
				message.append(rs.getString(5)).append("          | ");
				message.append(rs.getString(6)).append("          | ");
				message.append(rs.getString(7)).append(" | <br/>");
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getTransactionSummaryOfYesterday..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}



	private String getNewAgentsCreatedThisWeek() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry = "SELECT \tTO_CHAR(MAKER_DTTM, 'dd-mon-yyyy'), COUNT(*) FROM MERCHANT_MASTER WHERE TRUNC(MAKER_DTTM) BETWEEN TRUNC(SYSDATE-8) and TRUNC(SYSDATE-1) GROUP BY TO_CHAR(MAKER_DTTM, 'dd-mon-yyyy')";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1)).append(" | ");
				message.append(rs.getString(2)).append("     |<br/>");
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getNewAgentsCreatedThisWeek..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}


	private String getTotalAgents() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry="SELECT COUNT(*) FROM MERCHANT_MASTER";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1));
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getTotalAgents..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	private String getTotalTransactionsTillToday() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry = "SELECT COUNT(*), SUM(AMOUNT), ROUND(AVG(AMOUNT), 3) FROM TXN_LOG WHERE STATUS='SUCCESS'";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1)).append("         | ");
				message.append(rs.getString(2)).append("     | ");
				message.append(rs.getString(3)).append("   |<br/>");
			}

		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getTotalTransactionsTillToday..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}

	private String getLastThirtyDaysAverages() throws SQLException {
		StringBuilder message= new StringBuilder();
		Connection conn =null;
		PreparedStatement pstmt=null;
		SessionImpl sessionImpl=null;
		try{
			sessionImpl = (SessionImpl) sessionFactory.openSession();
			conn = sessionImpl.connection();
			String qry = "SELECT TRUNC(TXNDATETIME), SUM(AMOUNT), COUNT(*), ROUND(AVG(AMOUNT),3) FROM TXN_LOG WHERE TRUNC(TXNDATETIME) BETWEEN TRUNC(SYSDATE-31) AND TRUNC(SYSDATE-1) AND STATUS='SUCCESS' GROUP BY TRUNC(TXNDATETIME) ORDER BY trunc(TXNDATETIME) DESC";
			pstmt=conn.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				message.append(rs.getString(1).substring(0, 10)).append(" | ");
				message.append(rs.getString(2)).append("    | ");
				message.append(rs.getString(3)).append("     | ");
				message.append(rs.getString(4)).append(" |<br/>");
			}
	
		}catch(Exception e){
			e.printStackTrace();
			log.error("reason getLastThirtyDaysAverages..:"+e.getLocalizedMessage());
		}finally{
			conn.close();
			sessionImpl.close();
		}
		return message.toString();
	}


}

