package com.ceva.bank.services;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.beans.MMOperators;
import com.ceva.bank.common.beans.MMProductFormItems;
import com.ceva.bank.common.beans.MMProducts;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component("bankMobileService")
public class BankMobileService implements Constants{

	private static final Logger logger = Logger.getLogger(BankMobileService.class);
	
	private static final String countryId = ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId");
	private static final String operators = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.operators");
	private static final String operatorsJsonValue = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.operators.jsonvalue");
	private static final String products = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.products");
	private static final String productsJsonValue = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.products.jsonvalue");
	private static final String productFormItems = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.productsFItems");
	private static final String productFIJsonValue = ConfigLoader.convertResourceBundleToMap("config").get("bank.mobile.productsFItems.jsonvalue");
	
	public BankMobileService() {

	}

	@Autowired
	MobilePaymentProcessor mobilePaymentProcessor;
	
	public ResponseDTO loadOperators(MMOperators bean) {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = mobilePaymentProcessor.excecuteService(request, operators);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray(operatorsJsonValue)));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While loadOperators...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadMMProducts(MMProducts bean) {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest("BillerId",bean.getBillerId());
			JSONObject object = mobilePaymentProcessor.excecuteService(request, products);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray(productsJsonValue)));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While loadProducts...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}
	
	public ResponseDTO loadMMProductFormItems(MMProductFormItems bean) {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest("ProductId",bean.getProductId());
			JSONObject object = mobilePaymentProcessor.excecuteService(request, productFormItems);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray(productFIJsonValue)));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While loadProducts...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}
	
	private JSONObject prepareRequest() {
		JSONObject request = new JSONObject();
		request.put("RequestId", CommonUtil.createAlphaNumericString(18));
		request.put("CountryId", countryId);
		return request;
	}
	
	private JSONObject prepareRequest(String key,int value) {
		JSONObject request = new JSONObject();
		request.put("RequestId", CommonUtil.createAlphaNumericString(18));
		request.put("CountryId", countryId);
		request.put(key, value);
		return request;
	}
	
	private Set<JSONObject> deleteDuplicates(JSONArray array){
		Set<JSONObject> uniqueSet = new HashSet<>();
		for(int i=0;i<array.size(); i++){
			uniqueSet.add((JSONObject) array.get(i));
		}
		return uniqueSet;
	}
	
}