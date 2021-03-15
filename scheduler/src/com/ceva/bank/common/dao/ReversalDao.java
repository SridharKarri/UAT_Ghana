package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.Reversals;

public interface ReversalDao {
	public Long save(Reversals reversals);
	public Reversals getByRefNumber(String Id);
	public List<Reversals> getByStatus(String Id);
	public void update(Reversals reversals);
}
