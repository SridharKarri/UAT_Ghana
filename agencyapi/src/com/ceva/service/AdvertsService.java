package com.ceva.service;

import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.db.model.Adverts;
import com.ceva.dto.ResponseDTO;

public interface AdvertsService {
	public ResponseDTO listAdverts(BalanceEnquirey enquirey);
	public Adverts getById(long id);
	public ResponseDTO update(Adverts add);
}
