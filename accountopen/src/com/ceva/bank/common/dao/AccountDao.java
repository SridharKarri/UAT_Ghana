package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.db.model.AccountBean;
import com.ceva.db.model.AccountImage;

public interface AccountDao {
	public AccountImage getById(Long id);
	public boolean getByPhone(String phone);
	public AccountBean getByKey(Long id);
	public List<AccountBean> listAll();
	public boolean save(AccountBean accountBean);
	public boolean update(AccountBean accountBean);
	public Long save(AccountImage accountImage);
	public boolean update(AccountImage accountImage);
}
