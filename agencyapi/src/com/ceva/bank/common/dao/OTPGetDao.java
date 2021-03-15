package com.ceva.bank.common.dao;

import java.util.List;

public interface OTPGetDao {

	public List<String> getOTP(String userId);
}