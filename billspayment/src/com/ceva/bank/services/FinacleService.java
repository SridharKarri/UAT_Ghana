package com.ceva.bank.services;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.beans.AccountBalance;
import com.ceva.bank.common.beans.QueryAccount;
import com.ceva.dto.ResponseDTO;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

@Component("finacleService")
public class FinacleService implements Constants {
	private static Logger logger = Logger.getLogger(FinacleService.class);

	private static final String countryId= ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId");
	private static final String channelId= ConfigLoader.convertResourceBundleToMap("config").get("bank.channelId");
	private static final String language= ConfigLoader.convertResourceBundleToMap("config").get("bank.language");
	public FinacleService() {

	}

	public ResponseDTO getPhonesByAccount(String accountNumber) {
		String queryAccountUrl = null;
		ResponseDTO dto = new ResponseDTO();
		QueryAccount accountBean = null;
		try {
			queryAccountUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.phonebyaccount.url");
			JSONObject request = new JSONObject();
			request.put("AccountNumber", accountNumber);
			request.put("CountryId", countryId);
			request.put("Language", language);
			request.put("ChannelId", channelId);
			request.put("ReferenceId", System.nanoTime()+"");

			JSONObject object = new RequestProcessor().excecuteService(request, queryAccountUrl,new HashMap<String, String>());

			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					accountBean = new QueryAccount();
					accountBean.setPhone(object.getString("PhoneNumber"));
					dto.setMessage(SUCESS_SMALL);
					dto.setResponseCode(SUCCESS_RESP_CODE);
					dto.setData(accountBean);
				}else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}

			} else {
				dto.setMessage(GENERIC_FAILURE_MESS);
				dto.setResponseCode(GENERIC_FAILURE_MESS);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		} finally {
			queryAccountUrl = null;
		}
		return dto;
	}
	
	public ResponseDTO queryAccount(String accountNumber) {
		String queryAccountUrl = null;
		ResponseDTO dto = new ResponseDTO();
		QueryAccount accountBean = null;
		try {
			queryAccountUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.queryaccount.url");
			JSONObject request = new JSONObject();
			request.put("AccountNumber", accountNumber);
			request.put("CountryId", countryId);
			request.put("Language", language);
			request.put("ChannelId", channelId);
			request.put("ReferenceId", System.nanoTime()+"");
			
			JSONObject object = new RequestProcessor().excecuteService(request, queryAccountUrl,new HashMap<String, String>());

			if (object != null) {
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					accountBean = new QueryAccount();
					accountBean.setAccountName(object.getString("AccountName"));
					accountBean.setAccountNumber(object.getString("AccountNumber"));
					accountBean.setAcctCurrCode(object.getString("CurrencyCode"));
					accountBean.setBranchCode(object.getString("BranchCode"));
					accountBean.setEmail(object.getString("Email"));
					accountBean.setSchemeType(object.getString("SchemeType"));
					accountBean.setSubProductCode(object.getString("SubProductCode"));
					accountBean.setRelationshipMgr(object.getString("RelationshipMgrId"));

					logger.debug(accountBean.toString());

					dto.setMessage(SUCESS_SMALL);
					dto.setResponseCode(SUCCESS_RESP_CODE);
					dto.setData(accountBean);
				}else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}

			} else {
				dto.setMessage(GENERIC_FAILURE_MESS);
				dto.setResponseCode(GENERIC_FAILURE_MESS);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		} finally {
			queryAccountUrl = null;
		}
		return dto;
	}

	public AccountBalance balanceEnquirey(String accountNumber) {
		AccountBalance accountBalance = new AccountBalance();
		String balEnqUrl = null;
		try {
			balEnqUrl = ConfigLoader.convertResourceBundleToMap("config").get("balance.enquirey");

			JSONObject request = prepareBalanceEnquiry(accountNumber);

			JSONObject object = new RequestProcessor().excecuteService(request, balEnqUrl, new HashMap<String, String>());
			if (object != null)
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					accountBalance.setBalance(new BigDecimal(object.getString("AvailableBalance")));
					accountBalance.setBalance(new BigDecimal(object.getString("BookBalance")));
					accountBalance.setCurrencyCode(object.getString("CurrencyCode"));
				}

			logger.info(accountBalance.toString());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Reason..:" + e.getLocalizedMessage());
			logger.error("Error While fetching account balance...");
		} finally {
			balEnqUrl = null;
		}
		return accountBalance;
	}
	private JSONObject prepareBalanceEnquiry(String accountNumber){
		JSONObject request = new JSONObject();
		request.put("AccountNumber", accountNumber);
		request.put("CountryId", countryId);
		request.put("Language", language);
		request.put("ChannelId", channelId);
		request.put("ReferenceId", System.nanoTime()+"");
		/*request.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));*/
		return request;
	}

	public static void main(String[] args) {
		/*
		 * String key =new
		 * SHA512().sha256("972853E8-85C6-4964-9AAF-A8D03478B74A");
		 * System.out.println("key...:"+key);
		 */

		try {
			/*
			 * Date date = new SimpleDateFormat("yyyyMMdd").parse("20170911");
			 * new SimpleDateFormat("dd-MM-yyyy").format(date);
			 */
			System.out.println(
					new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse("20170911")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getAcOpenDate(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}
}
