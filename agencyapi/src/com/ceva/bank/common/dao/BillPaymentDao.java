package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.bank.common.beans.Services;
import com.ceva.db.model.BillerMakets;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.Service;

public interface BillPaymentDao {

	public List<Service> getServices(Services services);
	public List<Billers> getBillers();
	public Billers getBiller(long id);
	public List<Billers> getBillers(long sserviceId);
	public List<Billers> MMObillers(String ServiceCode);
	public List<BillerPackages> getBillerPackages(long billerId);
	public List<BillerMakets> getBillerMarkets(long billerId);
	public void saveQuicktellerTxn(String id, String request, String Response, String staus);
	public BillerPackages getBillerPackage(String string, String packId);
	public boolean save(com.ceva.db.model.BillPayment billPayment);

}
