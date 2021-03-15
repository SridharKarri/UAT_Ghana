package com.ceva.service;

import com.ceva.bank.common.beans.CashWithdrawalConfirm;
import com.ceva.bank.common.beans.CashWithdrawalInit;
import com.ceva.dto.ResponseDTO;

public interface CashWithdrawalService {
	public ResponseDTO cashByAccountInit(CashWithdrawalInit withdrawalInit);
	public ResponseDTO cashByAccountConfirm(CashWithdrawalConfirm withdrawalConfirm);
}
