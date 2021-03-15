package com.ceva.bank.common.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.beans.ChangePin;
import com.ceva.bank.common.beans.DeviceRegister;
import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.Alert;
import com.ceva.db.model.ChannelUsers;
import com.ceva.db.model.OtpBean;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@Repository("loginDao")
public class LoginDaoImpl implements LoginDao
{
  Logger log = Logger.getLogger(LoginDaoImpl.class);
  
  @Autowired
  SessionFactory sessionFactory;

  @Autowired
  MerchantDao merchantDao;

  @Autowired
  AlertDao alertDao;
  
  @Override
  public ResponseDTO login(UserLoginCredentials userBean) {
	    log.debug("User Validation START.");
	    ResponseDTO responseDTO = new ResponseDTO();
	    List<ChannelUsers> channelUsers = null;
	    long invalidPwdCnt = 0;
	    boolean isActiveChannel = false;
	    Date date = new Date();
	    int activeChannelCount=0;
	    Session session = null;
	    try
	    {
	      session = sessionFactory.openSession();
	      Criteria criteria = session
	        .createCriteria(UserLoginCredentials.class);
	      criteria.add(Restrictions.eq("userId", userBean.getUserId()));
	      UserLoginCredentials userLoginCredentials = (UserLoginCredentials)criteria
	        .uniqueResult();
	      if (userLoginCredentials != null){
	        if("A".equals(userLoginCredentials.getStatus()) || "F".equals(userLoginCredentials.getStatus())){
	        	boolean isValid=CommonUtil.validatePin(userBean.getPin(), userLoginCredentials.getPin(), "1");
	        	//boolean isValid=true;
	        	//if(userLoginCredentials.getPin().equals(userBean.getPin())){
	        	if(isValid){
	        		channelUsers = getChannelUsers(userLoginCredentials.getCommonId());
	        		for(ChannelUsers channelUser : channelUsers){
	        			isActiveChannel = isUserActive(date, channelUser.getLastRequestTime());
	        			log.debug("isActiveChannel..:"+isActiveChannel);
	        			if(isActiveChannel){
	        				activeChannelCount++;
	        			}
	        		}
	        		log.debug("active channels count..:"+activeChannelCount);        		
	        		if(activeChannelCount <= 1){
		    			responseDTO.setData(userLoginCredentials);
		    	        userLoginCredentials.setLastLoggedIn(new Date());
		    	        userLoginCredentials.setInvalidPwdCount(new Long(0));
		    			responseDTO.setMessage(Constants.SUCESS_SMALL);
		    			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
	        		}else{
	        			responseDTO.setMessage(Constants.USER_TRANSACT_OTHERDEVICE_MESSAGE);
		    			responseDTO.setResponseCode(Constants.USER_LOCK_CODE);
	        		}
	        	}else{
	    			log.debug("pin validation failed, updating invalid password counts");
	    			invalidPwdCnt = userLoginCredentials.getInvalidPwdCount();
	    			log.debug("invalidPwdCnt...:"+invalidPwdCnt);
	    			if(invalidPwdCnt <2){
	    				invalidPwdCnt = invalidPwdCnt+1;
	    				log.debug("updating invalid pwd count...:");
	    				userLoginCredentials.setInvalidPwdCount(invalidPwdCnt);
	    				responseDTO.setMessage(Constants.INVALID_PIN+", "+(3 - invalidPwdCnt)+Constants.ATTEMPTS_LEFT);
	    		        responseDTO.setResponseCode(Constants.INVALID_CREDENTIALS);
	    		        //prepareAlert("Dear "+userLoginCredentials.getUserName()+",<BR/> You entered Invalid PIN.", "INVALIDPIN", "MAIL", userLoginCredentials.getEmail(), "Invalid PIN");
	    		        prepareAlert(userLoginCredentials.getUserName(), "INVALIDPIN", "MAIL", userLoginCredentials.getEmail());
	    			}else{
	    				log.debug("exceeded invalid pawword count locking account...:"+userLoginCredentials.getUserId());
	    				responseDTO.setMessage(Constants.USER_LOCK_MESSAGE);
	    		        responseDTO.setResponseCode(Constants.USER_LOCK_CODE);
	    				userLoginCredentials.setStatus("L");
	    				//prepareAlert("Dear "+userLoginCredentials.getUserName()+",<br/>", "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail(), "Acount Locked");
	    				prepareAlert(userLoginCredentials.getUserName(), "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail());
	    			}
	        	}

			}else{
				log.debug("user inactivated/locked, Please consult Administrator.");
				responseDTO.setMessage(Constants.USER_LOCK_MESSAGE);
		        responseDTO.setResponseCode(Constants.USER_LOCK_CODE);
		        //prepareAlert("Dear "+userLoginCredentials.getUserId()+",", "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail(), "Acount Locked");
		        prepareAlert(userLoginCredentials.getUserName(), "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail());
			}
	        session.update(userLoginCredentials);
			session.flush();
	      }
	      else
	      {
	        log.debug("Invalid UserId.");
	        responseDTO.setMessage(Constants.INVALID_CREDENTIALS_MESSAGE);
	        responseDTO.setResponseCode(Constants.INVALID_CREDENTIALS);
	      }
	    }
	    catch (Exception e)
	    {
	      log.error("Error While validating user input.:");
	      log.error("Reason:" + e.getLocalizedMessage());
	      responseDTO.setMessage(Constants.SEARCH_FAIL);
	      responseDTO.setMessage(Constants.FAILURE_RESP_CODE);
	    }finally {
			session.close();
		}
	    log.debug("User Validation END.");
	    log.debug(responseDTO.toString());
	    return responseDTO;
	  }
  
  public ResponseDTO validate(UserLoginCredentials userBean)
  {
    this.log.debug("User Validation START.");
    ResponseDTO responseDTO = new ResponseDTO();
    long invalidPwdCnt = 0;
    Session session = null;
    try
    {
      session = sessionFactory.openSession();
      Criteria criteria = session
        .createCriteria(UserLoginCredentials.class);
      criteria.add(Restrictions.eq("userId", userBean.getUserId()));
      UserLoginCredentials userLoginCredentials = (UserLoginCredentials)criteria
        .uniqueResult();
      if (userLoginCredentials != null){
        if("A".equals(userLoginCredentials.getStatus()) || "F".equals(userLoginCredentials.getStatus())){
        	log.debug("User status is active");
        	boolean isValid=CommonUtil.validatePin(userBean.getPin(), userLoginCredentials.getPin(), "1");
        	if(isValid){
        		log.debug("pin validation success, validating user status.");
    			responseDTO.setData(userLoginCredentials);
    	        userLoginCredentials.setLastLoggedIn(new Date());
    	        userLoginCredentials.setInvalidPwdCount(new Long(0));
    	        userLoginCredentials.setStatus(Status.A.toString());
    			responseDTO.setMessage(Constants.SUCESS_SMALL);
    			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
    			log.debug("sending response.");
        	}else{
    			log.debug("pin validation failed, updating invalid password counts");
    			invalidPwdCnt = userLoginCredentials.getInvalidPwdCount();
    			log.debug("invalidPwdCnt...:"+invalidPwdCnt);
    			if(invalidPwdCnt <2){
    				invalidPwdCnt = invalidPwdCnt+1;
    				log.debug("updating invalid pwd count...:");
    				userLoginCredentials.setInvalidPwdCount(invalidPwdCnt);
    				responseDTO.setMessage(Constants.INVALID_PIN+", "+(3 - invalidPwdCnt)+Constants.ATTEMPTS_LEFT);
    		        responseDTO.setResponseCode(Constants.INVALID_CREDENTIALS);
    		        //prepareAlert("Dear "+userLoginCredentials.getUserName()+",<BR/> You entered Invalid PIN.", "INVALIDPIN", "MAIL", userLoginCredentials.getEmail(), "Invalid PIN");
    		        prepareAlert(userLoginCredentials.getUserName(), "INVALIDPIN", "MAIL", userLoginCredentials.getEmail());
    			}else{
    				log.debug("exceeded invalid pawword count locking account...:"+userLoginCredentials.getUserId());
    				responseDTO.setMessage(Constants.USER_LOCK_MESSAGE);
    		        responseDTO.setResponseCode(Constants.USER_LOCK_CODE);
    				userLoginCredentials.setStatus("L");
    				//prepareAlert("Dear "+userLoginCredentials.getUserName()+",<br/>", "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail(), "Account Locked");
    				prepareAlert(userLoginCredentials.getUserName(), "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail());
    			}
        	}

		}else{
			log.debug("user inactivated/locked, Please consult Administrator.");
			responseDTO.setMessage(Constants.USER_LOCK_MESSAGE);
	        responseDTO.setResponseCode(Constants.USER_LOCK_CODE);
	       // prepareAlert("Dear "+userLoginCredentials.getUserId()+",", "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail(), "Account Locked");
	        prepareAlert(userLoginCredentials.getUserName(), "LOCKACCOUNT", "MAIL", userLoginCredentials.getEmail());
		}
        session.update(userLoginCredentials);
		session.flush();

      }
      else
      {
        log.debug("Invalid UserId.");
        responseDTO.setMessage(Constants.INVALID_CREDENTIALS_MESSAGE);
        responseDTO.setResponseCode(Constants.INVALID_CREDENTIALS);
      }
    }
    catch (Exception e)
    {
      log.error("Error While validating user input.:");
      log.error("Reason:" + e.getLocalizedMessage());
      responseDTO.setMessage(Constants.SEARCH_FAIL);
      responseDTO.setMessage(Constants.FAILURE_RESP_CODE);
    }finally {
		session.close();
	}
    log.debug("User Validation END.");
    log.debug(responseDTO.toString());
    return responseDTO;
  }
  public UserLoginCredentials getById(String userId)
  {
    log.debug("loading by user id");
    UserLoginCredentials userLoginCredentials = null;
    Session session= null;
    try
    {
      session = sessionFactory.openSession();
      Criteria criteria = session
    		  .createCriteria(UserLoginCredentials.class);
      criteria.add(Restrictions.eq("userId", userId));
      criteria.add(Restrictions.eq("status", "A"));
      userLoginCredentials = (UserLoginCredentials)criteria
        .uniqueResult();
    }
    catch (Exception e)
    {
      log.error("Error While loading user by id.");
      log.error("Reason..:" + e.getLocalizedMessage());
    }finally{
    	session.close();
    }
    return userLoginCredentials;
  }
  @Override
  public UserLoginCredentials getByIdForFirstLogin(String userId)
  {
    log.debug("loading by user id");
    UserLoginCredentials userLoginCredentials = null;
    Session session = null;
    try
    {
      session = sessionFactory.openSession();
      Criteria criteria = session
        .createCriteria(UserLoginCredentials.class);
      criteria.add(Restrictions.eq("userId", userId));
      criteria.add(Restrictions.in("status", new String[]{"A","F"}));
      
      userLoginCredentials = (UserLoginCredentials)criteria
        .uniqueResult();
    }
    catch (Exception e)
    {
      log.error("Error While loading user by id.");
      log.error("Reason..:" + e.getLocalizedMessage());
    }finally{
    	session.close();
    }
    return userLoginCredentials;
  }

  public ResponseDTO changePwd(ChangePin bean)
  {
    log.debug("changePwd start");
    UserLoginCredentials userLoginCredentials = null;
    ResponseDTO dto = new ResponseDTO();
    Session session = null;
    try
    {
      userLoginCredentials = getByIdForFirstLogin(bean.getUserId());
      if (userLoginCredentials != null)
      {
          if (CommonUtil.validatePin(bean.getOldPin(), userLoginCredentials.getPin(), "1"))
          {
            userLoginCredentials.setPin(bean.getNewPin());
            userLoginCredentials.setStatus(Status.A.toString());
            session = sessionFactory.openSession();
            session.merge(userLoginCredentials);
            session.flush();
            dto.setMessage(Constants.SUCESS_SMALL);
            dto.setResponseCode("00");
           // insertAlert(userLoginCredentials, "CHANGEPIN", "PIN Change");
            prepareAlert(userLoginCredentials.getUserId(), "CHANGEPIN", "MAIL", userLoginCredentials.getEmail());
          }
          else
          {
            log.debug("Old pin doesn't matched.");
            dto.setMessage(Constants.INVALID_CREDENTIALS_MESSAGE);
            dto.setResponseCode(Constants.FAILURE_RESP_CODE);
          }
        }
      else
      {
    	  log.debug("Invalid User Id, Please try using valid details.");
          dto.setMessage(Constants.INVALID_USERID_MESSAGE);
          dto.setResponseCode(Constants.FAILURE_RESP_CODE);
      }
    }
    catch (Exception e)
    {
      log.error("Error while changePwd end");
      log.error("Reason..:" + e.getLocalizedMessage());
      dto.setMessage(Constants.GENERIC_FAILURE_MESS);
      dto.setResponseCode(Constants.FAILURE_RESP_CODE);
    }finally{
    	if(session != null && session.isOpen())session.close();
    }
    log.debug("changePwd end");
    return dto;
  }

	private void prepareAlert(String userId, String txnCode, String applCode, String email) {

		String language = ConfigLoader.convertResourceBundleToMap("config").get("bank.language");

		if ("en".equals(language)) {
			prepareEnglsihAlert(userId, txnCode, applCode, email);
		}
		if ("fr".equals(language)) {
			prepareFrenchAlert(userId, txnCode, applCode, email);
		}

	}

	private void prepareFrenchAlert(String userName, String txnCode, String applCode, String mailTo) {
		Alert alert = new Alert();
		try {
			StringBuilder message = new StringBuilder();
			if ("PINRESET".equals(txnCode)) {
				message.append("cher <b>" + userName + "</b>,");
				message.append("<BR/>");

				message.append("Votre demande de modification du code PIN a été traitée le");
				message.append("" + new SimpleDateFormat(Constants.MAIL_DATE_FORMAT).format(new Date()));
				message.append("<BR/><BR/>");

				alert.setTxnType("PINRESET");
				alert.setSubject("Changement de pin");

			}
			if ("INVALIDPIN".equals(txnCode)) {
				message.append("cher " + userName + ",<BR/> Vous avez entré un code PIN invalide.");
				alert.setTxnType("INVALIDPIN");
				alert.setSubject("PIN invalide");

			}

			if ("LOCKACCOUNT".equals(txnCode)) {
				message.append("cher " + userName + ",<br/>");
				alert.setTxnType("LOCKACCOUNT");
				alert.setSubject("Compte bloqué");
			}
			alert.setAppl(applCode);
			alert.setMailTo(mailTo);
			alert.setMessage(message.toString());
			alertDao.save(alert);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

private void prepareEnglsihAlert(String userName, String txnCode, String applCode, String mailTo) {
	Alert alert = new Alert();
  try {
	  StringBuilder message= new StringBuilder();
		if ("PINRESET".equals(txnCode)) {
			message.append("Dear <b>"+userName+"</b>,");
			message.append("<BR/>");

			message.append("Your request for PIN change has been processed on");
			message.append(""+new SimpleDateFormat(Constants.MAIL_DATE_FORMAT).format(new Date()));
			message.append("<BR/><BR/>");
			
			alert.setTxnType("LOCKACCOUNT");
			alert.setSubject("Pin Change");

		}
		if ("INVALIDPIN".equals(txnCode)) {
			message.append("Dear "+userName+",<BR/> You entered Invalid PIN.");
			alert.setTxnType("INVALIDPIN");
			alert.setSubject("Invalid PIN");

		}

		if ("LOCKACCOUNT".equals(txnCode)) {
			message.append("Dear "+userName+",<br/>");
			alert.setTxnType("LOCKACCOUNT");
			alert.setSubject("Acount Locked");
		}
		alert.setAppl(applCode);
		alert.setMailTo(mailTo);
		alert.setMessage(message.toString());
		alertDao.save(alert);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

/*
private void insertAlert(UserLoginCredentials loginCredentials,
	String txnCode, String subject) {
	StringBuilder message = new StringBuilder();
	try{
		log.debug("pin update success, inserting notification..:"+txnCode);
		message.append("Dear <b>"+loginCredentials.getUserName()+"</b>,");
		message.append("<BR/>");

		message.append("Your request for PIN change has been processed on");
		message.append(""+new SimpleDateFormat(Constants.MAIL_DATE_FORMAT).format(new Date()));
		message.append("<BR/>");
		message.append("<BR/>");
		prepareAlert(message.toString(), txnCode, "MAIL", loginCredentials.getEmail(), subject);

	}catch(Exception e){
		log.error("error while generating email for pin change for user.");
		log.error("Error..:"+e.getLocalizedMessage());
	}finally{
		message = null;
		loginCredentials = null;
	}
}*/

  public ResponseDTO registerDevice(DeviceRegister bean)
  {
    log.debug("registerDevice...START");
    log.debug("registerDevice..." + bean.toString());
    ResponseDTO responseDTO = new ResponseDTO();
    UserLoginCredentials credentials = null;
    Session session = null;
    List<String> imeis= null;
    try
    {
      credentials = getByIdForFirstLogin(bean.getUserId());
      session = sessionFactory.openSession();
      if (credentials != null)
      {
        log.debug("loaded user");
       boolean isValid= CommonUtil.validatePin(bean.getPin(), credentials.getPin(), bean.getChannel()+"");
       // boolean isValid= true;
       if (isValid)
        {
        	boolean isValidOtp= validateOtp(credentials.getMobileNo(), bean.getOtp());
        	if(isValidOtp){
        		log.debug("SUCCESS in validating credentials.. status:"+credentials.getStatus());
           	 if("A".equals(credentials.getStatus())||"F".equals(credentials.getStatus())){
           		List<ChannelUsers> channelUsers1 = getChannelUsers(credentials.getCommonId());
           		List<ChannelUsers> channelUsers = CommonUtil.getAppChannelUsers(channelUsers1);
           			if(channelUsers1.size()>0){
           				log.debug("Mobile devices registered..:"+channelUsers.size());
           				if(channelUsers.size()<=1000 ){
           					for(ChannelUsers channelUser : channelUsers1){
                      			 if("APP".equals(channelUser.getCannelId())){
                      				 log.debug("User has app Channel registerd");
                      				 credentials.setLastLoggedIn(new Date());
                                	 log.debug("SUCCESS in Registering device.");
                                	 responseDTO.setMessage("Success");
                                	 responseDTO.setResponseCode("00");
                                	 responseDTO.setData(credentials);
                                	 break;
                      			 }else{
                      				 log.debug("Android Channel is not allowed for this user");
                                    responseDTO.setMessage(Constants.NO_ANDROID_CHANNEL_MESSAGE);
                                    responseDTO.setResponseCode(Constants.DEVICE_REGISTER_FAIL_CODE);
                      			 }
                      		 }	
           				}else{
                   			imeis = new ArrayList<String>();
                   			for(ChannelUsers channelUser : channelUsers){
                   				imeis.add(channelUser.getImeiNo());
                   			}
                   			responseDTO.setData(imeis);
                   			log.debug("Exceeded maximum devices.");
                            responseDTO.setMessage(Constants.DEVICE_EXCEEDED_MESSAGE);
                            responseDTO.setResponseCode(Constants.DEVICE_REGISTER_FAIL_CODE);
                   		}
               		}else{
               			log.debug("No channels assigned to this user.");
                        responseDTO.setMessage(Constants.NO_ANDROID_CHANNEL_MESSAGE);
                        responseDTO.setResponseCode(Constants.DEVICE_REGISTER_FAIL_CODE);
               		}
               	 session.merge(credentials);
              	 session.flush();
           		
           		
           	 }else{
           		 log.debug("FAILURE in validating user status.");
                 responseDTO.setMessage(Constants.USER_INACTIVE_MESSAGE);
                 responseDTO.setResponseCode(Constants.USER_INACTIVE_CODE);
           	 }
        	}else{
        		log.debug("FAILURE in validating OTP.");
                responseDTO.setMessage(Constants.OTP_FAILED_MESSAGE);
                responseDTO.setResponseCode(Constants.DEVICE_REGISTER_FAIL_CODE);
        	}
        }
        else
        {
          log.debug("FAILURE in validating credentials");
          //not deployed to prod
          responseDTO.setMessage(Constants.INVALID_USERID_MESSAGE);
          responseDTO.setResponseCode(Constants.INVALID_CREDENTIALS);
        }
      }
      else
      {
        log.debug("Invalid User Id.");
        responseDTO.setMessage(Constants.INVALID_CREDENTIALS_MESSAGE);
        responseDTO.setResponseCode(Constants.USER_NOTFOUNT_CODE);
      }
    }
    catch (Exception e)
    {
      log.error("Error While registering device to user.");
      log.error("Reason..:" + e.getLocalizedMessage());
      responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
      responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
    }finally
    {
    	session.close();
    }
    log.debug("registerDevice...END"+responseDTO.toString());
    return responseDTO;
  }

public ResponseDTO updateProfilePicture(Map<String, Object> map)
  {
    log.debug("updateProfilePicture...START");
    UserLoginCredentials credentials = null;
    ResponseDTO dto = new ResponseDTO();
    Session session = null;
    try
    {
      credentials = getById(map.get("userId")+"");
      credentials.setPic(map.get("fileName")+"");
      session = sessionFactory.openSession();
      session.merge(credentials);
      session.flush();
      dto.setData("");
      dto.setResponseCode("00");
      dto.setMessage("Success");
    }
    catch (Exception e)
    {
      dto.setResponseCode(Constants.FAILURE_RESP_CODE);
      dto.setMessage(Constants.GENERIC_FAILURE_MESS);
      e.printStackTrace();
      log.error("Error while updating profile pic..:");
      log.error("Reason..:" + e.getLocalizedMessage());
    }finally{
    	session.close();
    }
    return dto;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<ChannelUsers> getChannelUsers(String commonId) {
	    log.debug("loading ChannelUsers..:"+commonId);
	    List<ChannelUsers> channelUsers = null;
	    Session session = null;
	    try
	    {
	      session = sessionFactory.openSession();
	      Criteria criteria = session
	        .createCriteria(ChannelUsers.class);
	      criteria.add(Restrictions.eq("commonId", commonId));
	      channelUsers = (List<ChannelUsers>) criteria.list();
	    }
	    catch (Exception e)
	    {
	      log.error("Error While loading user by id.");
	      log.error("Reason..:" + e.getLocalizedMessage());
	    }finally{
	    	session.close();
	    }
	    return channelUsers;
	  }
  
  @Override
  public boolean validateOtp(String mobile, String otp) {
		log.debug("validating otp for mobile..:"+mobile);
		boolean isValid=false;
		Session session =null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(OtpBean.class);
			criteria.add(Restrictions.eq("mobileNo", mobile));
			criteria.add(Restrictions.eq("status", "N"));
			OtpBean otpBean = (OtpBean) criteria.uniqueResult();
			log.debug("executed results..otpBean:"+ otpBean == null);
			log.debug(otpBean.toString() );
			if(otpBean != null){
				Date now= new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				String sysDate = sdf.format(now); 

				Date nowDate = sdf.parse(sysDate);
				Date dbDate = otpBean.getDateTime();
				
				long timeDiff = (nowDate.getTime()-dbDate.getTime())/(1000*60);
				if(timeDiff < 5){
					if(otp.equals(otpBean.getOtp())){
						isValid = true;
						otpBean.setStatus("D");
						otpBean.setValidationTime(new Date());
						//updateOtp(otpBean);
					}else{
						if(otpBean.getRetryCount()<3){
							otpBean.setRetryCount(otpBean.getRetryCount()+1);
							isValid = false;
						}else{
							log.debug("Reached max reties, changing status..");
							otpBean.setRetryCount(otpBean.getRetryCount()+1);
							otpBean.setStatus("L");
							isValid = false;
						}
						log.debug(isValid);
					}
					session.update(otpBean);
					session.flush();
				}else{
					log.debug("Otp Time Expired.");
					isValid = false;
				}

			}else{
				log.debug("executed results..otpBean:"+ otpBean);
				isValid = false;
			}

		}catch(Exception e){
			log.error("Error Occured");
			log.error("Reason..:"+e.getLocalizedMessage());
			e.printStackTrace();
			isValid = false;
		}finally{
			session.close();
		}
		return isValid;
	}
  
  private boolean isUserActive(Date now, Date dbDate) {
		log.debug("isUserActive...:"+dbDate);
		boolean isActive = true;
		try{
			if(dbDate != null){
				long timeDiff = (now.getTime()-dbDate.getTime())/(1000*60);
				log.debug("timeDiff in minutes...:"+timeDiff%60);
				if(timeDiff >= 3){
					log.debug("timeDiff in minutes, is greater than ideal time");
					isActive = false;
				}else{
					log.debug("timeDiff in minutes, is less than ideal time, identified active user.");
				}
			}else{
				isActive = false;
				log.debug("dbDate is null...:"+isActive);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			isActive = false;
		}

		return isActive;
	}

@Override
public ResponseDTO updateProfile(UserLoginCredentials bean) {
	// TODO Auto-generated method stub
	return null;
}

}
