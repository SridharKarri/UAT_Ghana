package com.ceva.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceva.bank.common.beans.AppVersion;
import com.ceva.bank.common.beans.ChangePin;
import com.ceva.bank.common.beans.DeviceRegister;
import com.ceva.bank.common.dao.impl.SecurityDAOImpl;
import com.ceva.db.model.ChannelUsers;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.LoginService;
import com.nbk.util.CommonUtil;
import com.nbk.util.Constants;

@RestController
public class LoginController {

	Logger log = Logger.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;

	@Autowired
	SecurityDAOImpl securityDAO;

	@RequestMapping(value = "/login/login.action/{channel}/{userId}/{pin}/{mobileNo}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> userLogin(@Valid UserLoginCredentials bean, @PathVariable String channel, BindingResult bindingResult,HttpServletRequest requestContext) {
		log.debug("userLogin.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				log.debug("userLogin.. passed Validation");
				responseDTO = loginService.validate(bean);
				log.info(responseDTO.toString());
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					log.info("userLogin..:SUCCESS");
					//log.info(responseDTO.toString());
				}
				return new ResponseEntity(responseDTO, HttpStatus.OK);
			}else{
				log.info("userLogin.. failed Validation");
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			bean = null;
		}
	}
	@RequestMapping(value = "/login/changepin.action/{channel}/{userId}/{oldPin}/{newPin}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> changePin(@Valid ChangePin bean, BindingResult bindingResult) {
		log.info("changePin.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				if(!"".equals(bean.getNewPin()) && bean.getNewPin().length()>0)
					responseDTO = loginService.changePwd(bean);
				else
					responseDTO = new ResponseDTO("New pin should not be null.", Constants.FAILURE_RESP_CODE);
					
				log.info(responseDTO.toString());
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					log.debug("Pin change success.");
					log.debug(responseDTO.toString());
				}
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				log.info("changePin.. failed Validation");
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			bean = null;
		}
	}

	@RequestMapping(value = "/login/updateProfile.action/{channel}/{userId}/{merchantId}/{mobileNo}/{oldPin}/{newPin}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> updateProfile(@Valid UserLoginCredentials bean, BindingResult bindingResult) {
		log.info("updateProfile.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = loginService.updateProfile(bean);
				log.info(responseDTO.toString());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				log.info("updateProfile.. failed Validation");
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			bean = null;
		}
	}


	@RequestMapping(value = "/reg/devReg.action/{channel}/{userId}/{otp}/{pin}/{secans1}/{secans2}/{secans3}/{macAddr}/{deviceIp}/{imeiNo}/{serialNo}/{version}/{deviceType}/{gcmId}/{language}",
	//@RequestMapping(value = "/reg/devReg.action/{channel}/{userId}/{otp}/{pin}/{secans1}/{secans2}/{macAddr}/{deviceIp}/{imeiNo}/{serialNo}/{version}/{deviceType}/{gcmId}",
			method = RequestMethod.POST, produces = {"application/json ; charset=utf-8","application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> registerDevice(@Valid DeviceRegister bean,
			BindingResult bindingResult, HttpSession session) {
		log.debug("registerDevice.." + bean.toString());
		log.info("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				log.info("registerDevice.. passed Validation");
				responseDTO = loginService.registerDevice(bean);
				log.info(responseDTO.toString());
				if (Constants.SUCESS_SMALL.equals(responseDTO.getMessage())) {
					ChannelUsers channelUser = securityDAO.prepareAppLogin(bean.getUserId(), bean.getImeiNo(), "APP", bean.getSerialNo(), bean.getMacAddr(), bean.getVersion(), bean.getDeviceIp(), bean.getLanguage());
					String s = channelUser.getAppId()+"|"+bean.getChannel()+"|"+channelUser.getImeiNo()+"|"+channelUser.getSessionId();
					session.setAttribute(Constants.SECURE_DATA, CommonUtil.toHex(s));
					session.setAttribute(Constants.CHANNEL_DATA, channelUser);
					session.setAttribute(Constants.TXN_CODE, "A");
					log.info("AppId For user..:"+bean.getUserId()+", is..:"+channelUser.getAppId());
					//log.info(responseDTO.toString());
				}
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.info("registerDevice.. failed Validation");
				return new ResponseEntity<>(
						CommonUtil.getErrorsInASaneFormat(bindingResult),
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError(
					"Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(
					CommonUtil.getErrorsInASaneFormat(bindingResult),
					HttpStatus.BAD_REQUEST);
		} finally {
			bean = null;
		}
	}
	
	@RequestMapping(value = "/reg/appVersion.action/{channel}/{userId}/{appId}/{version}",
			method = RequestMethod.POST, produces = {"application/json ; charset=utf-8","application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> appVersion(AppVersion bean) {
		log.debug("appVersion.." + bean.toString());
		ResponseDTO responseDTO = new ResponseDTO();
		try {
				boolean updated= securityDAO.updateAppVersion(bean.getAppId(), bean.getVersion());
				if(updated){
					com.ceva.db.model.AppVersion appVersion = securityDAO.fetchMinAppVersion(bean.getChannel());
					Map<String, String> data = new HashMap<String, String>();
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
					data.put("minVersion", appVersion.getMinVersion());
					responseDTO.setData(data);
				}else{
					responseDTO.setMessage("Unable to update app version.");
					responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
					log.info("Failed To update app version please try again");
				}
				log.info(responseDTO.toString());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			e.printStackTrace();
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		} finally {
			bean = null;
		}
	}
	
	@RequestMapping(value = "/reg/getnewuserid.action/{channel}/{userId}",
			method = RequestMethod.POST, produces = {"application/json ; charset=utf-8","application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> getNewUserId(@PathVariable("userId") String userId) {
		log.debug("getNewUserId.." + userId);
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			responseDTO = loginService.loadUser(userId);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			e.printStackTrace();
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		} 
	}
	
	@RequestMapping(value = "/login/logout.action/{channel}/{userId}/{appId}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> logout(@PathVariable String userId, @PathVariable String channel, @PathVariable String appId) {
		log.info("logout mobileNo..:"+userId);
		ResponseDTO responseDTO = new ResponseDTO();
		try{
				log.info("logout.. passed Validation");
				/*ChannelUsers channelUsers = securityDAO.fetchSecurityData(appId);
				channelUsers.setLastRequestTime(null);
				securityDAO.updateChannelUser(channelUsers);*/
				securityDAO.updateChannelUser(appId);
				responseDTO.setMessage(Constants.SUCESS_SMALL);
				responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			responseDTO.setMessage(e.getLocalizedMessage());
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
		}finally{
		}
		log.info(responseDTO.toString());
		return new ResponseEntity(responseDTO, HttpStatus.OK);
	}
	public SecurityDAOImpl getSecurityDAO() {
		return securityDAO;
	}
	public void setSecurityDAO(SecurityDAOImpl securityDAO) {
		this.securityDAO = securityDAO;
	}
	
}
