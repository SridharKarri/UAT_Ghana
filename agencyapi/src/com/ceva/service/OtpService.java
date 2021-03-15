package com.ceva.service;

import com.ceva.bank.common.beans.OtpBean;
import com.ceva.dto.ResponseDTO;

public interface OtpService {
	public ResponseDTO sendOtp(OtpBean bean);
	public ResponseDTO listStates();
	public ResponseDTO listCities(String stateid);
}
