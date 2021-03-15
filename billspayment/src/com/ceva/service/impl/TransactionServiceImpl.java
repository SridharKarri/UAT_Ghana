package com.ceva.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.db.model.Transaction;
import com.ceva.service.TransactionService;

@Repository("transactionService")
public class TransactionServiceImpl implements TransactionService {

	Logger log = Logger.getLogger(TransactionServiceImpl.class);

	@Autowired
	TransactionDao transactionDao;
	@Override
	public long save(Transaction transaction) {
		log.info("Saving transaction");
		return transactionDao.save(transaction);
	}

	@Override
	public boolean update(Transaction transaction) {
		log.info("Update transaction");
		return transactionDao.update(transaction);
	}

	@Override
	public Transaction getById(long id) {
		log.info("getById transaction START");
		return transactionDao.getById(id);
	}

	@Override
	public List<Transaction> getAll(Map<String, Object> condtions) {
		return null;
	}

}
