package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.Notification;

public interface NotificationDao {

	public List<Notification> getAll(String applCode);

	public void update(Notification alert);

	public void save(Notification alert);

}
