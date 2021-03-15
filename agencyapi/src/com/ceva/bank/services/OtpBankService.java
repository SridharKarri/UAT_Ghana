package com.ceva.bank.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.OtpBean;
import com.ceva.db.model.Alert;
import com.ceva.dto.ResponseDTO;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

@Service("otpBankService")
public class OtpBankService {

	Logger log = Logger.getLogger(OtpBankService.class);

	@Autowired
	RequestProcessor requestProcessor;
	
	public ResponseDTO send(OtpBean bean) {
		ResponseDTO dto = new ResponseDTO();
		Long otpLength = null;
		String otp = null;
		Long referenceNumber = System.nanoTime();
		String url = null;
		String message = null;
		JSONObject obj= null;
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.otp.url");
			otpLength = new Long(ConfigLoader.convertResourceBundleToMap("config").get("bank.otp.digits"));
			
			if("REGOTP".equals(bean.getTxncode())){
				message = ConfigLoader.convertResourceBundleToMap("config").get("bank.regotp.message");
			}else if("ACTOTP".equals(bean.getTxncode())){
				message = ConfigLoader.convertResourceBundleToMap("config").get("bank.actotp.message");
			}else{
				message = ConfigLoader.convertResourceBundleToMap("config").get("bank.otp.message");
			}
			log.debug("otp Request To 3rd Party service otp length .:"+otpLength);
			otp=CommonUtil.createRandomNumber(otpLength);
			message = message.replace("$otp$", otp);
			bean.setOtp(otp);
			bean.setRefNumber(referenceNumber+"");
			
			String sendOTPReq=ConfigLoader.convertResourceBundleToMap("config").get("bank.sendsms.required");
			JSONObject request = constructRequestJSON(bean, message);
			log.debug("request ..:"+request);
			
			if(sendOTPReq.equals("N")){
				obj = new JSONObject();
				obj.put("ResponseCode", Constants.SUCCESS_RESP_CODE);
				log.debug("Message Not send:"+obj);
			}else{
				obj = requestProcessor.excecuteService(request, url);
				log.debug("response ..:"+obj);
			}
			
			if (Constants.SUCCESS_RESP_CODE.equals(obj.getString("ResponseCode"))) {
				dto.setMessage(Constants.SUCESS_SMALL);
				dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
				dto.setData(bean);
			} else{
				dto.setMessage(obj.getString("ResponseMessage"));
				dto.setResponseCode(obj.getString("ResponseCode"));
			}
		} catch (Exception e) {
			log.error("Error Occured..:"+e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}finally{
			otpLength = null;
			otp = null;
			referenceNumber = null;
			url = null;
			message = null;
			obj= null;
		}
		return dto;
	}
	
	public Alert sendTxnSms(Alert alert, String accountNumber) {
		ResponseDTO dto = new ResponseDTO();
		String referenceNumber = CommonUtil.createRandomNumber(16);
		String url = null;
		JSONObject obj= null;
		try {
			url = ConfigLoader.convertResourceBundleToMap("config").get("bank.otp.url");
			alert.setId(referenceNumber);
			JSONObject request = constructRequestJSON(alert, accountNumber);
			log.debug("request ..:"+request);
			obj = requestProcessor.excecuteService(request, url);
			log.debug("response ..:"+obj);
		
			if (Constants.SUCCESS_RESP_CODE.equals(obj.getString("ResponseCode"))) {
				alert.setDeliveryStatus("Success");
				alert.setFetchStatus("S");
			} else{
				alert.setDeliveryStatus("Failed");
				alert.setRetryCount(alert.getRetryCount()+1);
				if(alert.getRetryCount()<=2){
					alert.setFetchStatus("P");
				}else{
					alert.setFetchStatus("F");
				}
			}
		} catch (Exception e) {
			log.error("Error Occured..:"+e.getLocalizedMessage());
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}finally{
			referenceNumber = null;
			url = null;
			obj= null;
		}
		return alert;
	}
	
	private JSONObject constructRequestJSON(OtpBean bean, String message){
		JSONObject object = new JSONObject();
		object.put("ChannelId", "agent");
		object.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
		object.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));
		object.put("Message", message);
		String countryCode = ConfigLoader.convertResourceBundleToMap("config").get("bank.countrycode");//added 
		if(!bean.getMobileNumber().startsWith(countryCode))
			object.put("MobileNumber", countryCode+bean.getMobileNumber());
		else
			object.put("MobileNumber", bean.getMobileNumber());
		object.put("ReferenceId", bean.getRefNumber());
		object.put("AccountNumber", bean.getAccountNumber());
		return object;
	}
	
	private JSONObject constructRequestJSON(Alert bean, String accountNumber){
		JSONObject object = new JSONObject();
		object.put("ChannelId", "agent");
		object.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
		object.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));
		object.put("Message", bean.getMessage());
		object.put("MobileNumber", bean.getMobile());
		object.put("ReferenceId", bean.getId());
		object.put("AccountNumber", accountNumber);
		return object;
	}

}
