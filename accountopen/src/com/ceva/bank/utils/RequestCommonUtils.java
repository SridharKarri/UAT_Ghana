package com.ceva.bank.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class RequestCommonUtils {
	static Logger logger = Logger.getLogger(RequestCommonUtils.class);

	public static String getConvertedFormat(String inPattern) {
		SimpleDateFormat informat = null;
		StringBuffer year = null;
		try {
			year = new StringBuffer(15);
			informat = new SimpleDateFormat(inPattern);
			year.append(informat.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		} finally {

		}

		return year.toString();
	}
	public static String getRandomToDate(String input) {

		String currentMill = null;
		try {

			currentMill = String.valueOf(new Random().nextLong());
			input += currentMill.substring(currentMill.length() - 4,
					currentMill.length());
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		} finally {

		}

		return input;
	}
	public static String responseBuilder(final List<String> message) {
		String resp = null;
		JSONObject jsonResp = null;
		try {
			logger.info(" Inside responseBuilder.....");
			if (message.size() > 1) {

				jsonResp = new JSONObject();
				jsonResp.put("responsecode", message.get(0));
				jsonResp.put("responsemessage", message.get(1));
				jsonResp.put("referenceid", message.get(2));

				resp = jsonResp.toString();

			} else {
				resp = message.get(0);
			}
			logger.info(" Response Builded Successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("The exception while parsing is ...", e);
		} finally {

			if (jsonResp != null) {
				jsonResp.clear();
				jsonResp = null;
			}
		}

		return resp;
	}
}
