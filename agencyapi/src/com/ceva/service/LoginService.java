package com.ceva.service;

import java.util.Map;

import com.ceva.bank.common.beans.AppVersion;
import com.ceva.bank.common.beans.ChangePin;
import com.ceva.bank.common.beans.DeviceRegister;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;

public interface LoginService {
	public ResponseDTO validate(UserLoginCredentials userBean);
	public ResponseDTO login(UserLoginCredentials userBean);
	public ResponseDTO changePwd(ChangePin bean);
	public ResponseDTO updateProfile(UserLoginCredentials bean);
	public ResponseDTO updateProfilePicture(Map<String, Object> map);
	public ResponseDTO registerDevice(DeviceRegister bean);
	public ResponseDTO loadUser(String userId);

}
