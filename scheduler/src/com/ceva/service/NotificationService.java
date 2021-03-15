package com.ceva.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import com.nbk.util.ConfigLoader;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class NotificationService {
	static String url = ConfigLoader.convertResourceBundleToMap("config").get("bank.push.url");
	
	public static JSONObject push(JSONObject jrequest) {
		System.out.println("pushing");
		String inputLine="";
		StringBuffer response = new StringBuffer();
		JSONObject jresponse = new JSONObject();
		URL obj=null;
		BufferedReader in =null;
		DataOutputStream wr =null;
		try {
			jresponse.put("respcode", "99");
			jresponse.put("respdesc", "Service Internal Error");
			obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			 
			String data = jrequest.toString();

			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			jresponse.put("httprespcode", responseCode);
			
			if((responseCode==201) || (responseCode == 200))
			{
				in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
			}else {
				
				in = new BufferedReader(
						new InputStreamReader(con.getErrorStream()));
			}
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			String finalresponse= URLDecoder.decode(response.toString(), "UTF-8");
			System.out.println("Router response ->"+finalresponse);
			jresponse =JSONObject.fromObject(finalresponse);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
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
		return jresponse;
	}

}
