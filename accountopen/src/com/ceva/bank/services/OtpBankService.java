package com.ceva.bank.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
	
	private static String telcoCode=ConfigLoader.convertResourceBundleToMap("config").get("bank.countrycode");

	@Autowired
	private RequestProcessor requestProcessor;
	
	public ResponseDTO send(OtpBean bean) {
		ResponseDTO dto = new ResponseDTO();
		Long otpLength = null;
		String otp = null;
		Long referenceNumber = System.currentTimeMillis();
		String url = null;
		RestTemplate restTemplate = null;
		String message = null;
		JSONObject obj= null;
		ResponseEntity<String> response = null;
		String mobileNum = null;
		String countryCode= null;
		String sender= null;
		try {
			restTemplate = new RestTemplate();
			sender = ConfigLoader.convertResourceBundleToMap("config").get("otp.sender");
			countryCode = ConfigLoader.convertResourceBundleToMap("config").get("bank.countrycode");
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
			if(bean.getMobileNumber().length() == 11){
				mobileNum = bean.getMobileNumber().replaceFirst("0", countryCode);
			}
			if(bean.getMobileNumber().length() == 10){
				mobileNum = countryCode+bean.getMobileNumber();
			}
			if(bean.getMobileNumber().length() == 13){
				mobileNum = bean.getMobileNumber();
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
					.queryParam("sender", sender)
			        .queryParam("phone", mobileNum)
			        .queryParam("text", message);

			HttpEntity<?> entity = new HttpEntity<>(headers);
			try{
				log.info("before sending ..:"+builder.build().encode().toUri());
				response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
			}catch(Exception e){
				log.error("Error while calling sms service.");
				log.error("Reason..:"+e.getLocalizedMessage());
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}

			log.info("Response Status..:"+response.getStatusCode());
			if (response.getStatusCode() == HttpStatus.OK) {
				obj = JSONObject.fromObject(response.getBody());
				dto.setMessage(Constants.SUCESS_SMALL);
				dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
				dto.setData(bean);
			} else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				log.debug("Bad Request..:"+response.getStatusCode());
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
			log.info("otp Response from bank..:"+response);

		} catch (Exception e) {
			log.error("Error Occured..:"+e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}finally{
			otpLength = null;
			otp = null;
			referenceNumber = null;
			url = null;
			restTemplate = null;
			message = null;
			obj= null;
			countryCode = null;
			sender = null;
		}
		return dto;
	}
	
	public boolean sendSMS(Alert alert) {
		RestTemplate restTemplate = new RestTemplate();
		boolean isSent=false;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String mobile = alert.getMobile();
		if(mobile.length()==10){
			mobile=telcoCode+mobile;
		}else if(mobile.startsWith("0")){
			
		}
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(ConfigLoader.convertResourceBundleToMap("config").get("bank.otp.url"))
				.queryParam("sender", ConfigLoader.convertResourceBundleToMap("config").get("otp.sender"))
				.queryParam("phone", mobile).queryParam("text", alert.getMessage());

		HttpEntity<?> entity = new HttpEntity<>(headers);
		try {
			log.info("before sending ..:" + builder.build().encode().toUri());
			ResponseEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST,
					entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				JSONObject obj = JSONObject.fromObject(response.getBody());
				isSent=true;
			} else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				log.debug("Bad Request..:"+response.getStatusCode());
				isSent=false;
			}
		} catch (Exception e) {
			log.error("Error while calling sms service.");
			log.error("Reason..:" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return isSent;
	}

	/*public ResponseDTO generateReport(TransactionReport report) {
		ResponseDTO dto =new ResponseDTO();
		log.info("ReportType..:"+report.getReportType());
		String dest= null;
		try{
			dest = ConfigLoader.convertResourceBundleToMap("config").get("txn.report");
			log.info("url..." + dest);
			JSONObject  object =requestProcessor.excecuteService(report, dest);
			log.info("generateReport Response..." + object);
			if (object != null) {
				log.info("generateReport ..." + object.getString(Constants.KEY_RESP_CODE));
				log.info("generateReport message..." + object.getString(Constants.KEY_MESSAGE));
				if(Constants.SUCCESS_RESP_CODE.equals(object.getString(Constants.KEY_RESP_CODE))){
					dto.setMessage(object.getString(Constants.KEY_MESSAGE));
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setData(object.get(Constants.KEY_DATA));
				}else{
					log.info("generateReport..failed." + object.getString(Constants.KEY_RESP_CODE));
					dto.setMessage(object.getString(Constants.KEY_MESSAGE));
					dto.setResponseCode(object.getString(Constants.KEY_RESP_CODE));
				}
			} else {
				dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				dto.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
			log.info("generateReport...Response..:" + dto.toString());
		}catch(Exception e){
			log.error("error While calliing generateReport api.");
			log.info("error reason.:"+e.getLocalizedMessage());
			e.printStackTrace();
			dto.setMessage(Constants.GENERIC_FAILURE_MESS);
			dto.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		log.info(dto.toString());
		return  dto;
	}*/

}
