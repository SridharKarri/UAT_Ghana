package com.ceva.crypto.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.axis.encoding.Base64;
import org.apache.log4j.Logger;

public class SHA512 {
	Logger log = Logger.getLogger(SHA512.class);
	public SHA512() {
		super();
	}

	public String encrypt(String message) {
		MessageDigest md;
		String out = "";
		try {
			md = MessageDigest.getInstance("SHA-512");

			md.update(message.getBytes());
			byte[] mb = md.digest();
			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				s = s.substring(s.length() - 2);
				out += s;
			}
			System.out.println(out.length());
			System.out.println("CRYPTO: " + out);

		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return out;
	}


	public String sha256(String data) {
		String hash256 = "";
		StringBuffer sb = null;
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes(StandardCharsets.US_ASCII));
			byte byteData[] = md.digest();
			sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			hash256 = Base64.encode(sb.toString().getBytes());
			log.info("base64 format : " + hash256);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error..:" + e.getLocalizedMessage());
		} finally {
			sb = null;
		}

		return hash256;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {

		//String encrypted = new SHA512().encrypt("100000566CA100000566ACNG");
		//String encrypted = new SHA512().sha256("972853E8-85C6-4964-9AAF-A8D03478B74A");
		String encrypted = new SHA512().sha256("7dc53df5-703e-49b3-8670-b1c468f47f1f");

		System.out.println("encrypted..:" + encrypted);
	}
}
