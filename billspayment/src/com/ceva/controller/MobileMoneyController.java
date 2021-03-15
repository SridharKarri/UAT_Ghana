package com.ceva.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.bank.common.beans.BillPayment;
import com.ceva.bank.common.beans.MMOperators;
import com.ceva.bank.common.beans.MMProductFormItems;
import com.ceva.bank.common.beans.MMProducts;
import com.ceva.bank.common.beans.MMValidateRequest;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.MobileMoneyService;
import com.nbk.util.CommonUtil;
import com.nbk.util.Constants;

@Controller
@Scope("prototype")
public class MobileMoneyController {

	@Autowired
	private MobileMoneyService mobileMoneyService;

	@Autowired
	private LoginDao loginDao;

	private static Logger log = Logger.getLogger(MobileMoneyController.class);

	@RequestMapping(value = "/mobilemoney/getoperators.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> getoperators(@Valid @RequestBody MMOperators bean, BindingResult bindingResult) {
		log.info("Fetching getoperators..:"+bean.toString());
		log.info("Validation getoperators Result..:"+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = mobileMoneyService.getMMOperators(bean);
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			responseDTO = null;
		}
	}

	@RequestMapping(value = "/mobilemoney/getmobilemoneyproducts.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> getmmproducts(@Valid @RequestBody MMProducts bean, BindingResult bindingResult) {
		log.info("Fetching getmmproducts..:"+bean.toString());
		log.info("Validation getmmproducts Result..:"+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = mobileMoneyService.getMMProducts(bean);
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			responseDTO = null;
		}
	}
	
	@RequestMapping(value = "/mobilemoney/getmobilemoneyformitems.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> getmmproductformitems(@Valid @RequestBody MMProductFormItems bean, BindingResult bindingResult) {
		log.info("Fetching getmobilemoneyformitems..:"+bean.toString());
		log.info("Validation getmobilemoneyformitems Result..:"+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = mobileMoneyService.getMMProductFormItems(bean);
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			responseDTO = null;
		}
	}
	
	@RequestMapping(value = "/mobilemoney/validatemobilemoney.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> validatemobilemoney(@Valid @RequestBody MMValidateRequest bean, BindingResult bindingResult) {
		log.info("Fetching validatemobilemoney..:"+bean.toString());
		log.info("Validation validatemobilemoney Result..:"+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = mobileMoneyService.mmvalidatereq(bean);
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			responseDTO = null;
		}
	}
	
	@RequestMapping(value = "/billpayment/commitmblpayment.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> commitMblPayment(@RequestBody MMValidateRequest bean,
			BindingResult bindingResult) {
		log.info(" commitMblPayment.." + bean.toString());
		log.info("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				responseDTO = validateUser(bean.getUserId(), bean.getPin());
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					responseDTO = mobileMoneyService.commitMobilePayment(bean);
				}
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.info("Mobile Commit.. failed Validation");
				return new ResponseEntity<>(
						CommonUtil.getErrorsInASaneFormat(bindingResult),
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			bindingResult.addError(new ObjectError(
					"Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(
					CommonUtil.getErrorsInASaneFormat(bindingResult),
					HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	private ResponseDTO validateUser(String userId, String pin) {
		UserLoginCredentials userBean =null;
		ResponseDTO dto = new ResponseDTO();
		try{
			userBean = new UserLoginCredentials();
			userBean.setUserId(userId);
			userBean.setPin(pin);
			dto = loginDao.validate(userBean);
		}catch(Exception e){
			e.printStackTrace();
			dto.setMessage(Constants.INVALID_CREDENTIALS_MESSAGE);
			dto.setResponseCode(Constants.INVALID_CREDENTIALS);
			log.error("Error while validating pin, reason.."+e.getLocalizedMessage());
		}finally{
			userBean = null;
		}
		return dto;
	}
}