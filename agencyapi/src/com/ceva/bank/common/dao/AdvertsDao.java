package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.db.model.Adverts;

public interface AdvertsDao {
	public List<Adverts> listAdverts(BalanceEnquirey enquirey);
	public boolean update(Adverts adds);
	public Adverts getById(long id);
}
