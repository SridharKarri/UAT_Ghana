package com.ceva.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.db.model.Merchant;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.AgentService;
import com.nbk.util.Constants;

@Service("agentService")
public class AgentServiceImpl implements AgentService {

	Logger logger = Logger.getLogger(AgentService.class);
	@Autowired
	MerchantDao merchantDao;

	@Override
	public ResponseDTO loagAgent(String id) {
		logger.info("loading agent..:"+id);
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			Merchant merchant = merchantDao.getById(id);
			merchant.setAccountNumber("");
			responseDTO.setData(merchant);
			responseDTO.setMessage(Constants.SUCESS_SMALL);
			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error While Loading agent");
			responseDTO.setMessage(Constants.ERROR_MESSAGE_VALUE);
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		logger.info(responseDTO.toString());
		return responseDTO;
	}

}
