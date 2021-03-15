package com.ceva.crypto.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class AESBouncyCastle {

	private final BlockCipher AESCipher = new AESEngine();

	private PaddedBufferedBlockCipher pbbc;
	private KeyParameter key;

	public void setPadding(BlockCipherPadding bcp) {
		this.pbbc = new PaddedBufferedBlockCipher(AESCipher, bcp);
	}

	public void setKey(byte[] key) {
		this.key = new KeyParameter(key);
	}

	public byte[] encrypt(byte[] input) throws DataLengthException,
			InvalidCipherTextException {
		return processing(input, true);
	}

	public byte[] decrypt(byte[] input) throws DataLengthException,
			InvalidCipherTextException {
		return processing(input, false);
	}

	private byte[] processing(byte[] input, boolean encrypt)
			throws DataLengthException, InvalidCipherTextException {

		pbbc.init(encrypt, key);

		byte[] output = new byte[pbbc.getOutputSize(input.length)];
		int bytesWrittenOut = pbbc.processBytes(input, 0, input.length, output,
				0);

		pbbc.doFinal(output, bytesWrittenOut);

		return output;

	}

	private static SecretKeySpec secretKey;
	private static byte[] key1;

	public static void setKey(String myKey) {

		MessageDigest sha = null;
		try {
			key1 = myKey.getBytes("UTF-8");
			System.out.println("Key length ====> " + key1.length);
			sha = MessageDigest.getInstance("SHA-512");
			key1 = sha.digest(key1);
			key1 = Arrays.copyOf(key1, 16); // use only first 128 bit

			secretKey = new SecretKeySpec(key1, "AES");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DataLengthException,
			InvalidCipherTextException {
		
		setKey("AeH6GrLRGK2SBtNiziAdl+Z9HK+98qChhGuCaLZ7O5M");
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		kg.init(256);
		SecretKey sk = secretKey;//kg.generateKey();

		AESBouncyCastle abc = new AESBouncyCastle();
		abc.setPadding(new PKCS7Padding());
		abc.setKey(sk.getEncoded());

		String secret = "This is a secret message!";
		System.out.println(secret);
		byte[] ba = secret.getBytes("UTF-8");

		byte[] encr = abc.encrypt(ba);
		System.out.println("Encrypted : " + byteArrayToHexString(encr));
		byte[] retr = abc
				.decrypt(hexStringToByteArray(byteArrayToHexString(encr)));

		if (retr.length == ba.length) {
			ba = retr;
		} else {
			System.arraycopy(retr, 0, ba, 0, ba.length);
		}

		String decrypted = new String(ba, "UTF-8");
		System.out.println(decrypted);
	}

	public static String byteArrayToHexString(byte b[]) {

		if (b == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer(b.length * 2);

		for (int i = 0; i < b.length; i++) {
			// look up high nibble char
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			// look up low nibble char
			sb.append(hexChar[b[i] & 0x0f]);
		}

		return sb.toString();
	}

	public static byte[] hexStringToByteArray(String hexString) {

		if (hexString == null) {
			return null;
		}

		byte[] data = hexString.getBytes();
		int l = data.length;
		if ((l & 0x01) != 0) {
			// throw new CodecException("odd number of characters.");
		}
		byte[] out = new byte[l >> 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; j < l; i++) {
			int f = Character.digit((char) data[j++], 16) << 4;
			f = f | Character.digit((char) data[j++], 16);
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

	static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

}