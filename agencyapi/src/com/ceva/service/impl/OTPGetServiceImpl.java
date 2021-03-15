package com.ceva.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.OtpBean;
import com.ceva.bank.common.dao.OTPGetDao;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.OTPGetService;
import com.nbk.util.Constants;

@Service("otpGetService")
public class OTPGetServiceImpl  implements OTPGetService {

	Logger log = Logger.getLogger(OTPGetServiceImpl.class);
	
	@Autowired
	OTPGetDao otpGetDao; 
	
	@Override
	public ResponseDTO getOtp(OtpBean bean) {
		ResponseDTO responseDTO =new ResponseDTO();
		try{
			responseDTO.setData(otpGetDao.getOTP(bean.getUserId()));
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