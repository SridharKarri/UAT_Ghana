package com.ceva.bank.services;

import org.apache.log4j.Logger;

import com.ceva.dto.ResponseDTO;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

public class TransferService {
	Logger logger = Logger.getLogger(TransferService.class);

	public ResponseDTO nameEnquiry(JSONObject enquiry) {
		logger.debug("enquiry...." + enquiry.toString());
		String destUrl = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.nameenquiry.url");
			logger.debug("url..:" + destUrl);
			dto = callService(enquiry, destUrl);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			logger.error("error occured..:" + e.getLocalizedMessage());
		} finally {
			destUrl = null;
		}
		logger.debug("Response Constructed..:" + dto.toString());
		return dto;
	}
	
	public ResponseDTO nameEnquireyOtherBank(JSONObject enquiry) {
		logger.debug("enquiry otherbank...." + enquiry.toString());
		String destUrl = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.nameenquiryotherbank.url");
			logger.debug("url..:" + destUrl);
			dto = callService(enquiry, destUrl);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			logger.error("error occured..:" + e.getLocalizedMessage());
		} finally {
			destUrl = null;
		}
		logger.debug("Response Constructed..:" + dto.toString());
		return dto;
	}

	public ResponseDTO deposit(JSONObject deposit) {
		logger.debug("deposit...." + deposit.toString());
		String destUrl = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.deposit.url");
			logger.debug("url..:" + destUrl);
			dto = callService(deposit, destUrl);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		} finally {
			destUrl = null;
		}
		logger.debug("Response Constructed..:" + dto.toString());
		return dto;
	}
	public ResponseDTO withdraw(JSONObject deposit) {
		logger.debug("withdraw...." + deposit.toString());
		String destUrl = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.withdraw.url");
			logger.debug("url..:" + destUrl);
			dto = callService(deposit, destUrl);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		} finally {
			destUrl = null;
		}
		logger.debug("Response Constructed..:" + dto.toString());
		return dto;
	}
	
	public ResponseDTO interbankTransfer(JSONObject deposit) {
		logger.debug("deposit...." + deposit.toString());
		String destUrl = null;
		ResponseDTO dto = new ResponseDTO();
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.interbank.url");
			logger.debug("url..:" + destUrl);
			dto = callService(deposit, destUrl);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		} finally {
			destUrl = null;
		}
		logger.debug("Response Constructed..:" + dto.toString());
		return dto;
	}
	
	public ResponseDTO getBanks(JSONObject deposit) {
		logger.debug("getBanks...." + deposit.toString());
		String destUrl = null;
		ResponseDTO dto = null;
		try {
			destUrl = ConfigLoader.convertResourceBundleToMap("config").get("fin.banks.url");
			logger.debug("url..:" + destUrl);
			dto = callService(deposit, destUrl);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto = new ResponseDTO(Constants.GENERIC_FAILURE_MESS,Constants.FAILURE_RESP_CODE);
		} finally {
			destUrl = null;
		}
		logger.debug("Response Constructed..:" + dto.toString());
		return dto;
	}
	
	private ResponseDTO callService(JSONObject request, String url){
		ResponseDTO dto = new ResponseDTO();
		try{
			JSONObject object = new RequestProcessor().excecuteService(request, url);
			if (object != null) {
				logger.info("response..:" + object);
				if (object.getString(Constants.RESPONSE_CODE_KEY).equals(Constants.SUCCESS_RESP_CODE)) {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(object);
				} else {
					dto.setMessage(object.getString("ResponseMessage"));
					dto.setResponseCode(object.getString("ResponseCode"));
				}

			} else {
				logger.debug("failure response..in account query.");
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("error occured..:" + e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		return dto;
	}

}
