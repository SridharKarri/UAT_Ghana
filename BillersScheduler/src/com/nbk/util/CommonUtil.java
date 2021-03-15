package com.nbk.util;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


public class CommonUtil
{
	public static Logger logger = Logger.getLogger(CommonUtil.class);
	public static String outputString = null;


	public static String getRandomInteger() {
		int aStart = 100000;
		int aEnd = 999999;
		Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = aEnd - aStart + 1L;
		long fraction = (long)(range * aRandom.nextDouble());
		Long randomNumber = Long.valueOf(fraction + aStart);
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

		return password;
	}












	public static String maskCardNumber(String cardNumber, String mask) {
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


		return maskedNumber.toString();
	}





	public static void main(String[] args) {
		String number = "1231231231";
		System.out.println(String.valueOf(number.substring(0, 3)) + "****" + number.substring(7, number.length()));
	}



	public static Map<String, String> getErrorsInASaneFormat(BindingResult result) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : result.getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return errors;
	}


	public static String logSoapRequest(Class<?> t, Object obj) {
		String xml = "";
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { t });
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(obj, sw);
			xml = sw.toString();
			logger.info("Request body:" + xml);
		} catch (Exception e) {
			logger.error("Error While Constructing soap xml..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} 
		return xml;
	}
	public static final String createRandomNumber(long len) {
		if (len > 18L)
			throw new IllegalStateException("To many digits"); 
		long tLen = (long)Math.pow(10.0D, (len - 1L)) * 9L;

		long number = (long)(Math.random() * tLen) + (long)Math.pow(10.0D, (len - 1L)) * 1L;

		String tVal = (new StringBuilder(String.valueOf(number))).toString();
		if (tVal.length() != len) {
			throw new IllegalStateException("The random number '" + tVal + "' is not '" + len + "' digits");
		}
		return tVal;
	}

	public static String getLastDate(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		calendar.set(5, calendar.getActualMaximum(5));
		Date date = calendar.getTime();
		DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
		return DATE_FORMAT.format(date);
	}

	public static String theMonth(int month) {
		String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		return monthNames[month];
	}
}