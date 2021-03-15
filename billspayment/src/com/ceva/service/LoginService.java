package com.ceva.service;

import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;

public interface LoginService {
	public ResponseDTO validate(UserLoginCredentials userBean);
	public ResponseDTO login(UserLoginCredentials userBean);
	public ResponseDTO loadUser(String userId);

}
