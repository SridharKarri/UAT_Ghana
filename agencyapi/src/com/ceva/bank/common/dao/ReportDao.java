package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.bank.common.beans.TransactionReport;
import com.ceva.db.model.Transaction;

public interface ReportDao {

	public List<Transaction> sucessTransactionReport(TransactionReport report);
	public List<Transaction> summaryReport(TransactionReport report);
}
