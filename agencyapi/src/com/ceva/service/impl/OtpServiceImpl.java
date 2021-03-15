package com.ceva.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.OtpBean;
import com.ceva.bank.common.dao.AccountDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.OtpDao;
import com.ceva.bank.common.dao.ReportDao;
import com.ceva.bank.common.dao.StateDao;
import com.ceva.bank.services.OtpBankService;
import com.ceva.bank.services.RequestProcessor;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.OtpService;
import com.nbk.util.Constants;

@Service("otpService")
public class OtpServiceImpl implements OtpService {
	Logger log = Logger.getLogger(OtpServiceImpl.class);
	
	@Autowired
	LoginDao loginDao; 
	
	@Autowired
	MerchantDao merchantDao; 
	
	@Autowired
	StateDao stateDao; 
	
	@Autowired
	OtpBankService otpBankService;
	
	@Autowired
	RequestProcessor requestProcessor;
	
	@Autowired
	OtpDao otpDao;
	
	@Autowired
	AccountDao accountDao;
	
	@Autowired
	ReportDao reportDao; 

	@Override
	public ResponseDTO sendOtp(OtpBean bean) {
		log.info("send otp");
		ResponseDTO dto= new ResponseDTO();
		UserLoginCredentials loginCredentials=null;
		com.ceva.db.model.OtpBean otpBean = new com.ceva.db.model.OtpBean();
		try{
			log.debug("calling otp service");
			loginCredentials = loginDao.getByIdForFirstLogin(bean.getUserId());
			if(loginCredentials != null && loginCredentials.getAgent()!=null && loginCredentials.getAgent().length()>0){
				if(TransactionCodes.REGOTP.toString().equals(bean.getTxncode())){
					bean.setAccountNumber(merchantDao.getStoreById(loginCredentials.getStore()).getAccountNumber());
					bean.setMobileNumber(loginCredentials.getMobileNo());
					bean.setMerchantId(loginCredentials.getAgent());
					bean.setNarration("device Registration.");
				}else if(TransactionCodes.ACTOTP.toString().equals(bean.getTxncode())){
					boolean hasRecord = accountDao.getByPhone(bean.getMobileNumber());
					log.debug("hasRecord..:"+hasRecord);
					if(hasRecord){
						dto.setMessage("There is an account already opened with this mobile number.");
						dto.setResponseCode(Constants.FAILURE_RESP_CODE);
						return dto;
					}
					bean.setAccountNumber(merchantDao.getStoreById(loginCredentials.getStore()).getAccountNumber());
					bean.setMobileNumber(bean.getMobileNumber());
					bean.setMerchantId(loginCredentials.getAgent());
					bean.setNarration("Customer account open otp.");
				}else{
					bean.setNarration("cashwithdrawal");
				}
				dto = otpBankService.send(bean);
				log.debug("response ..." + dto.toString());
				if(dto != null){
					if(Constants.SUCCESS_RESP_CODE.equals(dto.getResponseCode())){
						log.info("persisting otp");
						otpBean.setId(new Long(bean.getRefNumber()));
						otpBean.setMobileNo(bean.getMobileNumber());
						otpBean.setNarration(bean.getNarration());
						otpBean.setOtp(bean.getOtp());
						otpBean.setDateTime(new Date());
						otpBean.setStatus("N");
						otpBean.setTxnCode(bean.getTxncode());
						bean = constructToRespons(bean);
						dto.setData(bean);
						otpDao.save(otpBean);
					}
				}
			}else{
				
				dto.setMessage(Constants.USER_NOTFOUNT_MESSAGE);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
			
		}catch(Exception e){
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			e.printStackTrace();
			log.error("Error while sending message.:"+e.getLocalizedMessage());
		}finally{
			loginCredentials = null;
		}
		return dto;
	}

	private OtpBean constructToRespons(OtpBean bean){
		bean.setOtp("");
		bean.setNarration("");
		bean.setTxncode("");
		bean.setMobileNumber("");
		return bean;
	}

	@Override
	public ResponseDTO listStates() {
		ResponseDTO responseDTO =new ResponseDTO();
		log.info("listStates");
		try{
			responseDTO.setData(stateDao.listStates());
			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
			responseDTO.setMessage(Constants.SUCESS_SMALL);
		}catch(Exception e){
			log.error("error reason.:"+e.getLocalizedMessage());
			e.printStackTrace();
			responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO listCities(String stateid) {
		ResponseDTO responseDTO =new ResponseDTO();
		log.info("listCities");
		try{
			responseDTO.setData(stateDao.listCities(stateid));
			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
			responseDTO.setMessage(Constants.SUCESS_SMALL);
		}catch(Exception e){
			log.error("error reason.:"+e.getLocalizedMessage());
			e.printStackTrace();
			responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		return responseDTO;
	}

}
