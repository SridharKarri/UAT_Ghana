package com.ceva.bank.common.dao;

import com.ceva.db.model.Cashwithdrawal;

public interface CashWithDrawalDao {
	public long save(Cashwithdrawal cashwithdrawal);
	public Cashwithdrawal update(Cashwithdrawal cashwithdrawal);
	public Cashwithdrawal getById(long ref);
}
