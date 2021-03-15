package com.ceva.bank.common.dao;

import com.ceva.db.model.UserLoginCredentials;

public interface LoginDao {
	public UserLoginCredentials getById(String userId);
}
