package com.ceva.crypto.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * Aes encryption
 */
public class AES {
	private Logger logger = Logger.getLogger(AES.class);
	private SecretKeySpec secretKey;
	private byte[] key;

	private String decryptedString;
	private String encryptedString;

	private String skey = "";

	private static ResourceBundle resourceBundle = null;

	static {
		resourceBundle = ResourceBundle.getBundle("config");
	}

	public void setKey() {
		skey = resourceBundle.getString("aes.key");
		MessageDigest sha = null;
		try {
			key = skey.getBytes("UTF-8");
			logger.debug("Key length ====> " + key.length);
			sha = MessageDigest.getInstance("SHA-512");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit

			secretKey = new SecretKeySpec(key, "AES");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getDecryptedString() {
		return decryptedString;
	}

	public void setDecryptedString(String decryptedString) {
		this.decryptedString = decryptedString;
	}

	public String getEncryptedString() {
		return encryptedString;
	}

	public void setEncryptedString(String encryptedString) {
		this.encryptedString = encryptedString;
	}

	public String encrypt(String strToEncrypt) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);

			setEncryptedString(Base64.encodeBase64String(cipher
					.doFinal(strToEncrypt.getBytes("UTF-8"))));

		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public String decrypt(String strToDecrypt) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

			cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
			setDecryptedString(new String(cipher.doFinal(Base64
					.decodeBase64(strToDecrypt))));

		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static void main(String args[]) throws UnsupportedEncodingException {
		String strToEncrypt = "9298";
		// strToEncrypt = "4015";
		// final String strPssword =
		// "AeH6GrLRGK2SBtNiziAdl+Z9HK+98qChhGuCaLZ7O5M";
		AES aes = new AES();

		aes.setKey();

		aes.encrypt(strToEncrypt.trim());
		//
		System.out.println("Encrypted: " + aes.getEncryptedString());

		// final String strToDecrypt = aes.getEncryptedString();
		aes.decrypt("4VF0oYRnu5CsJKeGVtShfg==");

		// System.out.println("String To Decrypt : " + strToDecrypt);
		System.out.println("Decrypted : " + aes.getDecryptedString());

	}

}
