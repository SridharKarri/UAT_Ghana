package com.ceva.bank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.beans.AccountBean;
import com.ceva.bank.common.dao.AcctOpenMasterDao;
import com.ceva.db.model.AccountOpeningMaster;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component("bankAccountService")
public class BankAccountService implements Constants {

	private static final Logger logger = Logger.getLogger(BankAccountService.class);

	private static final String countryId = ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId");
	private static final String branches = ConfigLoader.convertResourceBundleToMap("config").get("bank.branches");
	private static final String countries = ConfigLoader.convertResourceBundleToMap("config").get("bank.countries");
	private static final String states = ConfigLoader.convertResourceBundleToMap("config").get("bank.states");
	private static final String cities = ConfigLoader.convertResourceBundleToMap("config").get("bank.cities");
	private static final String idcategories = ConfigLoader.convertResourceBundleToMap("config").get("bank.idcategories");
	private static final String maritalstatus = ConfigLoader.convertResourceBundleToMap("config").get("bank.maritalstatus");
	private static final String occupations = ConfigLoader.convertResourceBundleToMap("config").get("bank.occupations");
	private static final String salutations = ConfigLoader.convertResourceBundleToMap("config").get("bank.salutations");
	private static final String nationalities = ConfigLoader.convertResourceBundleToMap("config").get("bank.nationalities");
	private static final String idtypebycat = ConfigLoader.convertResourceBundleToMap("config").get("bank.idtypebycat");
	private static final String openaccount = ConfigLoader.convertResourceBundleToMap("config").get("fin.openaccount.url");
	private static final String statesJsonValue = ConfigLoader.convertResourceBundleToMap("config").get("bank.states.jsonvalue");
	private static final String idcategoriesJsonValue = ConfigLoader.convertResourceBundleToMap("config").get("bank.categories.jsonvalue");
	private static final String manualReqFlag = ConfigLoader.convertResourceBundleToMap("config").get("manual.req.flag");
	
	public BankAccountService() {

	}

	@Autowired
	AccountProcessor accountProcessor;
	
	@Autowired
	AcctOpenMasterDao acctOpenMasterDao;

	public ResponseDTO openAccount(AccountBean bean) {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = createCustomerRequest(bean);
			JSONObject response = accountProcessor.excecuteService(request, openaccount);
			if (response != null) {
				if(response.containsKey("ResponseCode")){
					if (Constants.SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))) {
						Map<String, String> data = new HashMap<>();
						if (response.containsKey("AccountNumber"))
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
				}else{
					if(response.containsKey("Message")){
						if(response.containsKey("ModelState")){
							dto.setMessage(response.getString("ModelState"));
							dto.setResponseCode(FAILURE_RESP_CODE);
						}
					}else{
						dto.setMessage(GENERIC_FAILURE_MESS);
						dto.setResponseCode(FAILURE_RESP_CODE);
					}
				}
			} else {
				dto.setMessage(GENERIC_FAILURE_MESS);
				dto.setResponseCode(FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("error occured while opening account..:" + e.getLocalizedMessage());
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		} finally {
		}
		return dto;
	}

	private JSONObject createCustomerRequest(AccountBean bean) {
		JSONObject request = new JSONObject();
		request.put("RequestId", CommonUtil.createAlphaNumericString(18));
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
		request.put("MaritalStatus", bean.getMaritalStatus());
		request.put("BranchCode", bean.getSolId());
		request.put("AccountType", "SAVINGS");
		request.put("Occupation", bean.getOccupation());
		request.put("Nationality", bean.getNationality());
		request.put("IdNumber", bean.getIdNumber());
		request.put("IdType", bean.getIdType());
		request.put("IdImage", bean.getIdImage());
		request.put("Signature", bean.getMandateCard());
		//request.put("PassportPhoto", bean.getMandateCard());
		//request.put("CurrenyCode", bean.getCurrency());
		//request.put("PassportPhoto", bean.getMandateCard());
		return request;
	}

	private JSONObject prepareRequest() {
		JSONObject request = new JSONObject();
		request.put("RequestId", CommonUtil.createAlphaNumericString(18));
		request.put("CountryId", countryId);
		return request;
	}

	public ResponseDTO loadBranches() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, branches);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Branches")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load Branches...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadCountries() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, countries);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Countries")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Reason..:" + e.getLocalizedMessage());
			logger.error("Error While load Countries...");
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadStates() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, states);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray(statesJsonValue)));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load States...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadCities() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, cities);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Cities")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load cities...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadIdentityCategories() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, idcategories);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray(idcategoriesJsonValue)));
					//dto.setData(removeIfValueIsnull(object.getJSONArray("Categories")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load idcategories...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadMaritalStasuses() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, maritalstatus);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("MaritalStatuses")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load MaritalStasuses...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadOccupations() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, occupations);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Occupations")));
				} else {
					if(manualReqFlag.equals("Y")){
						AccountOpeningMaster accountOpeningMaster = acctOpenMasterDao.getJsonDataforService("Occupations");
						JSONObject json = JSONObject.fromObject(accountOpeningMaster.getJsonData());
						dto.setData(json.get("Occupations"));
						dto.setMessage(Constants.SUCESS_SMALL);
						dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					}else{
						dto.setMessage(object.getString("ResponseMessage"));
						dto.setResponseCode(object.getString("ResponseCode"));	
					}
				}
			} else {
				if(manualReqFlag.equals("Y")){
					AccountOpeningMaster accountOpeningMaster = acctOpenMasterDao.getJsonDataforService("Occupations");
					JSONObject json = JSONObject.fromObject(accountOpeningMaster.getJsonData());
					dto.setData(json.get("Occupations"));
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}else{
					dto.setMessage(Constants.GENERIC_FAILURE_MESS);
					dto.setResponseCode(Constants.FAILURE_RESP_CODE);
				}	
			}	
			
		} catch (Exception e) {
			logger.error("Error While load Occupations...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadSalutaions() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, salutations);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Salutations")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load Salutaions...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadNationalities() {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			JSONObject object = accountProcessor.excecuteService(request, nationalities);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Nationalities")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load nationalities...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

	public ResponseDTO loadIdentityTypeByCategories(String catId) {
		ResponseDTO dto = new ResponseDTO();
		try {
			JSONObject request = prepareRequest();
			request.put("CategoryId", catId);
			JSONObject object = accountProcessor.excecuteService(request, idtypebycat);
			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(Constants.SUCESS_SMALL);
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(deleteDuplicates(object.getJSONArray("Types")));
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
		} catch (Exception e) {
			logger.error("Error While load loadIdentity TypeByCategories...");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
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
