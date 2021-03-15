package com.ceva.service;

import com.ceva.bank.common.beans.MMOperators;
import com.ceva.bank.common.beans.MMProductFormItems;
import com.ceva.bank.common.beans.MMProducts;
import com.ceva.bank.common.beans.MMValidateRequest;
import com.ceva.dto.ResponseDTO;

public interface MobileMoneyService {
	public ResponseDTO getMMOperators(MMOperators bean);
	public ResponseDTO getMMProducts(MMProducts bean);
	public ResponseDTO getMMProductFormItems(MMProductFormItems bean);
	public ResponseDTO mmvalidatereq(MMValidateRequest bean);
	public ResponseDTO commitMobilePayment(MMValidateRequest bean);
}