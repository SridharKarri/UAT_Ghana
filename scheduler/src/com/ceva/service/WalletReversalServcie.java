package com.ceva.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.services.WalletProcessor;
import com.ceva.db.model.Reversals;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Service("walletReversalServcie")
public class WalletReversalServcie {
	
	Logger log = Logger.getLogger(WalletReversalServcie.class);
	
	@Autowired
	private WalletProcessor walletProcessor;
	
	static Map<String, String> bundle =  ConfigLoader.convertResourceBundleToMap("config");
	private static String authCode = bundle.get("wallet.bank.authcode");
	private static String reversalUrl = bundle.get("wallet.bank.reversal");
	private static String tokenReversalUrl = bundle.get("bank.data.reversalurl");
	
	public Reversals reverseTransactionWithFMW(Reversals reversal) {
		Map<String, String> data = new HashMap<>();
		try {
			JSONObject request = prepareReversalRequest(reversal.getRefNumber(), reversal.getAbReference());
			JSONObject response = walletProcessor.excecuteService(request, reversalUrl);
			if (Constants.SUCCESS_RESP_CODE.equals(response.getString("respcode"))) {
				reversal.setStatus("S");
				reversal.setMessage(response.getString("exttxnrefno"));
				data.put("walletRefNumber", response.getString("exttxnrefno"));
			} else {
				reversal = updateReversal(reversal);
				reversal.setRetryCount(reversal.getRetryCount()+1);
			}
		} catch (Exception e) {
			reversal.setStatus("P");
			reversal = updateReversal(reversal);
			log.error("error while calling generateReversalWithWallet..:" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {

		}
		return reversal;
	}
	
	private Reversals updateReversal(Reversals reversal) {
		if(reversal.getRetryCount()<3){
			reversal.setRetryCount(reversal.getRetryCount()+1);
			reversal.setStatus("P");
		}else{
			reversal.setStatus("E");
		}
		return reversal;
	}

	private JSONObject prepareReversalRequest(String transactionRef, String userId) {
		
		JSONObject request = new JSONObject();
		
		JSONObject jheader = new JSONObject();
		
		Long nanoTime = System.nanoTime();
		Long curTime = System.currentTimeMillis();
		String signatureString=userId+curTime+"AGENT"+"TRANSAC"+nanoTime+"FBNAGENT"+"AGNCASHIN"+transactionRef;
		System.out.println(signatureString);
		jheader.put("requesttype", "TRANSAC");
		jheader.put("clientid", "FBNAGENT");
		jheader.put("nanotime", nanoTime+"");
		jheader.put("channel", "AGENT");
		jheader.put("userid", userId);
		jheader.put("flowid", curTime+"");
		jheader.put("authsignature", generateSignature(signatureString));
		
		//txnrefno|craccount|txnamount|curreny|servicecode|narration|
		JSONObject jbody = new JSONObject();
		jbody.put("servicecode","AGNCASHIN");
		jbody.put("txnrefno", transactionRef);
		request.put("jbody", jbody);
		request.put("jheader", jheader);
		return request;
	}
	private Object generateSignature(String signatureString) {
		return hash(signatureString, authCode);
	}
	
	public static String hash(String signData, String authcode){
	    String signature = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        signData=signData+authcode;
	        byte[] bytes = md.digest(signData.getBytes(StandardCharsets.UTF_8));
	        signature= new String(Base64.getEncoder().encode(bytes));
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return signature;
	}

	public Reversals reverseTransactionWithCBA(Reversals reversal) {
		try {
			reversal.setMessage("reversal api not available");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return reversal;
	}

	public Reversals reverseTransactionWithTokenCBA(Reversals reversal) {
		try{
			JSONObject request = new JSONObject();
			request.put("ReferenceCode", reversal.getRefNumber());
			JSONObject response = excecuteService(request, tokenReversalUrl);
			if(Constants.SUCCESS_RESP_CODE.equals(response.getString("ResponseCode"))){
				reversal.setStatus("S");
			}
		}catch (Exception e) {
			e.printStackTrace();
			updateReversal(reversal);
		}
		return reversal;
	}
	
	public JSONObject excecuteService(JSONObject object, String requesturl)
			throws Exception {

		HttpURLConnection con = null;
		JSONObject response = null;
		String resp = null;
		try {
			String requestBody =object.toString();
			log.info(requestBody);
			log.info(requesturl);
			URL url = new URL(requesturl);

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

			// Install the all-trusting trust manager
			try {
				SSLContext sc = SSLContext.getInstance("TLS");
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

			con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			con.addRequestProperty("content-length", Integer.toString(requestBody.length()));

			con.connect();

			OutputStreamWriter request = new OutputStreamWriter(
					con.getOutputStream());
			request.append(requestBody);
			request.flush();
			request.close();
			log.debug("Response Code = " + con.getResponseCode());
			BufferedInputStream bufferedInputStream = null;
			if(con.getResponseCode() == 200){
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
			response = JSONObject.fromObject(resp);

		} catch (Exception e) {
			log.error("Error ... "+e.getLocalizedMessage());
			e.printStackTrace();

		} finally {
			if (con != null) {
				con.disconnect();
				log.debug("Disconnected");
			}
		}
		log.info("excecuteService COMPLETED..:"+response);
		return response;
	}

	

}
