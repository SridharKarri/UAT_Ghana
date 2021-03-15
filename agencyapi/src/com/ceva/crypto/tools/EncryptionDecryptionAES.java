package com.ceva.crypto.tools;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class EncryptionDecryptionAES {
	static Cipher cipher;


	public static void main(String[] args) throws Exception {
		final int outputKeyLength = 128;
		 SecureRandom secureRandom = new SecureRandom();
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(outputKeyLength,secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();

		System.out.println(new String(secretKey.getEncoded()));

		cipher = Cipher.getInstance("AES");

		String plainText = "AES Symmetric Encryption Decryption";
		System.out.println("Plain Text Before Encryption: " + plainText);

		String encryptedText = encrypt(plainText, secretKey);
		System.out.println("Encrypted Text After Encryption: " + encryptedText);

		String decryptedText = decrypt(encryptedText, secretKey);
		System.out.println("Decrypted Text After Decryption: " + decryptedText);
	}

	public static String encrypt(String plainText, SecretKey secretKey)
			throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		/*Base64.Encoder encoder = Base64.getEncoder();
		String encryptedText = encoder.encodeToString(encryptedByte);*/
		String encryptedText = Base64.encodeBase64String(encryptedByte);
		return encryptedText;
	}

	public static String decrypt(String encryptedText, SecretKey secretKey)
			throws Exception {
		/*Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);*/
		byte[] encryptedTextByte = Base64.decodeBase64(encryptedText);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
}