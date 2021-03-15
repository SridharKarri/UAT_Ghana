package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.ChannelUsers;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;

public interface LoginDao {
	public ResponseDTO validate(UserLoginCredentials userBean);
	public UserLoginCredentials getById(String userId);
	public boolean validateOtp(String mobile, String otp);
	public ResponseDTO login(UserLoginCredentials credentials);
	public List<ChannelUsers> getChannelUsers(String userId);
	
}
