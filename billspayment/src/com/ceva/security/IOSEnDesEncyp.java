package com.ceva.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class IOSEnDesEncyp {

	private static final String ALGORITHM = "AES/ECB/PKCS7Padding";
	// private static final byte[] keyValue = new byte[]{'A', 'b', 'c', 'D',
	// 'e', 'f', 'G', 'h', 'I', 'L', 'm', 'n', 'o', 'P', 'Q', 'r'};
	//private static final byte[] keyValue = "CktMfgdANQ5vttY0".getBytes();
	//private static final byte[] keyValue3 = "AeH6GrLRGK2SBtNi".getBytes();
	//private static final byte[] keyValue2 = "gcMjv0XShrQMdDc8".getBytes();

			//AeH6GrLRGK2SBtNi
	public String encrypt(String valueToEnc,Key key) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher c = Cipher.getInstance(ALGORITHM, "BC");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEnc.getBytes("UTF8"));
		String encryptedValue = new BASE64Encoder().encode(encValue);
		return encryptedValue;
	}

	public String decrypt(String encryptedValue,Key key) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher c = Cipher.getInstance(ALGORITHM, "BC");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	public String decryptMasterKey(String encryptedValue,Key key) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		Cipher c = Cipher.getInstance(ALGORITHM, "BC");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	public Key generateKey(byte[] keyValue) throws Exception {
		// System.out.println(keyValue[0]);
		//Key key = new SecretKeySpec(keyValue, ALGORITHM);
		Key key2 = new SecretKeySpec(keyValue, ALGORITHM);
		// SecretKeyFactory keyFactory =
		// SecretKeyFactory.getInstance(ALGORITHM);
		// key = keyFactory.generateSecret(new DESKeySpec(keyValue));
		return key2;
	}

	public Key generateMasterKey(byte[] keyValue) throws Exception {
		// System.out.println(keyValue[0]);
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		// SecretKeyFactory keyFactory =
		// SecretKeyFactory.getInstance(ALGORITHM);
		// key = keyFactory.generateSecret(new DESKeySpec(keyValue));
		return key;
	}

	public String generateSessionKey() throws NoSuchAlgorithmException, NoSuchProviderException
	 {
		 KeyGenerator kgen = KeyGenerator.getInstance("AES", "BC");
		  kgen.init(128);
		  SecretKey key = kgen.generateKey();
		  byte[] symmKey = key.getEncoded();
		  String encodedKey = new BASE64Encoder().encode(symmKey).substring(0, 16);
		  return encodedKey;

	 }

	static
	 {
	  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	 }

	public static void main(String[] args) {
		IOSEnDesEncyp crypto = new IOSEnDesEncyp();
		try {
			//byte[] keygen = generateSessionKey();
			//System.out.println(keygen+"\n");
		//	String Key = new String(keygen, StandardCharsets.UTF_8);

		//	System.out.println("{"+Key+"}");
			//System.out.println("\n\n");
			String encrypter = crypto.encrypt("1234",new IOSEnDesEncyp().generateMasterKey("AeH6GrLRGK2SBtNi".getBytes()));
			System.out.println("\nEncrypted:"+encrypter+"\n\n");
			//String decryptMasterKey = crypto.decryptMasterKey("umhKEVNODhIDzDeWv0Zybg==");
			//String decryptRandomKey = crypto.decrypt(decryptMasterKey);
			//String decryptRandomKey = crypto.decrypt("umhKEVNODhIDzDeWv0Zybg==",generateMasterKey("AeH6GrLRGK2SBtNi".getBytes()));
			//System.out.println("\nDecrypted:"+decryptRandomKey);
			//System.out.println(crypto.decrypt("umhKEVNODhIDzDeWv0Zybg=="));

			System.out.println(new IOSEnDesEncyp().generateSessionKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}