package com.nbk.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.xml.sax.InputSource;

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
			logger.debug("b64_sha256 outputString::" + outputString);
		} else {
			logger.debug("Input String Missing for b64_sha256");
		}
		outputString = outputString.substring(0, outputString.length() - 1);
		return outputString;

	}

	public static String generatePassword(int size) {
		String password = null;
		password = RandomStringUtils.randomNumeric(size);
//		logger.debug("[Password::" + password + "]");
		return password;
	}

	/**
	 * Applies the specified mask to the card number.
	 *
	 * @param cardNumber The card number in plain format
	 * @param mask The number mask pattern. Use # to include a digit from the
	 * card number at that position, use x to skip the digit at that position
	 *
	 * @return The masked card number
	 */
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

	public static void main(String args[]) {
	/*	String output = b64_sha256("nbkmobilebankingpinblock");
		System.out.println(output);
		// System.out.println(generatePassword());
		System.out.println(RandomStringUtils.randomNumeric(6));*/
		String number="1231231231";
		System.out.println(number.substring(0, 3)+"****"+number.substring(7, number.length()));

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
			logger.info("Request body:" + xml);
		} catch (Exception e) {
			logger.error("Error While Constructing soap xml..:"+e.getLocalizedMessage());
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
	
	public static String getLastDate(int month, int year) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month - 1, 1);
	    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
	    Date date = calendar.getTime();
	    DateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy");
	    return DATE_FORMAT.format(date);
	}
	
	public static String theMonth(int month){
	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    return monthNames[month];
	}
	
	public static Object XmlToObject(String xml, Class cls) throws JAXBException{
		
        JAXBContext jaxbContext = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return unmarshaller.unmarshal(new InputSource(new StringReader(xml)));
	}

	public static String convertToXml(Object object) {
		String requestBody =null;
		JAXBContext jaxbContext = null;
		Marshaller jaxbMarshaller = null;
		StringWriter sw = null;
		try {
			jaxbContext = JAXBContext.newInstance(object.getClass());
			jaxbMarshaller = jaxbContext.createMarshaller();
			sw = new StringWriter();
			jaxbMarshaller.marshal(object, sw);
			requestBody = sw.toString();
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

}
