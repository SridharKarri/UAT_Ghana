package com.ceva.bank.services;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;







@Component("requestProcessor")
public class RequestProcessor
{
	public static Logger logger = Logger.getLogger(RequestProcessor.class);

	private String includeCerts(Object object) {
		String requestBody = null;

		try {
			requestBody = object.toString();
			logger.debug("requestBody..:" + requestBody);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return requestBody;
	}




	public JSONObject excecuteService(Object object, String requesturl) throws Exception {
		HttpURLConnection con = null;
		JSONObject response = null;
		String resp = null;
		try {
			String requestBody = includeCerts(object);
			logger.info(requestBody);
			logger.info(requesturl);
			URL url = new URL(requesturl);

			TrustManager[] trustAllCerts = { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}





				public void checkClientTrusted(X509Certificate[] certs, String authType) {}




				public void checkServerTrusted(X509Certificate[] certs, String authType) {}
			} };
			try {
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc
						.getSocketFactory());
			} catch (Exception exception) {}



			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
			{
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			con = (HttpURLConnection)url.openConnection();

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			con.addRequestProperty("content-length", 
					Integer.toString(requestBody.length()));

			logger.debug(requestBody);

			con.connect();
			OutputStreamWriter request = new OutputStreamWriter(
					con.getOutputStream());
			request.append(requestBody);
			request.flush();
			request.close();
			logger.debug("Response Code = " + con.getResponseCode());
			BufferedInputStream bufferedInputStream = null;
			if (con.getResponseCode() == 200 || con.getResponseCode() == 201) {
				bufferedInputStream = new BufferedInputStream(
						con.getInputStream());
			} else {
				bufferedInputStream = new BufferedInputStream(
						con.getErrorStream());
			} 

			ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream(
					bufferedInputStream.available());
			for (int b = 0; (b = bufferedInputStream.read()) != -1;) {
				bytesOutputStream.write(b);
			}
			bufferedInputStream.close();
			resp = bytesOutputStream.toString();
			response = JSONObject.fromObject(resp);
		} catch (Exception e) {
			logger.error("Error ... ");
			e.printStackTrace();
		} finally {

			if (con != null) {
				con.disconnect();
				logger.debug("Disconnected");
			} 
		} 
		logger.debug("excecuteService COMPLETED..:" + response);
		return response;
	}
}