package com.ceva.service;

import java.util.List;
import java.util.Map;

import com.ceva.db.model.Transaction;

public interface TransactionService {

	public long save(Transaction transaction);
	public boolean update(Transaction transaction);
	public Transaction getById(long id);
	public List<Transaction> getAll(Map<String, Object> condtions);
}
