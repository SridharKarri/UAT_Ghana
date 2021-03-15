package com.ceva.bank.common.dao;

import com.ceva.db.model.SyncProperties;

public interface SyncPropertiesDao {
	public SyncProperties getByKey(String key);
	public Boolean update(SyncProperties properties);
}
