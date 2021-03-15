package com.ceva.bank.services;

import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("billerBankService")
public class BillerBankService
implements Constants
{
	private static Logger logger = Logger.getLogger(BillerBankService.class);
	private static String countryId = (String)ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId");
	private static String getNetworksurl = (String)ConfigLoader.convertResourceBundleToMap("config").get("bank.bills.networks");
	private static String getCategories = (String)ConfigLoader.convertResourceBundleToMap("config").get("bank.bills.categories");
	private static String getBillers = (String)ConfigLoader.convertResourceBundleToMap("config").get("bank.bills.billers");
	private static String getProducts = (String)ConfigLoader.convertResourceBundleToMap("config").get("bank.bill.products");
	private static String getProductFormItems = (String)ConfigLoader.convertResourceBundleToMap("config").get("bank.bills.productitems");

	@Autowired
	private BankBillpaymentProcessor bankBillpaymentProcessor;

	public JSONObject getNetworks() {
		try {
			JSONObject request = getNetworksRequest();
			JSONObject response = bankBillpaymentProcessor.excecuteService(request, getNetworksurl);
			return response;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} 
	}
	private JSONObject getNetworksRequest() {
		JSONObject request = new JSONObject();
		request.put("RequestId", CommonUtil.createRandomNumber(17L));
		request.put("CountryId", countryId);
		return request;
	}

	public JSONObject getBillerCategories() {
		try {
			JSONObject request = getNetworksRequest();
			JSONObject response = bankBillpaymentProcessor.excecuteService(request, getCategories);
			return response;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} 
	}
	public JSONObject getBillers(String catId) {
		try {
			JSONObject request = getNetworksRequest();
			request.put("BillerCategoryId", catId);
			JSONObject response = bankBillpaymentProcessor.excecuteService(request, getBillers);
			return response;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} 
	}
	public JSONObject getBillerPaymentItems(String billerId) {
		try {
			JSONObject request = getNetworksRequest();
			request.put("BillerId", billerId);
			JSONObject response = bankBillpaymentProcessor.excecuteService(request, getProducts);
			return response;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} 
	}
	public JSONObject getBillerFormItems(String productId) {
		try {
			JSONObject request = getNetworksRequest();
			request.put("ProductId", productId);
			JSONObject response = bankBillpaymentProcessor.excecuteService(request, getProductFormItems);
			return response;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} 
	}
}