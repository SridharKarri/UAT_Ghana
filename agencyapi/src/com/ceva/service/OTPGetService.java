package com.ceva.service;

import com.ceva.bank.common.beans.OtpBean;
import com.ceva.dto.ResponseDTO;

public interface OTPGetService {

	public ResponseDTO getOtp(OtpBean bean);
	
}