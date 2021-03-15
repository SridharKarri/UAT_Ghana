package com.ceva.bank.common.dao;

import java.util.List;
import java.util.Map;

import com.ceva.db.model.BanksAndWallets;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Store;
import com.ceva.db.model.Transaction;

public interface TransactionDao {

	public long save(Transaction transaction);
	public boolean update(Transaction transaction);
	public Transaction getById(long id);
	public List<Transaction> getAll(Map<String, Object> condtions);
	public void logError(String error, String errorCode, String merchantId, String narration, String userId);
	public List<BanksAndWallets> getBanks(String id);
	int getStoreTxnCount(String storeId);
	BanksAndWallets getBank(String bankCode);
}
