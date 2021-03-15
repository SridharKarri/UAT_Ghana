package com.ceva.bank.common.dao;

import com.ceva.db.model.OtpBean;
import com.ceva.dto.ResponseDTO;

public interface OtpDao {
	public ResponseDTO save(OtpBean bean);
	public ResponseDTO validate(String Mobile, String otp);
	public ResponseDTO getOtp(OtpBean bean);
	public ResponseDTO updateOtp(OtpBean bean);
	public void saveWalletTxn(long reference, String url, String response, String status);
}
