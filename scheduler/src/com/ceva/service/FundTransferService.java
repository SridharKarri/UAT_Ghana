package com.ceva.service;

import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

public interface FundTransferService extends Constants {

	public ResponseDTO transactionStatusQuery(String refNum);
}
