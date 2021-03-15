package com.ceva.security;

import java.util.ResourceBundle;

import com.ceva.bank.common.dao.impl.SecurityDAOImpl;

public class FilterFactory {

	private static ResourceBundle bundle = ResourceBundle.getBundle("filter");
	private static SecurityDAOImpl dao = new SecurityDAOImpl();

	private FilterFactory(){
		bundle=null;
		dao=null;
	}

	public static ResourceBundle getBundle() {
		return bundle;
	}

	public static SecurityDAOImpl getDao() {
		return dao;
	}
}
