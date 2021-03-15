package com.ceva.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.utils.Status;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.Store;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.response.beans.LoginResponse;
import com.ceva.service.LoginService;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;
@Service("loginService")
public class LoginServiceImpl implements LoginService {

	Logger log = Logger.getLogger(LoginServiceImpl.class);

	@Autowired
	LoginDao loginDao;

	@Autowired
	MerchantDao merchantDao;

	@Override
	public ResponseDTO validate(UserLoginCredentials userBean) {
		log.info("validating user with transaction pin.");
		ResponseDTO responseDTO = null;
		UserLoginCredentials credentials=null;
		LoginResponse loginResponse =null;
		Merchant merchant = null;
		Store store = null;
		try{
			//responseDTO =loginDao.validate(userBean);
			responseDTO =loginDao.login(userBean);
			if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
				log.info("success while validating transaction pin.");
				credentials = (UserLoginCredentials) responseDTO.getData();
				if(Status.A.toString().equals(credentials.getStatus())||Status.F.toString().equals(credentials.getStatus())){
					log.info("User Status active..");
					merchant = merchantDao.getById(credentials.getAgent());
					store = merchantDao.getStoreById(credentials.getStore());
					if(merchant != null && store != null){
						loginResponse = constructLoginResponse(credentials, merchant);
						loginResponse.setAcountNumber(CommonUtil.maskAccountNumber(store.getAccountNumber(), "*"));
						List<String> txnCurrencies= getTransactionCurrenies();
						loginResponse.setTxnCurrencies(txnCurrencies);
						responseDTO.setData(loginResponse);
						responseDTO.setCommission(loginResponse.getCanOpenAccount());
					}else{
						log.info("Agent or Store Status Deactive..");
						responseDTO.setData(null);
						responseDTO.setMessage(Constants.AGENT_STORE_INACTIVE_MESSAGE);
						responseDTO.setResponseCode(Constants.AGENT_STORE_INACTIVE_CODE);
					}
				}else{
					log.info("User Status Deactive..");
					responseDTO.setData(null);
					responseDTO.setMessage(Constants.USER_INACTIVE_MESSAGE);
					responseDTO.setResponseCode(Constants.USER_INACTIVE_CODE);
				}

			}
		//	log.info(responseDTO.toString());
		}catch(Exception e){
			credentials = null;
			log.error("validating user with transaction pin.");
			log.error("Error..:"+e.getLocalizedMessage());
		}finally{
			credentials = null;
			loginResponse = null;
		}
		log.info(responseDTO.toString());
		return responseDTO;
	}

	private List<String> getTransactionCurrenies() {
		String[] txnCurrencies=ConfigLoader.convertResourceBundleToMap("config").get("bank.country.currency").split(",");
		return Arrays.asList(txnCurrencies);
	}

	private LoginResponse constructLoginResponse(
			UserLoginCredentials credentials, Merchant merchant) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUserName(credentials.getUserName());
		loginResponse.setMobileNo(credentials.getMobileNo());
		loginResponse.setLastLoggedIn(credentials.getLastLoggedIn() + "");
		loginResponse.setEmail(credentials.getEmail());
		loginResponse.setUserId(credentials.getUserId());
		loginResponse.setAgent(credentials.getAgent());
		loginResponse.setStore(credentials.getStore());
		loginResponse.setStatus(credentials.getStatus());
		loginResponse.setAgentType(credentials.getUserType());
		loginResponse.setCanOpenAccount(merchant.getCanOpenAccount());
		return loginResponse;
	}

	@Override
	public ResponseDTO login(UserLoginCredentials userBean) {
		log.info("validating user with transaction pin.");
		return loginDao.login(userBean);
	
	}

	@Override
	public ResponseDTO loadUser(String userId) {
		ResponseDTO responseDTO =  new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
		responseDTO.setData(loginDao.getById(userId).getUserId());
		return responseDTO;
	}
}
