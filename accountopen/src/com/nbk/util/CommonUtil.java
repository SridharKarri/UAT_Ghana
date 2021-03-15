package com.nbk.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.ceva.crypto.tools.EncryptTransactionPin;
import com.ceva.db.model.ChannelUsers;
import com.ceva.db.model.Fee;
import com.ceva.db.model.Incentive;
import com.ceva.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class CommonUtil {

	public static Logger logger = Logger.getLogger(CommonUtil.class);
	public static String outputString = null;

	/** Method For Return 6 Digits Random Value **/
	public static String getRandomInteger() {
		int aStart = 100000;
		int aEnd = 999999;
		Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * aRandom.nextDouble());
		Long randomNumber = (Long) (fraction + aStart);
		return randomNumber.toString();
	}

	public static String b64_sha256(String inputString) {
		if (inputString != null) {
			outputString = Base64.encodeBase64String(
					DigestUtils.sha256(inputString)).trim();
			//logger.debug("b64_sha256 outputString...:" + outputString);
		} else {
			logger.debug("Input String Missing for b64_sha256");
		}
		outputString = outputString.substring(0, outputString.length() - 1);
		return outputString;

	}
	public static List<ChannelUsers> getAppChannelUsers(List<ChannelUsers> channelUsers){
		List<ChannelUsers> appChannelUsers=new ArrayList<>();
		for(ChannelUsers channelUser: channelUsers){
			if("APP".equals(channelUser.getCannelId())& channelUser.getImeiNo() != null){
				appChannelUsers.add(channelUser);
			}
		}
		return appChannelUsers;
	}

	public  static String maskCardNumber(String cardNumber, String mask) {

	    // format the number
	    int index = 0;
	    StringBuilder maskedNumber = new StringBuilder();
	    for (int i = 0; i < mask.length(); i++) {
	        char c = mask.charAt(i);
	        if (c == '#') {
	            maskedNumber.append(cardNumber.charAt(index));
	            index++;
	        } else if (c == 'x') {
	            maskedNumber.append(c);
	            index++;
	        } else {
	            maskedNumber.append(c);
	        }
	    }

	    // return the masked number
	    return maskedNumber.toString();
	}
	public  static String maskAccountNumber(String accountNumber, String mask) {

		final Pattern hidePattern = Pattern.compile("(.{4})(.*)(.{3})");
		Matcher m = hidePattern.matcher(accountNumber);
		String output=null;

		if(m.find()) {
		 int l = m.group(2).length();
		  output = m.group(1) + new String(new char[l]).replace("\0", "*") + m.group(3);
		}
	    return output.toString();
	}

	public static void main(String args[]) throws UnsupportedEncodingException {
		String output = b64_sha256("12344");
		System.out.println(output);
		String hex = toHex(output);
		System.out.println(hex);
		
		System.out.println(convertDateToStirng(new Date(), "yyyy-MM-dd hh:mm:ss"));
		// System.out.println(generatePassword());
		//System.out.println(RandomStringUtils.randomNumeric(6));

	}

	public static Map<String, String> getErrorsInASaneFormat(
			final BindingResult result) {
		Map<String, String> errors = new HashMap<String, String>();
		for (FieldError error : result.getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return errors;

	}

	public static String logSoapRequest(Class<?> t, Object obj) {
		String xml="";
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(t);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(obj, sw);
			xml = sw.toString();
			//logger.debug("Request body:" + xml);
		} catch (Exception e) {
			logger.error("Error While Constructing xml..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return xml;
	}
	public final static String createRandomNumber(long len) {
	    if (len > 18)
	        throw new IllegalStateException("To many digits");
	    long tLen = (long) Math.pow(10, len - 1) * 9;

	    long number = (long) (Math.random() * tLen) + (long) Math.pow(10, len - 1) * 1;

	    String tVal = number + "";
	    if (tVal.length() != len) {
	        throw new IllegalStateException("The random number '" + tVal + "' is not '" + len + "' digits");
	    }
	    return tVal;
	}
	
	public static final String createAlphaNumericString(int n) 
    { 
        int lowerLimit = 97; 
        int upperLimit = 122; 
        Random random = new Random(); 
        StringBuffer r = new StringBuffer(n); 
        for (int i = 0; i < n; i++) { 
            int nextRandomChar = lowerLimit + (int)(random.nextFloat()  * (upperLimit - lowerLimit + 1)); 
            r.append((char)nextRandomChar); 
        } 
        return r.toString(); 
    }

	public static String convertToXml(Object object) {
		logger.info("convertToXml START");
		String requestBody =null;
		JAXBContext jaxbContext = null;
		Marshaller jaxbMarshaller = null;
		StringWriter sw = null;
		try {
			logger.debug("creating jaxb START");
			jaxbContext = JAXBContext.newInstance(object.getClass());
			jaxbMarshaller = jaxbContext.createMarshaller();
			sw = new StringWriter();
			jaxbMarshaller.marshal(object, sw);
			requestBody = sw.toString();
			logger.info("converted xml:"+requestBody);
		} catch (Exception e) {
			logger.error("Error while constructing xml.:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			jaxbContext = null;
			jaxbMarshaller = null;
			sw = null;
		}
		return requestBody;
	}

	public static String toHex(String arg) throws UnsupportedEncodingException {
	    return String.format("%040x", new BigInteger(1, arg.getBytes("UTF-8")));
	}

	public static String toString(String hex) throws UnsupportedEncodingException, DecoderException{
		return new String(Hex.decodeHex(hex.toCharArray()),"UTF-8");
	}

	public static String generateHashString(String data)
	{
		 try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);
			md.update(dataInBytes);

			byte[] mdbytes = md.digest();

			StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<mdbytes.length;i++) {
	    	  hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
	    	}

	    	return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 public JSONObject getJsonObject(Object obj){

		  ObjectMapper mapper = new ObjectMapper();
		  JSONObject jsonObject= null;
		  try {
		   String json = mapper.writeValueAsString(obj);
		   jsonObject = JSONObject.fromObject(json);
		  } catch (JSONException | IOException e) {
		   e.printStackTrace();
		  }
		  return jsonObject;
		 }
	
	public ResponseDTO generateFailureMessage(String responseCode, String responseMessage){
		ResponseDTO dto= new ResponseDTO();
		dto.setResponseCode(responseCode);
		dto.setMessage(responseMessage);
		return dto;
	}
	public static String decryptPin(String dbPin) {
		String plainPin = "";
		try {
			if (dbPin != null) {
				plainPin = EncryptTransactionPin.decrypt(dbPin);
				plainPin=plainPin.substring(0,6);
			} else {
				logger.debug("error while decrypting pin..");
			}
			plainPin = plainPin.substring(0, plainPin.length() - 1);
		} catch (Exception e) {
			logger.error("error while decrypting pin..");
			e.printStackTrace();
		}
		return plainPin;
	}
	public static boolean validatePin(String channelPin, String dbPin, String channel) {
		boolean isValid = false;
		try {
			switch(channel){
				case "1":
					if (channelPin.length() == 16) {
						isValid = channelPin.equals(dbPin) ? true : false;
					} else if (dbPin != null && channelPin.length() > 16 && dbPin.length() == 16) {
						isValid = channelPin.equals(toHex(b64_sha256(decryptPin(dbPin)))) ? true : false;
					} else
						isValid = channelPin.equals(dbPin) ? true : false;
					break;
				case "3":
					if (dbPin != null && channelPin.length() > 16 && dbPin.length() == 16) {
						isValid = channelPin.equals(toHex(b64_sha256(decryptPin(dbPin)))) ? true : false;
					} else
						isValid = channelPin.equals(dbPin) ? true : false;
					break;
				case "2":
						isValid = channelPin.equals(dbPin) ? true : false;
					break;
				case "5":
					isValid = channelPin.equals(dbPin) ? true : false;
					break;
				default:
					isValid =false;
					break;
			}
			/*if (channelPin.length() == 16) {
				isValid = channelPin.equals(dbPin) ? true : false;
			} else if (dbPin != null && channelPin.length() > 16 && dbPin.length() == 16) {
				isValid = channelPin.equals(toHex(b64_sha256(dbPin))) ? true : false;
			} else
				isValid = channelPin.equals(dbPin) ? true : false;
			*/
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return isValid;
	}
	
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		 Map<String, Object> retMap = new HashMap<String, Object>();

		 if(json != null) {
		 retMap = toMap(json);
		 }
		 return retMap;
		}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		 Map<String, Object> map = new HashMap<String, Object>();

		 Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		 return map;
		}
	public static boolean isMultiCurrencyTxn(String sourceCurrency, String txnCurrency) {
		return sourceCurrency.equals(txnCurrency) ? false : true;
	}
	public static String convertDateToStirng(Date date, String format){
		DateFormat dateFormat = new SimpleDateFormat(format);  
        String strDate = dateFormat.format(date.getTime());  
        return strDate;
	}
	
	public static ResponseDTO constructFailureRespose(Object object,
			String responsCode) {
		ResponseDTO dto = new ResponseDTO();
		dto.setResponseCode(responsCode);
		dto.setMessage(object.toString());
		dto.setData(object);
		return dto;
	}
	
	public static BigDecimal calculateAgentIncome(Incentive incentive, Fee fee) {
		BigDecimal agentIncome= null;
		try{
			if(incentive != null && incentive.getId()>0){
				if(incentive.getIncVal().intValue()>0){
					agentIncome = getPercentageIncome(fee.getAgntCmsn().add(fee.getSupAgentCmsn()).add(incentive.getIncVal()), fee.getFeeVal());
				}else{
					agentIncome = getPercentageIncome(fee.getAgntCmsn().add(fee.getSupAgentCmsn()), fee.getFeeVal());
				}
			}else{
				agentIncome = getPercentageIncome(fee.getAgntCmsn().add(fee.getSupAgentCmsn()), fee.getFeeVal());
			}
		}catch(Exception e){
			logger.error("Error While calculateAgentIncome..:"+e.getLocalizedMessage());
		}finally{
			incentive = null;
			fee = null;
		}
		logger.debug("agentIncome..:"+agentIncome);
		return agentIncome;
	}
	public static BigDecimal getPercentageIncome(BigDecimal agentCommission, BigDecimal totalFee) {
		BigDecimal incomePercent =null;
		try{
			incomePercent = agentCommission.divide(totalFee);
		}catch(Exception e){
			logger.error("Error..:"+e.getLocalizedMessage());
			incomePercent = new BigDecimal(0);
		}
	    return incomePercent;
	}
}
