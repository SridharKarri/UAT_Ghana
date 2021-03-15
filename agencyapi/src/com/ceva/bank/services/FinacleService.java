package com.ceva.bank.services;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.agentbankingministatement.ArrayOfTransaction;
import org.datacontract.schemas._2004._07.agentbankingministatement.MiniStatementRequest;
import org.datacontract.schemas._2004._07.agentbankingministatement.MiniStatementResponse;
import org.datacontract.schemas._2004._07.agentbankingministatement.Transaction;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.beans.AccountBalance;
import com.ceva.bank.common.beans.GetRM;
import com.ceva.bank.common.beans.MiniStatement;
import com.ceva.bank.common.beans.MinistmtResponse;
import com.ceva.bank.common.beans.QueryAccount;
import com.ceva.crypto.tools.SHA512;
import com.ceva.dto.ResponseDTO;
import com.ceva.fbn.helper.MinistatementHelper;
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

			JSONObject object = new RequestProcessor().excecuteService(request, queryAccountUrl);

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
	
	public ResponseDTO getAccountsByPhone(String phoneNumber) {
		String queryAccountUrl = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			queryAccountUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.accountbyphone.url");
			JSONObject request = new JSONObject();
			request.put("PhoneNumber", phoneNumber);
			request.put("CountryId", countryId);
			request.put("Language", language);
			request.put("ChannelId", channelId);
			request.put("ReferenceId", System.nanoTime()+"");

			JSONObject object = new RequestProcessor().excecuteService(request, queryAccountUrl);

			if (object != null) {
				logger.debug(object);
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					Map<String, Object> data = new HashMap<>();
					data.put("accounts", object.get("AccountNumber"));
					dto.setMessage(SUCESS_SMALL);
					dto.setResponseCode(SUCCESS_RESP_CODE);
					dto.setData(data);
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
			
			JSONObject object = new RequestProcessor().excecuteService(request, queryAccountUrl);

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

			JSONObject object = new RequestProcessor().excecuteService(request, balEnqUrl);
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
	public GetRM getRMByAccount(String accountNumber) {
		String destUrl = null;
		com.ceva.bank.common.beans.GetRM rm = null;
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.getrm.url");

			JSONObject request = new JSONObject();
			request.put("AccountNumber", accountNumber);
			request.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
			request.put("ReferenceId", System.nanoTime()+"");
			/*request.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));*/
			request.put("ChannelId", ConfigLoader.convertResourceBundleToMap("config").get("bank.channelId"));

			JSONObject object = new RequestProcessor().excecuteService(request, destUrl);
			if (object != null)
				logger.debug(object);

				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					rm = new com.ceva.bank.common.beans.GetRM();
					rm.setRelationshipManagerID(object.getString("RelationshipManagerId"));
					rm.setRelationshipManagerEmail(object.getString("RelationshipManagerEmail"));
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error While fetching getRMByAccount...");
		} finally {
			destUrl = null;
		}
		return rm;
	}

	public ResponseDTO miniStatement(MiniStatement bean) {
		String url = null;
		String pkey = "";
		ResponseDTO dto = new ResponseDTO();
		List<Transaction> transactions = null;
		List<MinistmtResponse> miniTransactions = new ArrayList<MinistmtResponse>();
		MinistmtResponse miniResponse = null;
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("fin.ministatement.url");
			pkey = new SHA512().sha256(ConfigLoader.convertResourceBundleToMap("config").get("fin.ministatement.pkey"));

			MiniStatementRequest request = generateMinistatementRequest(bean);
			MinistatementHelper helper = new MinistatementHelper();
			MiniStatementResponse miniStatementResponse = helper.getMinistatement(request, url, pkey);
			org.datacontract.schemas._2004._07.agentbankingministatement.Response response = miniStatementResponse
					.getResponse().getValue();
			String respCode = response.getResponseCode().getValue();
			String respMessage = response.getResponseMessage().getValue();
			logger.debug("respCode..:" + respCode);
			logger.debug("respMessage..:" + respMessage);
			if (Constants.SUCCESS_RESP_CODE.equals(respCode) && Constants.SUCESS_SMALL.equals(respMessage)) {
				ArrayOfTransaction arrayOfTransaction = miniStatementResponse.getTransactionList().getValue();
				transactions = arrayOfTransaction.getTransaction();
				for (Transaction transaction : transactions) {
					miniResponse = new MinistmtResponse();
					miniResponse.setAmount(transaction.getAmount().getValue());
					miniResponse.setDate(transaction.getDate().getValue());
					miniResponse.setNarration(transaction.getNarration().getValue());
					miniResponse.setTransType(transaction.getTransType().getValue());
					miniResponse.setTime(transaction.getTime().getValue());
					miniTransactions.add(miniResponse);
					miniResponse = null;
				}
				dto.setData(miniTransactions);
			}
			dto.setMessage(respMessage);
			dto.setResponseCode(respCode);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(GENERIC_FAILURE_MESS);
			dto.setResponseCode(FAILURE_RESP_CODE);
		} finally {
			transactions = null;
			miniResponse = null;
			miniTransactions = null;
			pkey = null;
			url = null;
		}
		return dto;
	}

	private MiniStatementRequest generateMinistatementRequest(MiniStatement bean) {
		MiniStatementRequest request = new MiniStatementRequest();
		request.setAccountNo(new JAXBElement<String>(
				new QName("http://schemas.datacontract.org/2004/07/AgentBankingMiniStatement.Model", "AccountNo"),
				String.class, bean.getAccountNumber()));
		request.setEndDate(getDate(bean.getEndDate()));
		request.setStartDate(getDate(bean.getFromDate()));
		return request;
	}

	/*
	 * public GetRM getRMByAccount(String acNumber) { GetRM getRM = null; String
	 * destUrl = null; GetRM[] rms = null; RelationShipManagerHelper helper =
	 * null; try { destUrl =
	 * ConfigLoader.convertResourceBundleToMap("config").get("fin.getrm.url");
	 * logger.info("getRMByAccount...:" + destUrl); helper = new
	 * RelationShipManagerHelper(); rms = helper.getRelationShipManager(destUrl,
	 * acNumber); if (rms.length > 0) { for (GetRM rm : rms) { getRM = rm; } } }
	 * catch (Exception e) { e.printStackTrace(); logger.error(
	 * "Error While fetching getRMByAccount..."); } finally { helper = null;
	 * destUrl = null; rms = null; } return getRM; }
	 */

	private static XMLGregorianCalendar getDate(String inDate) {
		try {
			XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(inDate);
			System.out.println("xml date..:" + xmlCal);
			return xmlCal;
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}

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
