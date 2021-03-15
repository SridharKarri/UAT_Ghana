package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.ChatItem;

public interface ChatDao {
	public ChatItem getById(long id);
	public List<ChatItem> listAll(String userId);
	public boolean save(ChatItem item);
	public boolean update(ChatItem item);
}
