package com.ceva.service;

import com.ceva.bank.common.beans.NameEnquirey;
import com.ceva.bank.common.beans.QueryPayment;
import com.ceva.bank.common.beans.TransferRequest;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

public interface FundTransferService extends Constants {

	public ResponseDTO getBanks();
	public ResponseDTO nameEnquirey(NameEnquirey enquirey);
	public ResponseDTO nameEnquireyOtherBank(NameEnquirey enquirey);
	public ResponseDTO processIntraBank(TransferRequest transferRequest);
	public ResponseDTO processInterBank(TransferRequest transferRequest);
	//public ResponseDTO processwalletDeposit(TransferRequest transferRequest);
	public ResponseDTO transactionStatusQuery(QueryPayment transfer);
}
