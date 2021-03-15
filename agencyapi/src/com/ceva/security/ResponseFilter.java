package com.ceva.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ceva.bank.common.dao.impl.SecurityDAOImpl;
import com.ceva.db.model.ChannelUsers;
import com.ceva.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbk.util.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

//@Component("responseFilter")
public class ResponseFilter implements Filter,Modes {

	private ServletContext context=null;
	private SecurityDAOImpl securityDAO;
	private SecurityUtil securityUtil;
	private HttpSession session =null;
	private static Logger logger = Logger.getLogger(ResponseFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("INIT AFTER PROCESS");
		this.context = filterConfig.getServletContext();
		this.context.log("RequestLoggingFilter initialized");
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.info("doFilter response");
		String secureData = null;
		ChannelUsers channelUser = null;
		ResponseDTO responseDTO = null;
		String reqType = null;
		JSONObject obj =  null;
		try{
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			HttpServletResponse httpServletResponse = (HttpServletResponse)response;
			session = httpServletRequest.getSession();
			String requestURL = httpServletRequest.getRequestURL().toString();
			logger.info(response.getContentType());
			if(requestURL.contains(Constants.EXCLUDE_END_POINTS)){
				chain.doFilter((HttpServletRequest)request, response);
			}else{
				CustomResponseWrapper responseWrapper = new CustomResponseWrapper(httpServletResponse,httpServletRequest.getSession());
				chain.doFilter(request, responseWrapper);
				
				channelUser = (ChannelUsers) session.getAttribute(Constants.CHANNEL_DATA);
				secureData = securityUtil.toString((String) session.getAttribute(Constants.SECURE_DATA));
				reqType = session.getAttribute(Constants.TXN_CODE).toString();
				responseDTO = new ObjectMapper().readValue(responseWrapper.getDataStream(), ResponseDTO.class);
				if(responseDTO!= null){
					response.reset();
					obj = constructResponse(secureData, channelUser, responseDTO, httpServletRequest, reqType);
					response.getOutputStream().write(obj.toString().getBytes());
				}else{
					logger.info("Responsedto is null...");
				}
			}
		}catch(Exception e){
			logger.error("Error while encrypting response.."+e.getLocalizedMessage());
			obj = new JSONObject();
			obj.put(Constants.MESSAGE, "User Id not found");
			obj.put(Constants.RESP_CODE, "002");
			response.getOutputStream().write(obj.toString().getBytes());
			logger.info("Reason..:"+e.getLocalizedMessage());
		}finally{
			session.invalidate();
			session=null;
			channelUser = null;
			secureData =null;
			responseDTO =null;
		}

	}
	public static byte[] serialize(ResponseDTO responseDTO) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(responseDTO);
	    return out.toByteArray();
	}
	private JSONObject constructResponse(String securityData, ChannelUsers checkbean, ResponseDTO responseDTO, HttpServletRequest request, String reqType){
		logger.debug("constructing response.");
		JSONObject object = new JSONObject();

	    String imeino = null;
	    String appid = null;
	    String sessionid = null;
	    String chanl = null;
	    String token="";
		try{
			//secureData = CommonUtil.toString(securityData);
			String []secdata =  securityData.split("\\|");
			appid = secdata[0];
			chanl = secdata[1];
			imeino = secdata[2];
		    sessionid = secdata[3];
		    try{
		    	//object =JSONObject.fromObject(responseDTO.getData());
		    	object =JSONObject.fromObject(responseDTO);
		    }catch(JSONException je){
		    	object = new JSONObject();
		    	object.put(Constants.KEY_DATA, JSONArray.fromObject(responseDTO));
		    	je.printStackTrace();
		    	logger.error("error..:"+je.getLocalizedMessage());
		    }catch(Exception e){
		    	e.printStackTrace();
		    	logger.error("error..:"+e.getLocalizedMessage());
		    }
		    String[] appidSplit = {appid,checkbean.getpToken(),checkbean.getpKey(),sessionid,checkbean.getAppId(),chanl,imeino,checkbean.getsIv(),checkbean.getpIv(),
					 checkbean.getsIv(),request.getRemoteHost(),request.getRemotePort()+""};
			object =securityUtil.buildResponseJSON(appidSplit, object.toString(), reqType);
		}catch(Exception e){
			logger.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		finally{
			imeino = null;
			appid=null;
			sessionid = null;
			chanl = null;
			securityData = null;
			checkbean = null;
			responseDTO = null;
		}
		return object;
	}

	public SecurityDAOImpl getSecurityDAO() {
		return securityDAO;
	}

	public void setSecurityDAO(SecurityDAOImpl securityDAO) {
		this.securityDAO = securityDAO;
	}

	public SecurityUtil getSecurityUtil() {
		return securityUtil;
	}
	

	public void setSecurityUtil(SecurityUtil securityUtil) {
		this.securityUtil = securityUtil;
	}
	
}
