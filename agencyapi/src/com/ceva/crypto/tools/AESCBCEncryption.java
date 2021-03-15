package com.ceva.crypto.tools;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.nbk.util.CommonUtil;
import com.nbk.util.Constants;


public class AESCBCEncryption implements Constants {
	static
	{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	public static String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(SYMETRICKEY_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
           /* System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));*/

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(byte[] key, byte[] initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(SYMETRICKEY_ALG);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }

    public static byte[] generateSessionKey() throws NoSuchAlgorithmException, NoSuchProviderException
	{
		KeyGenerator kgen = KeyGenerator.getInstance(KEYGEN_ALG, PKI_PROVIDER);
		kgen.init(SYMETRIC_KEY_SIZE);
		SecretKey key = kgen.generateKey();
		byte[] symmKey = key.getEncoded();
		return symmKey;
	}

	public static byte[] generateIV() throws NoSuchAlgorithmException, NoSuchProviderException{
		return Arrays.copyOfRange(generateSessionKey(), 0, 16);
	}

	public static String base64Encode(byte[] binaryData)
	{
		return Base64.encodeBase64String(binaryData);
	}


	public static byte[] base64Decode(String base64String)
	{
		return Base64.decodeBase64(base64String);
	}


	public static String firstLogin() throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		byte[] key = base64Decode("mYCvSane74ZV2rS5BXiWi0beWxFQ2037I00wLipnFhU=");	//generateSessionKey(); // 256 bit key
        byte[] initVector = base64Decode("Eq/cxCxt6YPHUdy65/FMuA==");


		return sb.append("http://localhost:1111/agencyapi/rest/appws/Throwerr/")
				.append(CommonUtil.toHex(encrypt(key, initVector, "HelloWorld")))
				.append("/"+CommonUtil.generateHashString("HelloWorld"))
				.append("/"+"sureshdwarapudi")
				.append("/"+"9229874682736729")
				.append("/155155155")
				.append("/1212")
				.append("/"+CommonUtil.toHex(encrypt(key, initVector, "HelloWorld")))
				.append("/"+CommonUtil.toHex(encrypt(key, initVector, "HelloWorld")))
				.append("/version")
				.append("/Release")
				.toString();
	}


    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
       /* String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
*/

         //generateIV();

        //System.out.println("length : "+EnDesEncyp.base64Encode(initVector));

        //System.out.println(decrypt(key, initVector,
                //encrypt(key, initVector, "Hello World")));

    	System.out.println(base64Encode(generateIV()));

    }
}