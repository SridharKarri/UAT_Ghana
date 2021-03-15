package com.nbk.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigLoader {

	public static Map<String, String> convertResourceBundleToMap(String resource) {
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		ResourceBundle bundle = null;
		try {
			bundle = ResourceBundle.getBundle(resource);
			Enumeration<String> keys = bundle.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				map.put(key, bundle.getString(key));
			}

		} catch (Exception e) {

		}
		return map;
	}

}
