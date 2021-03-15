package com.ceva.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ceva.db.model.Reversals;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

@Service("airtimeTokenReversalServcie")
public class AirtimeTokenReversalServcie {
	
	Logger logger = Logger.getLogger(AirtimeTokenReversalServcie.class);
	
	static Map<String, String> bundle =  ConfigLoader.convertResourceBundleToMap("config");
	private static String tokenReversalUrl = bundle.get("bank.data.reversalurl");
	
	private Reversals updateReversal(Reversals reversal) {
		if(reversal.getRetryCount()<3){
			reversal.setRetryCount(reversal.getRetryCount()+1);
			reversal.setStatus("P");
		}else{
			reversal.setRetryCount(reversal.getRetryCount()+1);
			reversal.setStatus("E");
		}
		return reversal;
	}

	public Reversals reverseTransactionWithTokenCBA(Reversals reversal) {
		try{
			JSONObject request = new JSONObject();
			request.put("ReferenceCode", reversal.getAbReference());
			request.put("RequestId", reversal.getRefNumber());
			JSONObject response = excecuteService(request, tokenReversalUrl);
			if(Constants.SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))){
				reversal.setStatus("S");
				reversal.setRevRefNumber(response.getString("TransactionReference"));
			}else{
				updateReversal(reversal);
				reversal.setMessage(response.getString("ResponseMessage"));
			}
		}catch (Exception e) {
			e.printStackTrace();
			updateReversal(reversal);
		}
		return reversal;
	}
	
	public JSONObject excecuteService(Object object, String requesturl)
			throws Exception {
		JSONObject response = null;
		logger.info(requesturl);
		logger.info(object);
		
		if(requesturl.startsWith("https")){
			response = callHttpsService(object, requesturl);
		}else{
			response = callHttpService(object, requesturl);
		}
		logger.info(response);
		return response;
	}

	private JSONObject callHttpsService(Object object, String requesturl) {

		HttpsURLConnection con = null;
		JSONObject response = null;
		String resp = null;
		try {
			URL url = new URL(null,requesturl,new sun.net.www.protocol.https.Handler());

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}
			} };

			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc
						.getSocketFactory());
			} catch (Exception e) {
			}
 
			HttpsURLConnection
					.setDefaultHostnameVerifier(new HostnameVerifier() {
						public boolean verify(String hostname,
								SSLSession session) {
							return true;
						}
					});
			
			con = (HttpsURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.addRequestProperty("content-length", Integer.toString(object.toString().length()));


			con.connect();
			OutputStreamWriter request = new OutputStreamWriter(
					con.getOutputStream());
			request.append(object.toString());
			request.flush();
			request.close();
			System.out.println("Response Code = " + con.getResponseCode());
			BufferedInputStream bufferedInputStream = null;
			if(con.getResponseCode() == 200 || con.getResponseCode() == 201){
				bufferedInputStream = new BufferedInputStream(
						con.getInputStream());
			}else{
				bufferedInputStream=new BufferedInputStream(
						con.getErrorStream());
			}

			ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream(
					bufferedInputStream.available());
			for (int b = 0; (b = bufferedInputStream.read()) != -1;) {
				bytesOutputStream.write(b);
			}
			bufferedInputStream.close();
			resp = bytesOutputStream.toString();
			System.out.println("Response..:"+resp);
			response = JSONObject.fromObject(resp);
		} catch (Exception e) {
			e.printStackTrace();
			response = constructFailureMessage("Timeout", "9999");

		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return response;
	}
	
	private JSONObject callHttpService(Object object, String requesturl) {

		HttpURLConnection con = null;
		JSONObject response = null;
		String resp = null;
		try {
			URL url = new URL(requesturl);

			con = (HttpURLConnection) url.openConnection();
			logger.debug("Connection opened");

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.addRequestProperty("content-length",
					Integer.toString(object.toString().length()));


			con.connect();
			OutputStreamWriter request = new OutputStreamWriter(
					con.getOutputStream());
			request.append(object.toString());
			request.flush();
			request.close();
			logger.debug("Response Code = " + con.getResponseCode());
			BufferedInputStream bufferedInputStream = null;
			if(con.getResponseCode() == 200 || con.getResponseCode() == 201){
				bufferedInputStream = new BufferedInputStream(
						con.getInputStream());
			}else{
				bufferedInputStream=new BufferedInputStream(
						con.getErrorStream());
			}

			ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream(
					bufferedInputStream.available());
			for (int b = 0; (b = bufferedInputStream.read()) != -1;) {
				bytesOutputStream.write(b);
			}
			bufferedInputStream.close();
			resp = bytesOutputStream.toString();
			logger.debug("Response..:"+resp);
			response = JSONObject.fromObject(resp);
		} catch (Exception e) {
			logger.error("Error ... ");
			e.printStackTrace();
			response = constructFailureMessage("Timeout", "9999");

		} finally {
			if (con != null) {
				con.disconnect();
				logger.debug("Disconnected");
			}
		}
		return response;
	}
	
	private JSONObject constructFailureMessage(String message, String code){
		JSONObject response= new JSONObject();
		response.put("ResponseMessage", message);
		response.put("ResponseCode", code);
		return response;
	}

}
