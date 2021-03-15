package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.LogComplaint;

public interface LogComplaintDao {
	public LogComplaint getById(long id);
	public List<LogComplaint> listAll(String userId);
	public Long save(LogComplaint item);
}
