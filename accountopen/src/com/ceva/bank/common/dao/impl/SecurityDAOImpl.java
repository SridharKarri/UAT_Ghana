package com.ceva.bank.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.bank.utils.Status;
import com.ceva.crypto.tools.AESCBCEncryption;
import com.ceva.db.model.AppVersion;
import com.ceva.db.model.ChannelKeys;
import com.ceva.db.model.ChannelUsers;
import com.ceva.db.model.Fraud;
import com.ceva.db.model.UserLoginCredentials;

public class SecurityDAOImpl {

	@Autowired
	SessionFactory sessionFactory;

	private static Logger logger = Logger.getLogger(SecurityDAOImpl.class);

	public String logRequest(Map<String, String> requestMap) {
		return null;
	}


	public SecurityDAOImpl() {
		super();
	}

	public Boolean isDeviceLocked(String imei) {

		boolean isLocked = true;
		Session session = null;
		Criteria criteria = null;
		ChannelUsers checkBean = null;
		logger.debug("Inside isDeviceLocked Method");
		try {
			logger.debug(" imei : " + imei);
			session = sessionFactory.openSession();
			criteria = session.createCriteria(ChannelUsers.class);
			criteria.add(Restrictions.eq("imeiNo", imei));
			//criteria.add(Restrictions.eq("cannelId", "MOBILE"));
			criteria.add(Restrictions.eq("status", "L"));
			checkBean = (ChannelUsers) criteria.uniqueResult();
			if (checkBean == null) {
				logger.debug("Device is Not Locked..");
				isLocked = false;
			}
			//session.evict(checkBean);
		} catch (Exception e) {
			logger.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			session.close();
			session = null;
			criteria = null;
			checkBean = null;
		}
		return isLocked;
	}

	public String fetchMasterKey(String channelID) {
		Session session = null;
		try {
			session=sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ChannelKeys.class);
			criteria.add(Restrictions.eq("channelId", Long.parseLong(channelID)));
			ChannelKeys channelKeys = (ChannelKeys) criteria.uniqueResult();
			return channelKeys.getEncKey();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error While loading key..:"+e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}
	
	public AppVersion fetchMinAppVersion(String channelID) {
		Session session = null;
		try {
			session=sessionFactory.openSession();
			Criteria criteria = session.createCriteria(AppVersion.class);
			criteria.add(Restrictions.eq("channelId", channelID));
			AppVersion appVersion = (AppVersion) criteria.uniqueResult();
			return appVersion;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error While fetchMinAppVersion..:"+e.getLocalizedMessage());
			return null;
		} finally {
			session.close();
		}
	}

	public String fetchRequestKey(String sessionID, String channelID) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String requestKey = null;

		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				if (pstmt != null)
					pstmt.close();

				if (connection != null)
					connection.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public ChannelUsers fetchSecurityData(String appId) {
		ChannelUsers checkBean = null;
		Criteria criteria = null;
		org.hibernate.Session session = null;
		try {
			logger.debug("Inside fetch Security Data Method [APPID : " + appId);
			session = getSessionFactory().openSession();
			logger.debug("session created.");
			criteria = session.createCriteria(ChannelUsers.class);
			criteria.add(Restrictions.eq("appId", appId));
			logger.debug("restrictions added.");
			checkBean = (ChannelUsers) criteria.uniqueResult();
			session.evict(checkBean);
			if (checkBean == null) {
				logger.debug("no data found with app id, "+appId);
				throw new Exception("Invalid Login.");
			}else{
				logger.debug("bean loaded");
			}
		} catch (SQLException e) {
			logger.error("Exception fetch Security Data.... " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception fetch Security Data.... " + e.getMessage());
			e.printStackTrace();
		} finally {
			session.close();
			criteria=null;
			session = null;
		}
		return checkBean;
	}

	public boolean updateSessionIV(String appId, String newIV) {
		logger.debug("Inside updateSessionIV Method....:"+appId);
		int res = 0;
		String hql =null;
		boolean isUpdated = false;
		Session session= null;
		try{
			if(appId != null){
				hql="update ChannelUsers set sIv=:sIv, lastRequestTime=:lastRequestTime where appId=:appId";
				session = sessionFactory.openSession();
				Query query = session.createQuery(hql); 
				query.setParameter("sIv", newIV);
				query.setParameter("lastRequestTime", new Date());
				query.setParameter("appId", appId);
				res = query.executeUpdate();
				logger.debug("rows updated.....:"+res);
				session.flush();
				isUpdated = res > 0 ? true : false;
				}
			}
		catch(Exception e){
			logger.error("Error While updating siv, reason:"+e.getLocalizedMessage());
			e.printStackTrace();
			isUpdated = false;
		}
		finally{
			session.close();
			res = 0;
			hql = null;
		}
		return isUpdated;
	}
	
	public boolean updateAppVersion(String appId, String version) {
		logger.debug("Inside updateAppVersion Method....");
		int res = 0;
		String hql =null;
		boolean isUpdated = false;
		Session session= null;
		try{
			hql="update ChannelUsers set version=:version, lastRequestTime=:lastRequestTime where appId=:appId";
			session = sessionFactory.openSession();
			Query query = session.createQuery(hql); 
			query.setParameter("version", version);
			query.setParameter("lastRequestTime", new Date());
			query.setParameter("appId", appId);
			res = query.executeUpdate();
			session.flush();
			isUpdated = res > 0 ? true : false;
			}
		catch(Exception e){
			logger.error("Error While updateAppVersion, reason:"+e.getLocalizedMessage());
			e.printStackTrace();
			isUpdated = false;
		}
		finally{
			session.close();
			res = 0;
			hql = null;
		}
		return isUpdated;
	}

	public boolean updateChannelUser(ChannelUsers channelUser){
		logger.debug("Inside update ChannelUser....");
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			session.saveOrUpdate(channelUser);
			session.flush();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Error While Updating :"+e.getLocalizedMessage());
			return false;
		}finally{
			session.close();
		}

	}
	public boolean updateToken(String token,String appId){
		logger.debug("Inside insertResponseRandomKey Method....");
		Connection connection = null;
		PreparedStatement pstmt = null;
		int rows = 0;
		//String pin = null;

		try
		{/*
			connection = ConnectionHelper.getConnection();
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(UPDATE_TOKEN);

			pstmt.setString(1, token);
			pstmt.setString(2, appId);

			rows = pstmt.executeUpdate();

			logger.debug("Total records updated are .... " + rows);

			connection.commit();

			return true;
		*/}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			try {

				if (pstmt != null)
					pstmt.close();

				if (connection != null)
					connection.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean noteTransaction(Map<String,String> transMap){
		logger.debug("Inside noteTransaction Method....");
		Connection connection = null;
		PreparedStatement pstmt = null;

		try{

			/*
			connection = ConnectionHelper.getConnection();
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(NOTICE_TRANS);
			pstmt.setString(1, transMap.get("APPID"));
			pstmt.setString(2, transMap.get("IP"));
			pstmt.setString(3, transMap.get("REQUEST_URL"));
			pstmt.setString(4, transMap.get("ERROR_DESC"));
			pstmt.setString(5, transMap.get("REQUEST_CHANNEL"));
			pstmt.setString(6, "");


			pstmt.executeUpdate();

			connection.commit();

			return true;

		*/}
		catch(Exception e){
			logger.error("ERROR WHILE TRACKING TRANSACTION ERROR : "+e.getLocalizedMessage()+"]");
		}
		finally{
			try {

				if (pstmt != null)
					pstmt.close();

				if (connection != null)
					connection.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return  false;
	}

	public boolean noteFraud(Map<String,String> errorMap)
	{
		logger.debug("Inside noteFraud Method....");
		boolean isInserted = false;
		Session session = null;
		try {
			Fraud fraud = new Fraud();
			fraud.setId(System.currentTimeMillis()+"");
			fraud.setAppId(errorMap.get("APPID"));
			fraud.setErrorDescription(errorMap.get("ERROR_DESC"));
			fraud.setIpAddress(errorMap.get("IP"));
			fraud.setUrl(errorMap.get("REQUEST_URL"));
			fraud.setUserChannel(errorMap.get("REQUEST_CHANNEL"));
			session = sessionFactory.openSession();
			session.save(fraud);
			session.flush();
			isInserted = true;
		} catch (Exception e) {
			logger.error("Error Occured..:" + e.getLocalizedMessage());
			e.printStackTrace();
			isInserted = false;
		} finally {
			session.close();
		}
		logger.debug("inserted...:"+isInserted);
		return isInserted;
	}


	public boolean isUserActive(String userId){
		boolean isActive = false;
		UserLoginCredentials credentials = null;
		try{
			
		}
		catch(Exception e){
			e.printStackTrace();
			isActive = false;
		}

		return isActive;
	}
	
	public ChannelUsers prepareAppLogin(String userId, String imeiNo, String channelId, String serialNo, String macAddr, String version, String deviceIp)
	{
		logger.debug("preparing app login for...:"+userId);
		String key=null;
		String iosKey=null;
		String appId=null;
		String piv = null;
		String siv = null;
		UserLoginCredentials credentials = null;
		Session session = null;
		ChannelUsers userChannel = null;
		try{
			key = AESCBCEncryption.base64Encode(AESCBCEncryption.generateSessionKey());
			appId = String.valueOf(System.nanoTime());
			//appId = imeiNo;
			siv = AESCBCEncryption.base64Encode(AESCBCEncryption.generateIV());
			piv = AESCBCEncryption.base64Encode(AESCBCEncryption.generateIV());
			iosKey = AESCBCEncryption.base64Encode(AESCBCEncryption.generateSessionKey());
			logger.debug("created keys...");
			session = sessionFactory.openSession();
		
			userChannel = (ChannelUsers) session.createCriteria(ChannelUsers.class)
					.add(Restrictions.eq("imeiNo", imeiNo)).uniqueResult();
			if(userChannel != null){
				logger.debug("device found..:");
				userChannel.setCannelId("APP");
				userChannel.setStatus(Status.A.toString());
				userChannel.setpIv(piv);
				userChannel.setsIv(siv);
				userChannel.setpKey(key);
				//userChannel.setCommonId(credentials.getCommonId());
				//userChannel.setAppId(appId);
				//userChannel.setImeiNo(imeiNo);
				userChannel.setpToken("1234");
				userChannel.setActiveLogin(new Long(0));
				userChannel.setSessionId(iosKey);
				userChannel.setLastRequestTime(new Date());
				userChannel.setDeviceIp(deviceIp);
				userChannel.setMacAddr(macAddr);
				userChannel.setSerialNo(serialNo);
				userChannel.setVersion(version);
				
				logger.debug(userChannel.toString());
				session.merge(userChannel);
				session.flush();
			}else{
				logger.debug("device not found..:");
				credentials = (UserLoginCredentials) session.createCriteria(UserLoginCredentials.class)
						.add(Restrictions.eq("userId", userId)).uniqueResult();
				ChannelUsers channelUser2= new ChannelUsers();
				channelUser2.setCannelId("APP");
				//channelUser2.setUserLoginCredentials(credentials);
				channelUser2.setCannelValue(credentials.getMobileNo());
				channelUser2.setStatus(Status.A.toString());
				channelUser2.setpIv(piv);
				channelUser2.setsIv(siv);
				channelUser2.setpKey(key);
				channelUser2.setAppId(appId);
				channelUser2.setpToken("12344");
				channelUser2.setActiveLogin(new Long(0));
				channelUser2.setSessionId(iosKey);
				channelUser2.setMerchantId(credentials.getAgent());
				channelUser2.setStoreId(credentials.getStore());
				channelUser2.setImeiNo(imeiNo);
				channelUser2.setLastRequestTime(new Date());
				channelUser2.setDeviceIp(deviceIp);
				channelUser2.setMacAddr(macAddr);
				channelUser2.setSerialNo(serialNo);
				channelUser2.setVersion(version);
				channelUser2.setCommonId(credentials.getCommonId());
				logger.debug(channelUser2.toString());
				session.save(channelUser2);
				session.flush();
				userChannel=channelUser2;
			}
		}	
		catch(Exception e){
			e.printStackTrace();
			logger.error("Error..:"+e.getLocalizedMessage());
		}
		finally{
			session.close();
			key=null;
			iosKey=null;
			appId=null;
			piv = null;
			siv = null;
		}
		return userChannel;
	}

	private boolean isUserActive(Date now, Date dbDate) {
		logger.debug("isUserActive...:"+dbDate);
		boolean isActive = true;
		try{
			if(dbDate != null){
				long timeDiff = (now.getTime()-dbDate.getTime())/(1000*60);
				logger.debug("timeDiff in minutes...:"+timeDiff%60);
				if(timeDiff >= 3){
					//logger.info("timeDiff in minutes, is greater than ideal time");
					isActive = false;
				}else{
					logger.debug("timeDiff in minutes, is less than ideal time, identified active user.");
				}
			}else{
				isActive = false;
				logger.debug("dbDate is null...:"+isActive);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			isActive = false;
		}

		return isActive;
	}


	public static void main(String args[])
	{
		//System.out.println(new SecurityDAOImpl().insertResponseRandomKey("hHbsbSSSnnsh12ans*ssn","/yrkPpI+Gb03KCIf+GUnKKUb+yfuYH2FFbT0vdZ1ynA=","1"));
		//System.out.println("KEY : "+new securityDAO().fetchRequestKey("/yrkPpI+Gb03KCIf+GUnKKUb+yfuYH2FFbT0vdZ1ynA=","1"));
	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	/*public ChannelData isUserActive(String string, String getpId,
			String string2, String sessionId, String string3, String string4) {
		// TODO Auto-generated method stub
		return null;
	}*/


	public boolean updateLastLoginTime(String string, String appid,
			String string2) {
		// TODO Auto-generated method stub
		return false;
	}


	public void updateChannelUser(String appId) {
		logger.debug("Inside updateChannelUser logout....");
		int res = 0;
		String hql =null;
		Session session = null;
		try{
			hql="update ChannelUsers set lastRequestTime=:lastRequestTime where appId=:appId";
			session = sessionFactory.openSession();
			Query query = session.createQuery(hql);
			query.setParameter("lastRequestTime",getDateMinusMinutes());
			query.setParameter("appId", appId);
			res = query.executeUpdate();
			session.flush();
			}
		catch(Exception e){
			logger.error("Error While changing last request time:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		finally{
			session.close();
			res = 0;
			hql = null;
		}
	}

	private Date getDateMinusMinutes(){
		Calendar now = Calendar.getInstance();
	    now = Calendar.getInstance();
	    now.add(Calendar.MINUTE, -5);
	    return now.getTime();
	}
}






