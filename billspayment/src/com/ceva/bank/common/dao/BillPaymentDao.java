package com.ceva.bank.common.dao;

import java.util.List;

import com.ceva.bank.common.beans.Services;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.PackageFormItem;
import com.ceva.db.model.Service;

public interface BillPaymentDao {

	public List<Service> getServices(Services services);
	public List<Billers> getBillers();
	public Billers getBiller(long id);
	public List<Billers> getBillers(long sserviceId);
	public List<Billers> MMObillers(String ServiceCode);
	public List<BillerPackages> getBillerPackages(long billerId);
	public BillerPackages getBillerPackageById(long packId);
	public List<PackageFormItem> getBillerFormItems(long packId);
	public PackageFormItem getBillerFormItemByPackId(Long packId);
	public BillerPackages getBillerPackage(String string, String packId);
	public boolean save(com.ceva.db.model.BillPayment billPayment);
	public String getLanguage(String userId);

}
