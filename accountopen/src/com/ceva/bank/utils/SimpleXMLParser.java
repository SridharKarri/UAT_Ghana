package com.ceva.bank.utils;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SimpleXMLParser {

	public Map<String, String> parseXML(String data) 
	{
		SimpleXMLHandler handler = new SimpleXMLHandler();
		try 
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			//InputStream data = SimpleXMLParser.class.getResourceAsStream(fileName);
			InputStream is = new ByteArrayInputStream(data.getBytes());
			parser.parse(is, handler);
		} 
		catch (java.lang.IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return handler.getMap();

	}
	
	
	public static void main(String[] args) throws Exception 
	{
		String data = "<?xml version=\"1.0\" encoding=\"Windows-1252\"?><Response xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ResponseCode>90000</ResponseCode><Customer><CustomerValidationField /><PaymentCode>0</PaymentCode><CustomerId>14181869886</CustomerId><WithDetails>False</WithDetails><ResponseCode>70010</ResponseCode><ResponseDescription>70010 - Biller not found for: 0</ResponseDescription><Amount>0</Amount></Customer><Customer><PaymentCode>0</PaymentCode><CustomerId>14181869886</CustomerId><ResponseCode>70010</ResponseCode><ResponseDescription>70010 - Biller not found for: 0</ResponseDescription><Amount>0</Amount></Customer></Response>";		
		Map<String, String> map = new SimpleXMLParser().parseXML(data);
		System.out.println(map);
	}

}

