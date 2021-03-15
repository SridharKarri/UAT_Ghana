package com.ceva.bank.common.dao;

import com.ceva.db.model.OtpBean;
import com.ceva.dto.ResponseDTO;

public interface OtpDao {
	public ResponseDTO save(OtpBean bean);
	public ResponseDTO getOtp(OtpBean bean);
	public ResponseDTO updateOtp(OtpBean bean);
}
