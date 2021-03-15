package com.ceva.security;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.ceva.bank.common.dao.impl.SecurityDAOImpl;
import com.ceva.crypto.tools.AESCBCEncryption;
import com.ceva.db.model.ChannelUsers;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class SecurityUtil {

	SecurityDAOImpl securityDAO;
	public static Logger logger = Logger.getLogger(SecurityUtil.class);
	public static String outputString = null;

	/** Method For Return 6 Digits Random Value **/
	public static String getRandomInteger() {
		int aStart = 100000;
		int aEnd = 999999;
		Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * aRandom.nextDouble());
		Long randomNumber = (Long) (fraction + aStart);
		return randomNumber.toString();
	}

	public static String b64_sha256(String inputString) {
		if (inputString != null) {
			outputString = Base64.encodeBase64String(
					DigestUtils.sha256(inputString)).trim();
			//logger.debug("b64_sha256 outputString...:" + outputString);
		} else {
			logger.debug("Input String Missing for b64_sha256");
		}
		outputString = outputString.substring(0, outputString.length() - 1);
		return outputString;

	}
	public static List<ChannelUsers> getAppChannelUsers(List<ChannelUsers> channelUsers){
		List<ChannelUsers> appChannelUsers=new ArrayList<>();
		for(ChannelUsers channelUser: channelUsers){
			if("APP".equals(channelUser.getCannelId())& channelUser.getImeiNo() != null){
				appChannelUsers.add(channelUser);
			}
		}
		return appChannelUsers;
	}

	public  static String maskAccountNumber(String accountNumber, String mask) {

		final Pattern hidePattern = Pattern.compile("(.{4})(.*)(.{3})");
		Matcher m = hidePattern.matcher(accountNumber);
		String output=null;

		if(m.find()) {
		 int l = m.group(2).length();
		  output = m.group(1) + new String(new char[l]).replace("\0", "*") + m.group(3);
		}
	    return output.toString();
	}

	public static void main(String args[]) throws UnsupportedEncodingException {
		String output = b64_sha256("54321");
		System.out.println(output);
		String hex = toHex(output);
		System.out.println(hex);
		// System.out.println(generatePassword());
		//System.out.println(RandomStringUtils.randomNumeric(6));

	}


	public static String convertToXml(Object object) {
		logger.info("convertToXml START");
		String requestBody =null;
		JAXBContext jaxbContext = null;
		Marshaller jaxbMarshaller = null;
		StringWriter sw = null;
		try {
			logger.debug("creating jaxb START");
			jaxbContext = JAXBContext.newInstance(object.getClass());
			jaxbMarshaller = jaxbContext.createMarshaller();
			sw = new StringWriter();
			jaxbMarshaller.marshal(object, sw);
			requestBody = sw.toString();
			logger.info("converted xml:"+requestBody);
		} catch (Exception e) {
			logger.error("Error while constructing xml.:"+e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			jaxbContext = null;
			jaxbMarshaller = null;
			sw = null;
		}
		return requestBody;
	}

	public static String toHex(String arg) throws UnsupportedEncodingException {
	    return String.format("%040x", new BigInteger(1, arg.getBytes("UTF-8")));
	}

	public static String toString(String hex) throws UnsupportedEncodingException, DecoderException{
		return new String(Hex.decodeHex(hex.toCharArray()),"UTF-8");
	}

	public static String generateHashString(String data)
	{
		 try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] dataInBytes = data.getBytes(StandardCharsets.UTF_8);
			md.update(dataInBytes);

			byte[] mdbytes = md.digest();

			StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<mdbytes.length;i++) {
	    	  hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
	    	}

	    	return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static HttpServletRequest buildServletRequest(Map<Inputs,String> inputMap,HttpServletRequest request,String queryString,StringBuffer requestURL,Boolean isResourse,String endPoint){//Map<String,String> params
		URLRequestWrapper requestWrapper=null;
		try
		{
			StringBuffer newURL = new StringBuffer();
			newURL.append(inputMap.get(Inputs.PROTOCOL)+"/"+inputMap.get(Inputs.BLANK)+"/"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+inputMap.get(Inputs.SERVICE_TYPE)+"/"+inputMap.get(Inputs.MAIN_PATH)+"/"+endPoint+"/"+queryString);//
			requestWrapper = new URLRequestWrapper(request, queryString, newURL,request.getContextPath()+"/"+inputMap.get(Inputs.SERVICE_TYPE)+"/"+inputMap.get(Inputs.MAIN_PATH)+"/"+endPoint+"/"+queryString);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return requestWrapper;
	}

	public static HttpServletRequest buildErrorMap(HttpServletRequest req,Map<Inputs,String> map,String message,String channel){
		map.put(Inputs.ENDPOINT, "errorPage");
		String path=message;
		HttpServletRequest enhancedHttpRequest = (HttpServletRequest)buildServletRequest(map,req, path,null,false,"errorPage");
		return enhancedHttpRequest;
	}
	public static Map<String,String> buildErrorMap(HttpServletRequest request,
			String desc,
			String channel,
			String appId){
		Map<String,String> errorMap = new HashMap<String, String>();

		errorMap.put("REQUEST_URL", request.getRequestURL().toString());
		errorMap.put("IP", request.getRemoteAddr());
		errorMap.put("ERROR_DESC", desc);
		errorMap.put("REQUEST_CHANNEL", channel);
		errorMap.put("APPID", (appId != null)?appId:" ");

		return errorMap;
	}
	public static String getAndroidPath(Map<Inputs, String> input,ResourceBundle bundle,Inputs inp) throws Exception
	{
		byte[] key = AESCBCEncryption.base64Decode(bundle.getString("LOC.KEY")); //bundle.getString("LOC.KEY");
		byte[] iv = AESCBCEncryption.base64Decode(bundle.getString("LOC.IV"));

	//	logger.debug("LOCAL KEY : "+bundle.getString("LOC.KEY")+",  IV : "+bundle.getString("LOC.IV"));
		try
		{
			String path = AESCBCEncryption.decrypt(key, iv, toString(input.get(inp)));
			logger.debug("path : "+path);
			return path;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Error while decrypting..:"+e.getLocalizedMessage());
			throw new Exception();
		}
	}

	public String getAndroidPath(Map<Inputs, String> input,String appId,Inputs inp) throws Exception{

		//SecurityDAOImpl dao = new SecurityDAOImpl();
		logger.debug("App ID : "+appId);
		ChannelUsers channelData = securityDAO.fetchSecurityData(appId);

		//byte[] key = AESCBCEncryption.base64Decode(securityDAO.fetchSecurityData(appId).getpKey());
		//byte[] piv = AESCBCEncryption.base64Decode(securityDAO.fetchSecurityData(appId).getpIv());
		byte[] key = AESCBCEncryption.base64Decode(channelData.getpKey());
		byte[] piv = AESCBCEncryption.base64Decode(channelData.getpIv());
		logger.debug("PERSONAL KEY : "+key);
		try{
			if(key == null || piv == null){
				throw new NullPointerException("Problem in unlocking the request");
			}
			String path = AESCBCEncryption.decrypt(key, piv, toString(input.get(inp)));
			return path;

		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("Error while decrypting..:"+e.getLocalizedMessage());
			throw new NullPointerException("Facing problem in decrypting the request.");
		}finally{
			channelData = null;
		}
	}

	public String getAndroidUserKeys(Map<Inputs, String> input,ChannelUsers channelData,Inputs inp) throws Exception{

		byte[] key = AESCBCEncryption.base64Decode(channelData.getpKey());
		byte[] piv = AESCBCEncryption.base64Decode(channelData.getpIv());
		try{
			if(key == null || piv == null){
				logger.error("key and piv are null... ");
				throw new NullPointerException("Problem in unlocking the request");
			}
			String path = AESCBCEncryption.decrypt(key, piv, toString(input.get(inp)));
			return path;

		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("Error : "+e.getLocalizedMessage());
			throw new NullPointerException("Facing problem in decrypting the request.");
		}finally{
			channelData = null;
		}
	}

	public static String getAndroidPath(Map<Inputs, String> input,Inputs inp,byte[] key,byte[] iv)
	{
		byte[] siv = iv;//AESCBCEncryption.base64Decode(new securityDAO().fetchSecurityData(appid).getSiv());
		try{
			if(key == null || siv == null){
				logger.error("key and siv are null... ");
				throw new NullPointerException("Problem in unlocking the request");
			}
			String path = AESCBCEncryption.decrypt(key, siv, toString(input.get(inp)));
			return path;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new NullPointerException("Facing problem in decrypting the request.");
		}
	}

	public static String concatePath(String path, String append) throws UnsupportedEncodingException {
		return path.substring(0,path.lastIndexOf("/")+1)+toHex(append);
	}

	public static String getWebPath(Map<Inputs, String> inputMap, Inputs input,
			byte[] base64Decode, byte[] base64Decode2) {
		logger.info("web path request need to construct..");
		return null;
	}

	public JSONObject buildResponseJSON (String[] responseString,String responseJSON,String reqType) throws Exception
	{
		logger.info("buildResponseJSON..:");
		String[] responses = responseString;
		ResourceBundle bundle=ResourceBundle.getBundle("filter");
		String channel = responses[5];
		switch(channel){
			case "1":try{
						return androidResponse(responseString, responseJSON, reqType, bundle);
					}catch(Exception e){
						e.printStackTrace();
					}
					break;
			case "11":try{
						//return windowsResponse(responseString, responseJSON, reqType, bundle);
					}catch(Exception e){
						e.printStackTrace();
					}
					break;
			case "3":try{
						//return webResponse(responseString, responseJSON, reqType, bundle);
					}catch(Exception e){
						e.printStackTrace();
					}
					break;
			case "5":try{
						//return iosResponse(responseString, responseJSON, reqType, bundle);
					}catch(Exception e){
						e.printStackTrace();
					}
			break;
		}
		return null;
	}

	public JSONObject androidResponse(String[] responseString,String responseJSON,String reqType,ResourceBundle bundle) throws Exception{
		JSONObject jsonObject =  new JSONObject();
		String[] responses = responseString;
		JSONObject resJSON = JSONObject.fromObject(responseJSON);
		switch(reqType){

		case "A": try{
				 	  resJSON.put("appid", responses[4]);
				 	  resJSON.put("token", responses[1]);
				 	  jsonObject.put("DHASH", generateHashString(resJSON.toString()));
				 	  jsonObject.put("inp",  toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[2]), AESCBCEncryption.base64Decode(responses[8]), resJSON.toString())));
					  jsonObject.put("pkey", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(bundle.getString("LOC.KEY")), AESCBCEncryption.base64Decode(bundle.getString("LOC.IV")), responses[2])));
					  jsonObject.put("pvoke",toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(bundle.getString("LOC.KEY")), AESCBCEncryption.base64Decode(bundle.getString("LOC.IV")), responses[8])));
					  jsonObject.put("skey", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[2]), AESCBCEncryption.base64Decode(responses[8]), responses[3])));
					  jsonObject.put("svoke", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[2]), AESCBCEncryption.base64Decode(responses[8]), responses[9])));
					  jsonObject.put("status", "S");
				}
				catch(Exception e){
					e.printStackTrace();
					throw new Exception("Unable to encode response Login A");
				}
			break;
		case "B": try{
					  resJSON.put("appid", "");
			  		  resJSON.put("token", responses[1]);
			  		  jsonObject.put("DHASH", generateHashString(resJSON.toString()));
			  		  jsonObject.put("inp", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[2]), AESCBCEncryption.base64Decode(responses[8]), resJSON.toString())));
			          jsonObject.put("pkey", "");//toHex(EnDesEncyp.encryptRequestWithSessionKey(EnDesEncyp.base64Decode(bundle.getString("LOC.KEY")), responses[2].getBytes())));
					  jsonObject.put("skey", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[2]), AESCBCEncryption.base64Decode(responses[8]), responses[3])));
					  jsonObject.put("svoke", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[2]), AESCBCEncryption.base64Decode(responses[8]), responses[9])));
					  jsonObject.put("status", "S");
				}
				catch(Exception e){
					e.printStackTrace();
					throw new Exception("Unable to encode response Login B");
				}
			break;
		case "C": try{

					  resJSON.put("appid", "");
	  		  		  resJSON.put("token", responses[1]);
	  		  		  jsonObject.put("DHASH", generateHashString(resJSON.toString()));
	  		  		  String newSessionIV = null;
	  		  		  String appId= responses[4];
	  		  		  try{
	  		  			 /* newSessionIV = AESCBCEncryption.base64Encode(AESCBCEncryption.generateIV());
	  		  			  boolean isUpdate = securityDAO.updateSessionIV(appId, newSessionIV);
			  			  if(!isUpdate){
			  				  newSessionIV = responses[7];
			  			  }*/
	  		  			newSessionIV = responses[7];

	  		  		  }
	  		  		  catch(Exception e){
	  		  			  e.printStackTrace();
	  		  			  logger.error("Error while updating siv..:"+e.getLocalizedMessage());
	  		  		  }

		  		      jsonObject.put("svoke", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[2]), AESCBCEncryption.base64Decode(responses[8]), newSessionIV)));
	  		  		  jsonObject.put("inp", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(responses[3]), AESCBCEncryption.base64Decode(newSessionIV), resJSON.toString())));
	  		  		  jsonObject.put("status", "S");

				}
				catch(Exception e){
					e.printStackTrace();
					throw new Exception("Unable to encrypt for transaction response.");
				}
			break;
		case "D": try{
	  		  		  jsonObject.put("DHASH", generateHashString(resJSON.toString()));
	  		  		  jsonObject.put("inp", toHex(AESCBCEncryption.encrypt(AESCBCEncryption.base64Decode(bundle.getString("LOC.KEY")), AESCBCEncryption.base64Decode(bundle.getString("LOC.IV")), resJSON.toString())));
					}
					catch(Exception e){
						e.printStackTrace();
						throw new Exception("Unable to encode response D.");
					}
	  		  		  break;

		}
		return jsonObject;
	}

	 public JSONObject getJsonObject(Object obj){

		  ObjectMapper mapper = new ObjectMapper();
		  JSONObject jsonObject= null;
		  try {
		   String json = mapper.writeValueAsString(obj);
		   jsonObject = JSONObject.fromObject(json);
		  } catch (JSONException | IOException e) {
		   e.printStackTrace();
		  }
		  return jsonObject;
		 }

	public SecurityDAOImpl getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAOImpl securityDAO) {
		this.securityDAO = securityDAO;
	}
	
}
