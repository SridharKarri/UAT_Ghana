package com.ceva.bank.common.dao;

import com.ceva.db.model.AccountOpeningMaster;

public interface AcctOpenMasterDao {
	public AccountOpeningMaster getJsonDataforService(String serviceName);
}