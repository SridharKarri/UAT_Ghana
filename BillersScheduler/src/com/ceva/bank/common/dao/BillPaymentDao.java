package com.ceva.bank.common.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ceva.db.model.BillPayment;
import com.ceva.db.model.BillerCategory;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.PackageFormItem;

public interface BillPaymentDao {
	public List<Billers> getBillers();
	public Billers getBiller(long paramLong);
	public Billers getBiller(String paramString);
	public List<Billers> getBillers(long paramLong);
	public List<BillerPackages> getBillerPackages(String paramString);
	public List<BillPayment> getBillPayment(String paramString, Date paramDate);
	public void update(BillPayment paramBillPayment);
	public BillerCategory getCategorybyId(String paramString);
	public List<BillerCategory> getBillerCategories();
	public void update(BillerCategory paramBillerCategory);
	public void update(Billers paramBillers);
	public BillerPackages getBillerPackagbyId(String paramString);
	public void update(BillerPackages paramBillerPackages);
	public List<BillerPackages> getBillerPackages();
	public PackageFormItem getPackageFormItem(Long paramLong);
	public void update(PackageFormItem paramPackageFormItem);
}