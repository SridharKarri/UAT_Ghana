package com.ceva.cron.listener;

import com.ceva.bank.common.dao.BillPaymentDao;
import com.ceva.bank.services.BillerBankService;
import com.ceva.db.model.BillerCategory;
import com.ceva.db.model.BillerPackages;
import com.ceva.db.model.Billers;
import com.ceva.db.model.PackageFormItem;
import java.math.BigDecimal;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;




public class BillerJob
{
	private static Logger log = Logger.getLogger(BillerJob.class);

	@Autowired
	private BillPaymentDao billPaymentDao;

	@Autowired
	private BillerBankService billerBankService;

	public void syncAirTimeBillers() {
		log.info("sync AirTimeBillers with bank..START");
		JSONObject response = billerBankService.getNetworks();

		try {
			if ("00".equals(response.getString("ResponseCode")))
			{ JSONArray billers = response.getJSONArray("Billers");
			if (billers != null) {
				for (int i = 0; i < billers.size(); i++) {
					JSONObject biller = billers.getJSONObject(i);
					Billers dbBiller = billPaymentDao.getBiller(biller.getString("Id"));
					if (dbBiller == null) {
						dbBiller = new Billers(biller.getString("Id"), biller.getString("Name"), 1L);
					} else {
						dbBiller.setBillerName(dbBiller.getBillerName());
					}  billPaymentDao.update(dbBiller);
				} 
			} }
			else
			{ log.error("Failure Response from api.:" + response.getString("ResponseMessage")); } 
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
		} 
	}


	public void syncCategories() {
		log.info("sync Categories with bank..START");
		JSONObject response = billerBankService.getBillerCategories();
		try {
			if ("00".equals(response.getString("ResponseCode")))
			{ JSONArray categories = response.getJSONArray("Categories");
			if (categories != null && categories.size() > 0) {
				for (int i = 0; i < categories.size(); i++) {
					JSONObject category = categories.getJSONObject(i);
					BillerCategory billerCategory = billPaymentDao.getCategorybyId(category.getString("Id"));
					if (billerCategory == null) {
						billerCategory = new BillerCategory(category.getString("Id"), category.getString("Name"));
					} else {
						billerCategory.setCatId(category.getString("Id"));
						billerCategory.setName(category.getString("Name"));
					} 
					billPaymentDao.update(billerCategory);
				} 
			} }
			else
			{ log.error("Failure Response from api.:" + response.getString("ResponseMessage")); } 
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			response = null;
		} 
	}

	public void syncBillers() {
		log.info("sync Billers with bank..START");
		JSONObject response = null;
		List<BillerCategory> categories = billPaymentDao.getBillerCategories();
		try {
			for (BillerCategory category : categories) {
				response = billerBankService.getBillers(category.getCatId());
				if ("00".equals(response.getString("ResponseCode"))) {
					JSONArray billers = response.getJSONArray("Billers");
					if (billers != null && billers.size() > 0)
						for (int i = 0; i < billers.size(); i++) {
							JSONObject biller = billers.getJSONObject(i);
							Billers dbBiller = billPaymentDao.getBiller(biller.getString("Id"));
							if (dbBiller == null) {
								dbBiller = new Billers(biller.getString("Id"), biller.getString("Name"), category.getId());
							} else {
								dbBiller.setBillerId(biller.getString("Id"));
								dbBiller.setBillerName(biller.getString("Name"));
							} 
							billPaymentDao.update(dbBiller);
						}  
					continue;
				} 
				log.error("Failure Response from api.:" + response.getString("ResponseMessage"));
			} 
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			response = null;
		} 
	}

	public void syncBillPaymentItems() {
		log.info("sync BillPaymentItems with bank..START");
		JSONObject response = null;
		List<Billers> billers = billPaymentDao.getBillers();

		try {
			for (Billers biller : billers) {
				response = billerBankService.getBillerPaymentItems(biller.getBillerId());
				if ("00".equals(response.getString("ResponseCode"))) {
					JSONArray packages = response.getJSONArray("Products");
					if (packages != null && packages.size() > 0)
						for (int i = 0; i < packages.size(); i++) {
							JSONObject pack = packages.getJSONObject(i);
							BillerPackages dbPackage = billPaymentDao.getBillerPackagbyId(pack.getString("Id"));
							if (dbPackage == null) {
								dbPackage = new BillerPackages();
							}
							dbPackage.setBillerId((new StringBuilder(String.valueOf(biller.getId()))).toString());
							dbPackage.setDisplayName(pack.getString("DisplayName"));
							dbPackage.setPackId(pack.getString("Id"));
							dbPackage.setCharge(new BigDecimal(pack.getDouble("ProductAmount")));
							dbPackage.setStatus("A");
							billPaymentDao.update(dbPackage);
						}  
					continue;
				} 
				log.error("Failure Response from api.:" + response.getString("ResponseMessage"));
			} 
		} catch (Exception e) {
			log.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			response = null;
		} 
	}
	public void syncBillPaymentFormItems() {
		log.info("sync BillPaymentFormItems with bank..START");
		JSONObject response = null;
		List<BillerPackages> packages = billPaymentDao.getBillerPackages();
		try {
			for (BillerPackages pack : packages) {
				response = billerBankService.getBillerFormItems(pack.getPackId());
				if ("00".equals(response.getString("ResponseCode"))) {
					JSONArray productFormItems = response.getJSONArray("ProductFormItems");
					PackageFormItem formItem = billPaymentDao.getPackageFormItem(Long.valueOf(pack.getId()));
					if (formItem == null)
						formItem = new PackageFormItem(); 
					formItem.setFormItems(productFormItems.toString());
					formItem.setPackId(Long.valueOf(pack.getId()));
					billPaymentDao.update(formItem);
					continue;
				} 
				log.error("Failure Response from api.:" + response.getString("ResponseMessage"));
			} 
		} catch (Exception e) {
			log.error("BillPaymentFormItems Reason..:" + e.getLocalizedMessage());
		} finally {
			response = null;
		} 
	}
}