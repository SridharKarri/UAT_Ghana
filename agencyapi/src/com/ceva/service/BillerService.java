package com.ceva.service;

import java.util.List;

import com.ceva.bank.common.beans.BillPayment;
import com.ceva.bank.common.beans.BillePaymentItems;
import com.ceva.bank.common.beans.BillersBean;
import com.ceva.bank.common.beans.Services;
import com.ceva.bank.common.beans.ValidateCustomer;
import com.ceva.db.model.Billers;
import com.ceva.db.model.Service;
import com.ceva.dto.ResponseDTO;

public interface BillerService {
	public List<Service> getServices(Services services);
	public List<Billers> getBillers(BillersBean bean);
	public Billers getById(long id);
	public ResponseDTO getBillerPackages(BillePaymentItems billerPackages);
	public ResponseDTO getPaymentItemsAndMarkets(BillePaymentItems billerPackages);
	public ResponseDTO payBill(BillPayment billPayment);
	public ResponseDTO mobileRecharge(BillPayment billPayment);
	public ResponseDTO validateCustomer(ValidateCustomer customer);
	public ResponseDTO queryStatus(String refnumber);
	public List<Billers> MMObillers(BillersBean bean);
}
