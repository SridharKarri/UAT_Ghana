package com.ceva.bank.common.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.OtpDao;
import com.ceva.db.model.OtpBean;
import com.ceva.db.model.walletTxnLog;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

@Repository("otpDao")
public class OtpDaoImpl implements OtpDao {
	Logger log = Logger.getLogger(OtpDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ResponseDTO save(OtpBean bean) {
		ResponseDTO dto= new ResponseDTO();
		Session session=null;
		try{

			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(OtpBean.class);
			criteria.add(Restrictions.eq("mobileNo", bean.getMobileNo()));
			criteria.add(Restrictions.eq("status", "N"));
			List<OtpBean> validotps =  criteria.list();
			for(OtpBean otpBean: validotps){
				otpBean.setStatus("X");
				session.update(otpBean);
				log.debug("invalidating otp for..:"+otpBean.getId());
			}
			Long id = (Long) session.save(bean);
			log.info("generated id..:"+id);
			session.flush();
			dto.setMessage(Constants.SUCESS_CAPS);
			dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
			dto.setData(id);
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while Saving otp..:"+e.getLocalizedMessage());
		}finally{
			session.close();
			session=null;
		}
		log.debug("Persisting into database END.");
		return dto;
	}

	private void invalidatePrevious(String mobileNo) {
		log.debug("Invalidating old otp for the mobile.:"+mobileNo);
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(OtpBean.class);
			criteria.add(Restrictions.eq("mobileNo", mobileNo));
			criteria.add(Restrictions.eq("status", "N"));
			List<OtpBean> validotps =  criteria.list();
			for(OtpBean bean: validotps){
				bean.setStatus("X");
			}
			session.flush();
		}catch(Exception e){
			e.printStackTrace();
			log.error("Error while invalidatePrevious otp..:"+e.getLocalizedMessage());
		}finally{
			session.close();
			session= null;
		}
		log.debug("Invalidating old otp for the mobile.:END");
	}

	@Override
	public ResponseDTO validate(String mobile, String otp) {
		log.debug("validating otp for mobile..:"+mobile);
		ResponseDTO responseDTO = new ResponseDTO();
		Session session =null;
		try{
			//log.info("lower time limit..:"+ new Timestamp(new Date().getTime()-(15*60*1000)));
			//log.info("upper time limit..:"+ new Timestamp(new Date().getTime()));
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(OtpBean.class);
			criteria.add(Restrictions.eq("mobileNo", mobile));
			criteria.add(Restrictions.eq("status", "N"));
			//criteria.add(Restrictions.between("dateTime", new Timestamp(new Date().getTime()-(15*60*1000)),  new Timestamp(new Date().getTime())));
			OtpBean otpBean = (OtpBean) criteria.uniqueResult();
			log.debug("executed results..otpBean:"+ otpBean == null);
			if(otpBean !=null){
				long timeDiff = (new Date().getTime()-otpBean.getDateTime().getTime())/(1000*60);
				log.debug("timeDiff..:"+ timeDiff);
				if(timeDiff < 5){
					log.info("before expected time...");
					if(otp.equals(otpBean.getOtp())){
						log.info("otp matched..");
						responseDTO.setMessage(Constants.SUCESS_SMALL);
						responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
						responseDTO.setData(Constants.SUCESS_SMALL);
						otpBean.setStatus("D");
						otpBean.setValidationTime(new Date());
						log.info("changing otp status to validated.");
						updateOtp(otpBean);
					}else{
						log.info("otp NOT matched..");
						if(otpBean.getRetryCount()<3){
							log.debug("Updating try count..");
							otpBean.setRetryCount(otpBean.getRetryCount()+1);
							responseDTO.setData("Please Enter Valid password.");
							responseDTO.setMessage(Constants.OTP_FAILED_MESSAGE);
							responseDTO.setResponseCode(Constants.OTP_FAILED_CODE);
						}else{
							log.debug("Reached max reties, changing status..");
							otpBean.setRetryCount(otpBean.getRetryCount()+1);
							otpBean.setStatus("L");
							responseDTO.setData("Please Enter Valid password.");
							responseDTO.setMessage(Constants.OTP_RETRY_MESSAGE);
							responseDTO.setResponseCode(Constants.OTP_RETRY_LIMITS);
						}
						log.debug(responseDTO.toString());
					}
					session.saveOrUpdate(otpBean);
				}else{
					log.debug("Otp Time Expired.");
					//responseDTO.setData("Otp Expired.");
					responseDTO.setMessage(Constants.OTP_EXPIRED_MESSAGE);
					responseDTO.setResponseCode(Constants.OTP_RETRY_LIMITS);
				}

			}else{
				responseDTO.setMessage(Constants.INVALID_REG_MOBILE_MESSAGE);
				responseDTO.setResponseCode(Constants.OTP_RETRY_LIMITS);
			}

		}catch(Exception e){
			log.error("Error Occured");
			log.error("Reason..:"+e.getLocalizedMessage());
			//e.printStackTrace();
			responseDTO.setMessage(Constants.OTP_RETRY_MESSAGE);
			responseDTO.setResponseCode(Constants.OTP_RETRY_LIMITS);
		}finally{
			session.close();
			session= null;
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO getOtp(OtpBean bean) {
		return null;
	}

	@Override
	public ResponseDTO updateOtp(OtpBean bean) {
		ResponseDTO responseDTO = new ResponseDTO();
		log.info("updating otp.");
		try{

		}catch(Exception e){

		}
		return responseDTO;
	}

	@Override
	public void saveWalletTxn(long reference, String url, String response, String status) {
		log.info("saveQsaveWalletTxnuicktellerTxn to db. START");
		Session session = null;
		try{
			session = sessionFactory.openSession();
			walletTxnLog txnLog= new walletTxnLog();
			txnLog.setTxnRef(reference+"");
			txnLog.setResquest(url);
			txnLog.setResponse(response);
			txnLog.setStatus(status);
			session.save(txnLog);
			session.flush();
		}catch(Exception e){
			log.equals("Error while logging quicktellre transaction.");
			log.equals("Reason..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			session.close();
			session= null;
		}
		log.info("saveQsaveWalletTxnuicktellerTxn to db. END");
	}

}
