package com.ceva.service.impl;

import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.bank.common.beans.MiniStatement;
import com.ceva.bank.common.beans.UserLocation;
import com.ceva.dto.ResponseDTO;

public interface CoreService {
	public ResponseDTO queryAccount(BalanceEnquirey bean);
	public ResponseDTO miniStatement(MiniStatement statement);
	public ResponseDTO balanceEnquirey(BalanceEnquirey enquirey);
	public ResponseDTO updateUserLocation(UserLocation bean);
}
