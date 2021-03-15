package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.VisitShop;

public interface VisitShopDao {
	public VisitShop getById(long id);
	public List<VisitShop> listAll(String userId);
	public Long save(VisitShop item);
	public boolean update(VisitShop item);
}
