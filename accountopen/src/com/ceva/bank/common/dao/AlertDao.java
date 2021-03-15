package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.Alert;

public interface AlertDao {
public List<Alert> getAll(String applCode);
public void update(Alert alert);
public void save(Alert alert);
}
