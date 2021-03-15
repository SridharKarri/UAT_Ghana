package com.ceva.bank.common.dao;

import com.ceva.db.model.TransferRequest;

public interface FinacleDao {

	public TransferRequest getById(long id);
	public long save(TransferRequest request);
	public long update(TransferRequest request);
}