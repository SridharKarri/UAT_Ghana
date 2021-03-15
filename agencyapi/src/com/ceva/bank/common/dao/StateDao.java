package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.City;
import com.ceva.db.model.States;

public interface StateDao {
	public List<States> listStates();
	public List<City> listCities(String stateid);
}
