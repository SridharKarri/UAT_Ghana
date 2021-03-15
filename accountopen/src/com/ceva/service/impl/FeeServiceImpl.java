package com.ceva.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.dao.FeeDao;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Merchant;
import com.ceva.db.model.ServiceProducts;
import com.ceva.db.model.Store;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.FeeService;
import com.nbk.util.Constants;

@Service("feeService")
public class FeeServiceImpl implements FeeService {

	@Autowired
	FeeDao feeDao;

	@Autowired
	MerchantDao merchantDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	LoginDao loginDao;
	
	Logger log = Logger.getLogger(FeeServiceImpl.class);

	@Override
	public Fee getFee(String serviceId, long channel, String merchantId, BigDecimal amount, String txnType) {
		log.info("Getting fee for service ..:"+serviceId);
		return feeDao.getFee(serviceId, channel, merchantId, amount,txnType);
	}

	@Override
	public ResponseDTO getFee(String serviceCode, String agentId, BigDecimal amount, String userId) {
		log.info("Getting fee for service ..:"+serviceCode);
		Merchant merchant = null;
		Store store = null;
		UserLoginCredentials credentials = null;
		com.ceva.db.model.Service service = null;
		ServiceProducts serviceProducts = null;
		Fee fee = null;
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> conditions = null;
		try{
			credentials = loginDao.getById(userId);
			if(credentials!= null){
				merchant = merchantDao.getById(agentId);
				if(merchant != null){
					service = feeDao.getService(serviceCode);
					store = merchantDao.getStoreById(credentials.getStore());
					conditions = new HashMap<String, Object>();
					conditions.put("MERCHANTID", merchant.getMerchantID());
					serviceProducts =  feeDao.getServiceProducts(service.getsId(), Long.parseLong(merchant.getProduct()));
					if(serviceProducts !=null){
						if(serviceProducts.getTxnLimit().longValue() >= amount.longValue()){
							fee = feeDao.getFee(serviceProducts.getSpId(), amount);
							if(fee!=null){
								log.info("Service has fee.");
								responseDTO.setFee(fee.getFeeVal());
							}else{
								log.info("Service has No fee, setting 0 fee.");
								responseDTO.setFee("0");
							}
							responseDTO.setMessage(Constants.SUCESS_SMALL);
							responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
						}else{
							log.info("Amount is greaterthan service Limit, limit exceeded.");
							responseDTO.setMessage(Constants.SERVICE_LIMIT_EXCEED_MESSAGE);
							responseDTO.setResponseCode(Constants.SERVICE_LIMIT_EXCEED_CODE);
						}
					}else{
						log.info("serviceProducts is null.");
						responseDTO.setMessage(Constants.SERVICE_NOT_MAPPED_MESSAGE);
						responseDTO.setResponseCode(Constants.SERVICE_NOT_MAPPED_CODE);
					}
				}else{
					log.info("merchant is null or deactive.");
					responseDTO.setMessage(Constants.NO_MERCHANT_MESSAGE);
					responseDTO.setResponseCode(Constants.NO_MERCHANT);
				}
				
			}else{
				log.info("User Status Deactive..");
				responseDTO.setData(null);
				responseDTO.setMessage(Constants.USER_INACTIVE_MESSAGE);
				responseDTO.setResponseCode(Constants.USER_INACTIVE_CODE);
			}
		}catch(Exception e){
			log.error("Error while getting fee..");
			log.error("Reason..:"+e.getLocalizedMessage());
		}finally{
			merchant = null;
			service = null;
			serviceProducts = null;
			conditions = null;
		}
		log.info("Getting fee for service ..:end");
		return responseDTO;
	}


}
