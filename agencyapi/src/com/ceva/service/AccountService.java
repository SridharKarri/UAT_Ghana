package com.ceva.service;

import com.ceva.bank.common.beans.AccountBean;
import com.ceva.db.model.AccountImage;
import com.ceva.dto.ResponseDTO;

public interface AccountService {

	public ResponseDTO openAccount(AccountBean bean);
	public ResponseDTO loadBranches();
	public ResponseDTO loadParameters();
	public ResponseDTO loadCountries();
	public ResponseDTO loadStates();
	public ResponseDTO loadIdentityCategories();
	public ResponseDTO loadCities();
	public ResponseDTO loadNationalities();
	public ResponseDTO loadMaritalStasuses();
	public ResponseDTO loadOccupations();
	public ResponseDTO loadSalutaions();
	public ResponseDTO loadIdentityTypeByCategories(String catId);
	public AccountImage getById(Long id);
	public Long save(AccountImage image);
	public boolean update(AccountImage image);
}
