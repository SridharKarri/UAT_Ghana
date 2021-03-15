package com.ceva.test;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ceva.crypto.tools.AESCBCEncryption;
import com.ceva.security.SecurityUtil;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

public class SecurityTest {
	private static String sessionkey="EHxxOa9FpK256bvlaICg2bYVsxKodO4XekhsJEdNzaE=";
	private static String siv="VZcLVAk8WrZ54YFznxxNUg==";
	private static String piv="uSaVK6wmtnvwBuFj6W3OPg==";
	
	public static void main(String[] args) {
		
		JSONObject data = new JSONObject();
		data.put("channel", "1");
		data.put("serviceId", "1");
		data.put("userId", "SURESH");
		
		
		Map<String, Object> map = getSortedMap(data);
		String strToHash = getSortedStringToHash(map);
		String hashedBody = SecurityUtil.generateHashString(strToHash);
		System.out.println(hashedBody);
		
		JSONObject finalObj = new JSONObject();
		finalObj.put("hash", hashedBody);
		
		
		String encrypted = encrypt(sessionkey.getBytes(), siv.getBytes(), data.toString());
		System.out.println(encrypted);
		finalObj.put("data", encrypted);
		System.out.println(finalObj);
		
	}
	
	public static Map<String, Object> getSortedMap(JSONObject data){
		Map<String, Object> map = new TreeMap<>();
		Iterator<String> keyIterator = data.keySet().iterator();
		while(keyIterator.hasNext()){
			String key=keyIterator.next();
			map.put(key, data.get(key));
		}
		return map;
	}
	
	public static String getSortedStringToHash(Map<String, Object> map){
		StringBuilder strToHash = new StringBuilder();
		Iterator<String> keyIterator = map.keySet().iterator();
		while(keyIterator.hasNext()){
			String key=keyIterator.next();
			strToHash.append(map.get(key));
		}
		return strToHash.toString();
	}
	

    public static String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(Constants.SYMETRICKEY_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return AESCBCEncryption.base64Encode(encrypted);
            //return Base64.encode(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
           
        }

        return null;
    }
}
