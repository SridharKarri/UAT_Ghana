package com.ceva.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.AccountBalance;
import com.ceva.bank.common.beans.AccountBean;
import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.bank.common.beans.MiniStatement;
import com.ceva.bank.common.beans.UserLocation;
import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.bank.services.FinacleService;
import com.ceva.bank.utils.Status;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.bank.utils.TransactionStatus;
import com.ceva.db.model.NonFinTransaction;
import com.ceva.db.model.Store;
import com.ceva.db.model.Transaction;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.CommonUtil;
import com.nbk.util.Constants;


@Service("coreService")
public class CoreServiceImpl implements CoreService {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private FinacleService finacleService;
	
	@Autowired
	private FeeDao feeDao;
	
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private TransactionDao transactionDao;

	@Override
	public ResponseDTO queryAccount(BalanceEnquirey bean) {
		logger.debug("queryAccount...");
		ResponseDTO dto = finacleService.queryAccount(bean.getAccountNumber());
		if(dto!=null){
			logTransaction(TransactionCodes.QRYACT.toString(), bean.getMerchantId(), bean.getUserId(), bean.getMobileNumber(), bean.getAccountNumber(), bean.getChannel(), dto.getMessage(), dto.getResponseCode());
		}else{
			logTransaction(TransactionCodes.QRYACT.toString(), bean.getMerchantId(), bean.getUserId(), bean.getMobileNumber(), bean.getAccountNumber(), bean.getChannel(), Constants.UNEXPECTED_ERROR_MESSAGE, Constants.UNEXPECTED_ERROR_CODE);
		}
		logger.debug("queryAccount..");
		return dto;
	}
	
	@Override
	public ResponseDTO updateUserLocation(UserLocation bean) {
		logger.debug("updateUserLocation...");
		UserLoginCredentials credentials = loginDao.getById(bean.getUserId());
		bean.setStoreId(credentials.getStore());
		boolean isUpdate = merchantDao.updateStoreLocation(bean);
		logger.debug("updateUserLocation...:"+isUpdate);
		return new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
	}

	@Override
	public ResponseDTO balanceEnquirey(BalanceEnquirey enquirey) {
		logger.debug("balanceEnquirey...");
		ResponseDTO responseDTO = new ResponseDTO();
		HashMap<String, String> data = null;
		UserLoginCredentials credentials = loginDao.getById(enquirey.getUserId());
		Store store = merchantDao.getStoreById(credentials.getStore());
		try{
			if(credentials != null && Status.A.toString().equals(credentials.getStatus())){
				enquirey.setAccountNumber(store.getAccountNumber());
				AccountBalance balance =finacleService.balanceEnquirey(enquirey.getAccountNumber());
				BigDecimal userCommission = feeDao.getAgentUserCurrentMonthCommission(enquirey.getUserId(), credentials.getStore());
	 			BigDecimal storeCommission = feeDao.getAgentStoreCurrentMonthCommission(credentials.getStore());
				data = new HashMap<String, String>();
				if(balance != null){
					data.put("balance", balance.getBalance()+"");
				}else{
					data.put("balance", "Error while loading balance.");
				}
				if(storeCommission == null || storeCommission.equals(new BigDecimal(0)))
				{
					storeCommission = new BigDecimal(0);
				}
				
				data.put("commission", storeCommission+"");
				data.put("userCommission", userCommission+"");
				responseDTO.setMessage(Constants.SUCESS_SMALL);
				responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				responseDTO.setData(data);
			}else{
				responseDTO = commonUtil.generateFailureMessage(Constants.FAILURE_RESP_CODE, Constants.USER_INACTIVE_MESSAGE);
			}
		}catch(Exception e){
			logger.error("Error while getting balance..:"+e.getLocalizedMessage());
			responseDTO = commonUtil.generateFailureMessage(Constants.FAILURE_RESP_CODE, Constants.GENERIC_FAILURE_MESS);
		}finally{
			//logTransaction(TransactionCodes.BALENQ.toString(), enquirey.getMerchantId(), enquirey.getUserId(), enquirey.getMobileNumber(), enquirey.getAccountNumber(), enquirey.getChannel(), responseDTO.getMessage(), responseDTO.getResponseCode());
			enquirey=null;
			store = null;
			credentials=null;
			data = null;
		}
		return responseDTO;
	}
	
	@Override
	public ResponseDTO miniStatement(MiniStatement statement) {
		logger.debug("miniStatement...");
		ResponseDTO dto=null;
		try{
			UserLoginCredentials credentials = loginDao.getById(statement.getUserId());
			Store store = merchantDao.getStoreById(credentials.getStore());
			if(credentials != null && Status.A.toString().equals(credentials.getStatus())){
				statement.setAccountNumber(store.getAccountNumber());
				statement.setFromDate(statement.getFromDate()+"T00:00:00");
				statement.setEndDate(statement.getEndDate()+"T23:59:59");
				logger.debug(statement.toString());
				dto = finacleService.miniStatement(statement);
				logger.debug("miniStatement..");
				if(dto!=null){
					logTransaction(TransactionCodes.STMT.toString(), statement.getMerchantId(), statement.getUserId(), statement.getMobileNumber(), statement.getAccountNumber(), statement.getChannel(), dto.getMessage(), dto.getResponseCode());
				}else{
					logTransaction(TransactionCodes.STMT.toString(), statement.getMerchantId(), statement.getUserId(), statement.getMobileNumber(), statement.getAccountNumber(), statement.getChannel(), Constants.UNEXPECTED_ERROR_MESSAGE, Constants.UNEXPECTED_ERROR_CODE);
				}
			}else{
				dto = new ResponseDTO();
				dto.setMessage(Constants.USER_INACTIVE_MESSAGE);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
				}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error whil generating ministatement..:"+e.getLocalizedMessage());
		}finally{
			statement=null;
		}
		return dto;
	}
	
	private com.ceva.db.model.AccountBean generateAccountBean(AccountBean accountBean){
		com.ceva.db.model.AccountBean bean = new com.ceva.db.model.AccountBean();
		try{
			bean.setAddress(accountBean.getAccountNumber());
			bean.setChannel(accountBean.getChannel());
			bean.setCity(accountBean.getCity());
			bean.setDob(accountBean.getDob());
			bean.setEmail(accountBean.getEmail());
			bean.setFirstName(accountBean.getFirstName());
			bean.setGender(accountBean.getGender());
			bean.setId(Long.parseLong(accountBean.getMandateCard()));
			bean.setLastName(accountBean.getLastName());
			bean.setMaritalStatus(accountBean .getMaritalStatus());
			bean.setMerchantId(accountBean.getMerchantId());
			bean.setMidName(accountBean.getMidName());
			bean.setPhone(accountBean.getMobileNumber());
			bean.setSalutation(accountBean.getSalutation());
			bean.setMobileNumber(accountBean.getMobileNumber());
			bean.setState(accountBean.getState());
			bean.setUserId(accountBean.getUserId());
			bean.setStatus(TransactionStatus.FAILURE.toString());
		}catch(Exception e){
			logger.error("Error While generating bean..!"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return bean;
	}
	
	private void logTransaction(String txnCode, String merchantId, String userId, String mobileNumber, String toActNumber, long channel, String errorMessage, String errorCode) {
		NonFinTransaction transaction = null;
		try{
			transaction = new NonFinTransaction();
			transaction.setBankId(0);
			transaction.setChannel(channel);
			transaction.setApprovedBy(userId);
			transaction.setErrorDescription(errorMessage);
			try{
				transaction.setErrorCode(Long.parseLong(errorCode));
			}catch(Exception e){
				logger.error("Error..:"+e.getLocalizedMessage());
				e.printStackTrace();
				transaction.setErrorCode(90);
			}
			transaction.setMerchantId(merchantId);
			transaction.setRefNumber(System.nanoTime());
			transaction.setTerminalNumber(mobileNumber);
			transaction.setToAcNum(Long.parseLong(toActNumber));
			transaction.setTxnCode(txnCode);
			transaction.setTxndateTime(new Date());

			logger.debug(transaction.toString());
			transactionDao.save(transaction);

		}catch(Exception e){
			logger.error("Error..:"+e.getLocalizedMessage());
			//e.printStackTrace();
			logger.error("Transaction Details..:"+transaction.toString());
		}
	}
	
	private Transaction createTxn(com.ceva.db.model.AccountBean accountBean, String accountNumber, String respCode, String status) {
		Transaction transaction = new Transaction();
		try{
			transaction.setApprovedBy(accountBean.getUserId());
			transaction.setMerchantId(accountBean.getMerchantId());
			transaction.setBankId("011");
			transaction.setRefNumber(accountBean.getId()+"");
			transaction.setChannel(accountBean.getChannel());
			transaction.setStatus(status);
			transaction.setResponseCode(respCode);
			transaction.setCustomerAccountNumber(accountBean.getAccountNumber());
			transaction.setAgentAccountNumber("");
			transaction.setNarration("");
			transaction.setTerminalNumber(accountBean.getMobileNumber());
			transaction.setTxnCode(TransactionCodes.OPENACT.toString());
			transaction.setTxndateTime(new Date());
			transaction.setAmount(new BigDecimal(0));
			transaction.setErrorDescription(accountBean.getStatus());
		}catch(Exception e){
			e.printStackTrace();
		}
		return transaction;
	}

}
