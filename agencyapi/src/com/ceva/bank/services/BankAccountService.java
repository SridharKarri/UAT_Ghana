package com.ceva.bank.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.beans.AccountBean;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component("bankAccountService")
public class BankAccountService implements Constants {
	
	private static final Logger logger = Logger.getLogger(BankAccountService.class);

	private static final String countryId= ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId");
	
	public BankAccountService() {

	}
	
	@Autowired
	AccountProcessor accountProcessor;

	public ResponseDTO openAccount(AccountBean bean) {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request =  createCustomerRequest(bean);
			url = ConfigLoader.convertResourceBundleToMap("config").get("fin.openaccount.url");
			JSONObject response =  accountProcessor.excecuteService(request, url);
			if (response != null) {
				if (Constants.SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))) {
					Map<String, String> data = new HashMap<>();
					if(response.containsKey("AccountNumber"))
						data.put("accountNumber", response.getString("AccountNumber"));
					else
						data.put("accountNumber", "No account returned.");
					dto.setMessage(SUCESS_SMALL);
					dto.setResponseCode(SUCCESS_RESP_CODE);
					dto.setData(data);
				} else {
					dto.setMessage(response.getString("ResponseMessage"));
					dto.setResponseCode(response.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(GENERIC_FAILURE_MESS);
				dto.setResponseCode(FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		} finally {
			url = null;
		}
		return dto;
	}

	private JSONObject createCustomerRequest(AccountBean bean) {
		JSONObject request =  new JSONObject();
		request.put("RequestId",CommonUtil.createAlphaNumericString(18));
		request.put("Salutation", bean.getSalutation());
		request.put("FirstName", bean.getFirstName());
		request.put("LastName", bean.getLastName());
		request.put("MiddleName", bean.getMidName());
		request.put("MobileNumber", bean.getPhone());
		request.put("Email", bean.getEmail());
		request.put("DateOfBirth", bean.getDob());
		request.put("Gender", bean.getGender());
		request.put("HouseNumber", bean.getHouseNumber());
		request.put("StreetName", bean.getStreetName());
		request.put("Address", bean.getAddress());
		request.put("Country", bean.getCountry());
		request.put("CountryId", countryId);
		request.put("State", bean.getState());
		request.put("City", bean.getCity());
		//request.put("MandateCard", bean.getMandateCard());
		//request.put("PassportPhoto", bean.getMandateCard());
		request.put("Signature", bean.getMandateCard());
		request.put("MaritalStatus", bean.getMaritalStatus());
		request.put("BranchCode", bean.getSolId());
		request.put("AccountType", "SAVINGS");
		request.put("Occupation", bean.getOccupation());
		request.put("Nationality", bean.getNationality());
		request.put("IdNumber", bean.getIdNumber());
		request.put("IdType", bean.getIdType());
		request.put("IdImage", bean.getIdImage());
		return request;
	}

	private JSONObject prepareRequest() {
		JSONObject request = new JSONObject();
		request.put("RequestId", CommonUtil.createAlphaNumericString(18));
		request.put("CountryId", countryId);
		return request;
	}

	public ResponseDTO loadBranches() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.branches");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Branches")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load Branches...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadCountries() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.countries");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Countries")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Reason..:" + e.getLocalizedMessage());
			logger.error("Error While load Countries...");
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadStates() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.states");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("States")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load States...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadCities() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.cities");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Cities")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load States...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadIdentityCategories() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.idcategories");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					//dto.setData(deleteDuplicates(object.getJSONArray("Categories")));
					dto.setData(deleteDuplicates(object.getJSONArray("Types")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load States...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadMaritalStasuses() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.maritalstatus");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("MaritalStatuses")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load MaritalStasuses...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadOccupations() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.occupations");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Occupations")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load Occupations...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadSalutaions() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.salutations");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Salutations")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load Salutaions...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}
	
	public ResponseDTO loadNationalities() {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.nationalities");
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Nationalities")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Reason..:" + e.getLocalizedMessage());
			logger.error("Error While load Countries...");
		} finally {
			url = null;
		}
		return dto;
	}

	public ResponseDTO loadIdentityTypeByCategories(String catId) {
		String url = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.idtypebycat");
			JSONObject request = prepareRequest();
			request.put("CategoryId", catId);
			JSONObject object = accountProcessor.excecuteService(request, url);
			if (object != null){
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Types")));
				}else{
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			}else{
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load Salutaions...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
			url = null;
		}
		return dto;
	}
	
	private Set<JSONObject> deleteDuplicates(JSONArray array){
		Set<JSONObject> uniqueSet = new HashSet<>();
		for(int i=0;i<array.size(); i++){
			uniqueSet.add((JSONObject) array.get(i));
		}
		return uniqueSet;
	}
	
}
