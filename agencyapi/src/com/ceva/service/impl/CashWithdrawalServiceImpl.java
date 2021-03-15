package com.ceva.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.CashWithdrawalConfirm;
import com.ceva.bank.common.beans.CashWithdrawalInit;
import com.ceva.bank.common.beans.OtpBean;
import com.ceva.bank.common.beans.QueryAccount;
import com.ceva.bank.common.beans.TransferRequest;
import com.ceva.bank.common.dao.CashWithDrawalDao;
import com.ceva.bank.common.dao.OtpDao;
import com.ceva.bank.services.FinacleService;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.db.model.Cashwithdrawal;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.CashWithdrawalService;
import com.ceva.service.FundTransferService;
import com.ceva.service.OtpService;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@Service("cashWithdrawalService")
public class CashWithdrawalServiceImpl implements CashWithdrawalService {

	Logger log = Logger.getLogger(CashWithdrawalService.class);
	
	@Autowired
	private CashWithDrawalDao cashWithDrawalDao;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private OtpDao otpDao;  
	
	@Autowired
	private FundTransferService fundTransferService;
	
	@Autowired
	private FinacleService finacleService;

	
	@Override
	public ResponseDTO cashByAccountConfirm(
			CashWithdrawalConfirm withdrawalConfirm) {
		log.info("cashByAccountConfirm START");
		ResponseDTO responseDTO = new ResponseDTO();
		Cashwithdrawal cashwithdrawal = null;
		String valid =null;

		try{
			cashwithdrawal = cashWithDrawalDao.getById(Long.valueOf(withdrawalConfirm.getTxnRef()));

			if(cashwithdrawal==null){
				valid ="Reference Number Not Valid.";
			}else{
				valid = validateObjects(withdrawalConfirm, cashwithdrawal);
				log.info("objects comparision.:"+valid);
				if(valid.length() == 0){
					log.info("validating otp for cwdby act start");
					responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
					responseDTO.setMessage(valid);
					responseDTO = validateOtp(cashwithdrawal.getPhone(), cashwithdrawal.getSecondaryPhone(), withdrawalConfirm.getOtp());
					log.info("validating otp for cwdby act response..:"+responseDTO.getMessage());
					if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
						withdrawalConfirm.setTxnCode(cashwithdrawal.getTxnCode());
						responseDTO = processFundTransfer(withdrawalConfirm);
						updatCWDTxn(cashwithdrawal, responseDTO);
					}else{
						log.info("validating otp failed..:");
					}
				}else{
					log.info("valid..:"+valid);
					responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
					responseDTO.setMessage(valid);
				}
			}
			log.info(responseDTO.toString());
		}catch(Exception e) {
			log.error("Error while loading phone numbers");
			log.error("Reason..:"+e.getLocalizedMessage());
		}finally{
			cashwithdrawal= null;
			withdrawalConfirm=null;
		}
		log.info("cashByAccountConfirm END");
		return responseDTO;
	}

	private ResponseDTO processFundTransfer(
			CashWithdrawalConfirm withdrawalConfirm) {
		String bankCode = null;
		ResponseDTO responseDTO = null;
		TransferRequest transferRequest = null;
		try{
			bankCode = ConfigLoader.convertResourceBundleToMap("config").get("fin.homebank.code");
			transferRequest = new TransferRequest();
			transferRequest.setAccountNumber(withdrawalConfirm.getCustomerAccountNumber());
			transferRequest.setBankCode(bankCode);
			transferRequest.setChannel(withdrawalConfirm.getChannel());
			transferRequest.setAmount(BigDecimal.valueOf(withdrawalConfirm.getAmount()));
			/*transferRequest.setBeneficiaryAccount(store.getAccountNumber());
			transferRequest.setBeneficiaryName(store.getAccountName());*/
			transferRequest.setMakerId(withdrawalConfirm.getMakerId());
			transferRequest.setMerchantId(withdrawalConfirm.getMerchantId());
			transferRequest.setMobileNumber(withdrawalConfirm.getMobileNumber());
			transferRequest.setNarration(withdrawalConfirm.getNarration());
			transferRequest.setServiceId(withdrawalConfirm.getServiceId()+"");
			transferRequest.setDateTime(new SimpleDateFormat(Constants.DATE_PATTERN).format(new Date()));
			log.debug("adding 4 digits to id to suit to fundtransfer");
			transferRequest.setReferenceCode(withdrawalConfirm.getTxnRef()+CommonUtil.createRandomNumber(4));
			transferRequest.setTxnCode(withdrawalConfirm.getTxnCode());
			log.info(transferRequest.toString());
			log.debug("===========================================================================");
			responseDTO = fundTransferService.processIntraBank(transferRequest);
			log.debug("===========================================================================");
			log.debug("response after processing fund transfer..:"+responseDTO.toString());

		}catch(Exception e){
			log.error("Error while processFundTransfer");
			log.error("Reason ,,..:"+e.getLocalizedMessage());
		}finally{
			transferRequest=null;
			bankCode=null;
		}
		return responseDTO;
	}

	@Override
	public ResponseDTO cashByAccountInit(CashWithdrawalInit withdrawalInit) {
		log.info("cashByAccountInit START");
		ResponseDTO responseDTO = null;
		QueryAccount queryAccount= null;
		try{
			log.debug("starting account query for phone number..:");
			responseDTO = validateAccount(withdrawalInit);
			log.debug("result from query account..:"+responseDTO.toString());
			if(Constants.SUCCESS_RESP_CODE.equals(responseDTO.getResponseCode())){
				log.info("account number has phones");
				queryAccount = (QueryAccount) responseDTO.getData();
				if(queryAccount.getPhone()!= null){
					queryAccount.setPhone(queryAccount.getPhone().replaceAll("\\W", ""));
					withdrawalInit.setCustomerPhone(queryAccount.getPhone());
					log.debug("sending otp for registerd mobile."+withdrawalInit.getCustomerPhone());
					String ref = sendOtp(withdrawalInit);
					long id = saveCWDTxn(ref, withdrawalInit);
					log.debug("withdrawalInit id.:"+id);
					responseDTO.setData(id+"");
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
				}else{
					//responseDTO.setMessage("No phone number linked to this account.");
					responseDTO.setMessage(Constants.NO_PHONE_TO_ACCOUNT);
					responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
					log.debug("Query account returned failure respose..:"+responseDTO.toString());
				}
			}else{
				//log.info("Query account returned failure respose..:"+responseDTO.toString());
			}
		}catch(Exception e) {
			log.error("Error while cashByAccountInit");
			log.error("Reason..:"+e.getLocalizedMessage());
			responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
			responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
		}finally{
			queryAccount= null;
		}
		log.info("cashByAccountInit "+responseDTO.toString());
		return responseDTO;
	}
	
	private ResponseDTO validateAccount(CashWithdrawalInit withdrawalInit) {
		log.info("Quireing account to get phone number START");
		ResponseDTO dto = null;
		try{
		dto = finacleService.getPhonesByAccount(withdrawalInit.getCustomerAccountNumber());
		}catch(Exception e) {
			log.error("Error while loading phone numbers");
			log.error("Reason ,,..:"+e.getLocalizedMessage());
		}finally{
		}
		log.info("Quireing account to get phone number END");
		return dto;
	}
	private String sendOtp(CashWithdrawalInit withdrawalInit) {
		log.info("sending Otp for cash withdrawal."+withdrawalInit.toString());
		OtpBean otpBean = null;
		String ref = null;
		try{
			otpBean = new OtpBean();
			otpBean.setUserId(withdrawalInit.getMakerId());
			otpBean.setChannel(withdrawalInit.getChannel());
			otpBean.setMobileNumber(withdrawalInit.getCustomerPhone());
			otpBean.setAccountNumber(withdrawalInit.getCustomerAccountNumber());
			otpBean.setNarration("Cash Withdrawal By Account at Agent.");
			otpBean.setTxncode(TransactionCodes.CWDBYACT.toString());
			otpBean = (OtpBean) otpService.sendOtp(otpBean).getData();
			ref = otpBean.getRefNumber();
			log.debug("saving txn in db start.");

		}catch(Exception e){
			ref ="";
			log.error("Error while sending OTP");
			log.error("Reason ..:"+e.getLocalizedMessage());
		}finally{
			otpBean= null;
		}
		return ref;
	}

	private long saveCWDTxn(String ref, CashWithdrawalInit withdrawalInit) {
		log.info("Saving txn in db START");
		long id = 0;
		try{
			Cashwithdrawal cashwithdrawal = new Cashwithdrawal(Long.valueOf(ref), withdrawalInit.getChannel(), withdrawalInit.getMakerId(), withdrawalInit.getMerchantId(), withdrawalInit.getMobileNumber(), withdrawalInit.getServiceId(), withdrawalInit.getCustomerAccountNumber(), withdrawalInit.getCustomerName(),TransactionCodes.CWDBYACT.toString(), withdrawalInit.getCustomerPhone(), "");
			cashwithdrawal.setStatus("N");
			cashwithdrawal.setSmsRef(ref);
			id = cashWithDrawalDao.save(cashwithdrawal);
		}catch(Exception e){
			log.error("Error while saveCWDTxn");
			log.error("Reason ..:"+e.getLocalizedMessage());
		}
		log.info("Saving txn in db END");
		return id;
	}
	private ResponseDTO validateOtp(String mobileNumber, String secondaryPhone,
			String otp) {
		log.debug("validateOtp START");
		ResponseDTO dto = null;
		try{
			log.debug("validateOtp with primary mobile.");
			dto = otpDao.validate(mobileNumber, otp);
			log.debug("validateOtp result..:"+dto.toString());
		}catch(Exception e){
			log.error("Error while loading phone numbers");
			log.error("Reason ,,..:"+e.getLocalizedMessage());
		}finally{
		}
		log.debug("validateOtp END");
		return dto;
	}
	private String validateObjects(CashWithdrawalConfirm withdrawalConfirm,
			Cashwithdrawal cashwithdrawal) {
		StringBuffer valid = new StringBuffer();
		try{
			if(!withdrawalConfirm.getTxnRef().equals(cashwithdrawal.getId()+"")){
				valid.append("reference Numbers not matched");
			}else if(!withdrawalConfirm.getCustomerAccountNumber().equals(cashwithdrawal.getCustomerAccountNumber())){
				valid.append("Account Numbers not matched");
			}else if(!withdrawalConfirm.getCustomerName().equals(cashwithdrawal.getCustomerName())){
				valid.append("Account Names not matched");
			}else if(!withdrawalConfirm.getMakerId().equals(cashwithdrawal.getMakerId())){
				valid.append("Maker details Not matched");
			}else{

			}
		}catch(Exception e){

		}finally{
			withdrawalConfirm=null;
			cashwithdrawal=null;
		}
		return valid.toString();
	}
	private void updatCWDTxn(Cashwithdrawal cashwithdrawal,
			ResponseDTO responseDTO) {
		log.debug("loggin cashByAccountConfirm START");
		try{
			if(cashwithdrawal != null & responseDTO !=null){
				if(Constants.SUCCESS_RESP_CODE.equals(responseDTO.getResponseCode())){
					cashwithdrawal.setStatus("S");
				}else{
					cashwithdrawal.setStatus("F");
				}
				log.info("===========updating cashwithdrawal START==================");
				cashWithDrawalDao.update(cashwithdrawal);
				log.info("===========updating cashwithdrawal END ==================");
			}
		}catch(Exception e){
			log.error("loggin cashByAccountConfirm ERROR"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		log.info("loggin cashByAccountConfirm END");
	}
}
