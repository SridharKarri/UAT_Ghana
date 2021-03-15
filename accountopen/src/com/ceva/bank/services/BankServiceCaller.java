package com.ceva.bank.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class BankServiceCaller {
	
	private static final String APPID="CEVANGN";
	private static final String APPKEY="RA==";
	
	private static final Logger log= Logger.getLogger(BankServiceCaller.class);
	
	public ResponseDTO  fraudcall(net.sf.json.JSONObject request, String url) {

		String inputLine="";
		ResponseDTO responseDTO = new ResponseDTO();
		StringBuffer response = new StringBuffer();
		JSONObject jresponse = new JSONObject();
		log.info("Fraud url.:"+url);
		log.info("Fraud Request.:"+request);
		
		URL obj=null;
		BufferedReader in =null;
		DataOutputStream wr =null;
		try {
			
			jresponse.put("respcode", "99");
			jresponse.put("respdesc", "Service Internal Error");
			obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("ApplicationID",APPID );
			con.setRequestProperty("AppKey", APPKEY);
			con.setRequestProperty("Content-Type", "application/json");
			
			con.addRequestProperty("content-length", request.toString().getBytes().length+"");
			 
			String data = request.toString();

			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			jresponse.put("httprespcode", responseCode);
			
			if((responseCode==201) || (responseCode == 200))
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			else 
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			log.info("Excat Bank Response .:"+response.toString());
			jresponse= JSONObject.fromObject(response.toString());
			if("0".equals(jresponse.getString("ResponseCode"))||"0".equals(jresponse.getString("ResponseStatus")))
			{
				responseDTO.setMessage(Constants.SUCESS_SMALL);
				responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
			}else{
				responseDTO.setMessage(jresponse.getString("ResponseMessage"));
				responseDTO.setResponseCode(jresponse.getString("ResponseCode"));
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
			responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
		}
		finally
		{
			if(wr!=null)
			{
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in!=null)
			{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return responseDTO;
	}
	

}
