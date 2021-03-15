package com.ceva.bank.services;

import com.ceva.bank.common.dao.OtpDao;
import com.ceva.db.model.Alert;
import com.nbk.util.ConfigLoader;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service("otpBankService")
public class OtpBankService
{
	Logger log = Logger.getLogger(OtpBankService.class);

	@Autowired
	OtpDao otpDao;

	@Autowired
	RequestProcessor requestProcessor;

	private JSONObject constructRequestJSON(Alert bean, String message) {
		JSONObject object = new JSONObject();
		object.put("ChannelId", "agent");
		object.put("CountryId", ConfigLoader.convertResourceBundleToMap("config").get("bank.countryId"));
		object.put("Language", ConfigLoader.convertResourceBundleToMap("config").get("bank.language"));
		object.put("Message", message);
		object.put("MobileNumber", bean.getMobile());
		object.put("ReferenceId", bean.getId());
		return object;
	}

	public Alert postSMS(Alert bean) {
		String url = null;
		JSONObject obj = null;
		try {
			url = (String)ConfigLoader.convertResourceBundleToMap("config").get("bank.otp.url");
			JSONObject request = constructRequestJSON(bean, bean.getMessage());
			log.debug("request ..:" + request);
			obj = requestProcessor.excecuteService(request, url);
			log.debug("response ..:" + obj);

			if ("00".equals(obj.getString("ResponseCode"))) {
				log.debug("success response from messaging api.");
				bean.setFetchStatus("S");
				bean.setDeliveryStatus("");
			} else {
				log.debug("failure response from messaging api." + obj.getString("ResponseCode"));
				bean.setDeliveryStatus("Failed");
				bean.setRetryCount(bean.getRetryCount() + 1);
				bean.setDeliveryStatus(obj.getString("ResponseCode"));
				if (bean.getRetryCount() <= 2) {
					bean.setFetchStatus("P");
				} else {
					bean.setFetchStatus("F");
				}
			}
		} catch (Exception e) {
			log.error("error While posting sms.");
			log.error("reason..:" + e.getLocalizedMessage());

			bean.setDeliveryStatus("Failed");
			bean.setRetryCount(bean.getRetryCount() + 1);
			if (bean.getRetryCount() <= 2) {
				bean.setFetchStatus("P");
			} else {
				bean.setFetchStatus("F");
			} 
		} finally {

			url = null;
		} 
		return bean;
	}
}