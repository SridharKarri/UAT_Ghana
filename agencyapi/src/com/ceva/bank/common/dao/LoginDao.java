package com.ceva.bank.common.dao;

import java.util.List;
import java.util.Map;

import com.ceva.bank.common.beans.ChangePin;
import com.ceva.bank.common.beans.DeviceRegister;
import com.ceva.db.model.ChannelUsers;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;

public interface LoginDao {
	public ResponseDTO validate(UserLoginCredentials userBean);
	public ResponseDTO changePwd(ChangePin bean);
	public ResponseDTO updateProfile(UserLoginCredentials bean);
	public ResponseDTO updateProfilePicture(Map<String, Object> map);
	public ResponseDTO registerDevice(DeviceRegister bean);
	public UserLoginCredentials getById(String userId);
	public boolean validateOtp(String mobile, String otp);
	public ResponseDTO login(UserLoginCredentials credentials);
	public UserLoginCredentials getByIdForFirstLogin(String userId);
	public List<ChannelUsers> getChannelUsers(String userId);
	
}
