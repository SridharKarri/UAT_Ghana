package com.ceva.security;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;

import com.ceva.bank.common.dao.impl.SecurityDAOImpl;
import com.nbk.util.CommonUtil;


public class IOSChannelOperator
{
	ResourceBundle bundle = null;
	private static Logger logger = Logger.getLogger(IOSChannelOperator.class);
	SecurityDAOImpl dao = null;
	HttpServletRequest enhancedRequest=null;


	public IOSChannelOperator(){

	}

	public IOSChannelOperator(ResourceBundle bundle,SecurityDAOImpl dao){
		this.bundle = bundle;
		this.dao = dao;
	}

	public HttpServletRequest buildServletRequest(Map<Inputs,String> inputMap,HttpServletRequest request,String queryString,StringBuffer requestURL,Boolean isResourse,String endPoint){//Map<String,String> params
		URLRequestWrapper requestWrapper=null;
		try
		{

			logger.info("Query String : "+queryString);


			StringBuffer newURL = new StringBuffer();
			newURL.append(inputMap.get(Inputs.PROTOCOL)+"/"+inputMap.get(Inputs.BLANK)+"/"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+inputMap.get(Inputs.SERVICE_TYPE)+"/"+inputMap.get(Inputs.MAIN_PATH)+"/"+endPoint+"/"+queryString);//
			logger.info(inputMap.get(Inputs.PROTOCOL)+"/"+inputMap.get(Inputs.BLANK)+"\n"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"\n"+inputMap.get(Inputs.SERVICE_TYPE)+"\n"+inputMap.get(Inputs.MAIN_PATH)+"\n"+endPoint+"\n"+queryString);
			requestWrapper = new URLRequestWrapper(request, queryString, newURL,request.getContextPath()+"/"+inputMap.get(Inputs.SERVICE_TYPE)+"/"+inputMap.get(Inputs.MAIN_PATH)+"/"+endPoint+"/"+queryString);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return requestWrapper;
	}

	public HttpServletRequest buildErrorMap(HttpServletRequest req,Map<Inputs,String> map,String message,String channel){
		map.put(Inputs.ENDPOINT, "Throwerr");
		String path=message;
		HttpServletRequest enhancedHttpRequest = (HttpServletRequest)buildServletRequest(map,req, path,null,false,"Throwerr");
		return enhancedHttpRequest;
	}



	public static void main(String args[]) throws UnsupportedEncodingException, DecoderException, Exception{
		String s = "6465333964676C70592B515436486D786C75424B41513D3D";

		IOSEnDesEncyp crypto = new IOSEnDesEncyp();
		String result = crypto.decrypt(CommonUtil.toString(s), crypto.generateMasterKey("AeH6GrLRGK2SBtNi".getBytes()));
		logger.info(result);
	}

}
