package com.ceva.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.bank.common.beans.MiniStatement;
import com.ceva.bank.common.beans.UserLocation;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.impl.CoreService;
import com.nbk.util.CommonUtil;

@Controller
@Scope("prototype")
public class CoreController {

	@Autowired
	private CoreService coreService;
	
	private Logger log = Logger.getLogger(CommonController.class);

	@RequestMapping(value = "/core/balenquirey.action/{channel}/{userId}/{merchantId}/{mobileNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> balanceEnquirey(@Valid BalanceEnquirey bean, BindingResult bindingResult) {
		log.debug(bindingResult);
		log.info(bean.toString());
		ResponseDTO dto = null;
		if(!bindingResult.hasErrors()){
			try{
				dto =  coreService.balanceEnquirey(bean);
				return new ResponseEntity<>(dto, HttpStatus.OK);
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/core/queryacct.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{accountNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> queryAccount(@Valid BalanceEnquirey bean, BindingResult bindingResult) {
		log.debug(bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		if(!bindingResult.hasErrors()){
			try{
				responseDTO = coreService.queryAccount(bean);
				if(responseDTO !=null){
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}else{
					return new ResponseEntity<>("External System Error.", HttpStatus.OK);
				}
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/core/location.action/{channel}/{userId}/{latitude}/{longitude}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> userLocation(@Valid UserLocation bean) {
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		try {
			responseDTO = coreService.updateUserLocation(bean);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			return new ResponseEntity<>("Internal System Error." + e.getMessage(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/core/stmt.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{fromDate}/{endDate}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> miniStatement(@Valid MiniStatement bean, BindingResult bindingResult) {
		log.debug(bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		if(!bindingResult.hasErrors()){
			try{
				responseDTO = coreService.miniStatement(bean);
				if(responseDTO != null){
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}else{
					return new ResponseEntity<>("External System Error.", HttpStatus.OK);
				}
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}

		}else{
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}
	}
}
