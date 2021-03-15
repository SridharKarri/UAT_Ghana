package com.ceva.bank.utils;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleXMLHandler extends DefaultHandler {
	HashMap<String, String> hashMap = new HashMap<String, String>();
	String value = "";
	String key = "";

	public Map<String, String> getMap() {
		return hashMap;
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		hashMap.put(qName, key);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		key = new String(ch, start, length);
	}
}
