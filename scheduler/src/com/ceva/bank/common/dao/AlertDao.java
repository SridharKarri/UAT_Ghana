package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.Alert;
import com.ceva.db.model.DailyReport;

public interface AlertDao {

	public List<Alert> getAll(String applCode);
	public List<Alert> getAllUserPendingEmail(String paramString);
	public List<Alert> getPinEmail(String txnCode, String appCode);
	public void update(Alert alert);
	public void save(Alert alert);
	public DailyReport getDailyReport();
	public List<DailyReport> getDailyReports();
	public void updateDailyReport(DailyReport dailyReport);
}