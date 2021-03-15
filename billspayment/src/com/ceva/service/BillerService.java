package com.ceva.service;

import java.util.List;

import com.ceva.bank.common.beans.BillPayment;
import com.ceva.bank.common.beans.BillePaymentItems;
import com.ceva.bank.common.beans.BillerFormItems;
import com.ceva.bank.common.beans.BillersBean;
import com.ceva.bank.common.beans.Services;
import com.ceva.db.model.Billers;
import com.ceva.db.model.Service;
import com.ceva.dto.ResponseDTO;

public interface BillerService {
	public List<Service> getServices(Services services);
	public List<Billers> getBillers(BillersBean bean);
	public Billers getById(long id);
	public ResponseDTO getBillerPackages(BillePaymentItems billerPackages);
	public ResponseDTO getBillerFormItems(BillerFormItems formItems);
	public ResponseDTO validatePaybill(BillPayment billPayment);
	public ResponseDTO mobileRecharge(BillPayment billPayment);
	public ResponseDTO commitPaybill(BillPayment billPayment);
	public List<Billers> MMObillers(BillersBean bean);
	public String getLanguage(String userId);
}