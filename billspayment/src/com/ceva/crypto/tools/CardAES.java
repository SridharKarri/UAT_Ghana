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
public class CardAES {
	private Logger logger = Logger.getLogger(CardAES.class);
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

		try {
			skey = new String(Base64.decodeBase64(resourceBundle
					.getString("card.aes.key")));

		} catch (Exception e1) {
			e1.printStackTrace();
		}

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

			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

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

			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			setDecryptedString(new String(cipher.doFinal(Base64
					.decodeBase64(strToDecrypt))));

		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static void main(String args[]) {
		String strToEncrypt = "DC4D3FBD7F4E5AB49FF698DE3DA6EAEA";
		// strToEncrypt = "4015";
		// final String strPssword =
		// "AeH6GrLRGK2SBtNiziAdl+Z9HK+98qChhGuCaLZ7O5M";

		// System.out.println(new
		// BASE64Encoder().encode("tCWqaxyQfG4nrajesOvdA49J4JRZBBx061RvnlyHwbM".getBytes()));

		CardAES aes = new CardAES();
		aes.setKey();
		// aes.encrypt(strToEncrypt.trim());
		aes.decrypt("yiZEKcdNThI5AOVMGD+0Ww==");
		
		System.out.println(aes.decryptedString);

		// System.out.println("String to Encrypt: " + strToEncrypt);
		// System.out.println("Encrypted: " + aes.getEncryptedString());

		// final String strToDecrypt = aes.getEncryptedString();
		aes.decrypt(resourceBundle.getString("card.pinblock.pin1"));

		// System.out.println("String To Decrypt : " + strToDecrypt);
		System.out.println("Decrypted : " + aes.getDecryptedString());

		aes.decrypt(resourceBundle.getString("card.pinblock.pin2"));

		// System.out.println("String To Decrypt : " + strToDecrypt);
		System.out.println("Decrypted : " + aes.getDecryptedString());

	}

}
