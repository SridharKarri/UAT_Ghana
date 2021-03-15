package com.ceva.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.bank.common.beans.CashWithdrawalConfirm;
import com.ceva.bank.common.beans.CashWithdrawalInit;
import com.ceva.bank.common.beans.TransferRequest;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.CashWithdrawalService;
import com.nbk.util.CommonUtil;
import com.nbk.util.Constants;

@Controller
public class CashWithdrawalController {

	private Logger log = Logger.getLogger(CashWithdrawalController.class);

	@Autowired
	CashWithdrawalService cashWithdrawalService;

	@Autowired
	LoginDao loginDao;

	@RequestMapping(value = "/withdrawal/cashbyaccountinit.action/{channel}/{makerId}/{customerAccountNumber}/{customerName}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> cashByAccountInit(
			@Valid CashWithdrawalInit withdrawalInit,	BindingResult bindingResult) {
		log.debug("bean Validation..:" + bindingResult);
		log.info(withdrawalInit.toString());
		ResponseDTO responseDTO = null;

		if (!bindingResult.hasErrors()) {
			try{
				log.debug("validating limits... START");
				responseDTO= cashWithdrawalService.cashByAccountInit(withdrawalInit);
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				log.error("Logging error for stack trace..:START");
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}
		}else{
			log.debug("Bean Validation Failed....");
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/withdrawal/cashbyaccountconfirm.action/{channel}/{makerId}/{amount}/{txnRef}/{customerAccountNumber}/{customerName}/{narration}/{otp}/{pin}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> cashByAccountConfirm(
			@Valid CashWithdrawalConfirm withdrawalConfirm,
			BindingResult bindingResult) {
		log.debug("bean Validation..:" + bindingResult);
		log.info(withdrawalConfirm.toString());
		ResponseDTO responseDTO = null;

		if (!bindingResult.hasErrors()) {
			try{
				log.debug("validating user START.");
				responseDTO = validateUser(withdrawalConfirm, withdrawalConfirm.getPin());
				log.debug("validating user END.");
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					log.info("validating limits... START");
					responseDTO= cashWithdrawalService.cashByAccountConfirm(withdrawalConfirm);
				}else{
					log.info("pin validation failed. ");
				}
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				log.error("Logging error for stack trace..:START");
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}
		}else{
			log.debug("Bean Validation Failed....");
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	private ResponseDTO validateUser(CashWithdrawalConfirm transferRequest, String pin) {
		log.debug("bean Validation..:");
		UserLoginCredentials userBean =null;
		ResponseDTO dto = new ResponseDTO();
		try{
			userBean = new UserLoginCredentials();
			userBean.setUserId(transferRequest.getMakerId());
			userBean.setPin(pin);
			userBean.setMobileNo(transferRequest.getMobileNumber());
			userBean.setAgent(transferRequest.getMerchantId());
			dto = loginDao.validate(userBean);

		}catch(Exception e){
			e.printStackTrace();
			dto.setMessage(Constants.INVALID_CREDENTIALS_MESSAGE);
			dto.setResponseCode(Constants.INVALID_CREDENTIALS);
			log.error("Error while validating pin, reason.."+e.getLocalizedMessage());
		}finally{
			userBean = null;
			transferRequest=null;
		}
		return dto;
	}


}
