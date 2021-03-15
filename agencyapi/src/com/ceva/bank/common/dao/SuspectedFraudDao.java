package com.ceva.bank.common.dao;

import com.ceva.db.model.SuspectedFraud;

public interface SuspectedFraudDao {
	public SuspectedFraud getByAccountOrCard(String accountOrCard);
}
