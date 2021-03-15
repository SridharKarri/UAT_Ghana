package com.ceva.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ceva.bank.common.beans.OtpBean;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.OTPGetService;
import com.nbk.util.CommonUtil;

@RestController
@Scope("prototype")
public class OTPController {

	@Autowired
	private OTPGetService otpGetService;
	
	private Logger log = Logger.getLogger(OTPController.class);
	
	@RequestMapping(value = "/core/getOTP.action/{channel}/{userId}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	public ResponseEntity<?> getUserOTP(@Valid OtpBean bean,BindingResult bindingResult) {
		log.info("Getting otp .." + bean.toString());
		log.debug("Validation Result.." + bindingResult);
		
		ResponseDTO dto = null;
		if(!bindingResult.hasErrors()){
			try{
				dto =  otpGetService.getOtp(bean);
				log.info(dto.toString());
				return new ResponseEntity<>(dto, HttpStatus.OK);
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
	}	
}