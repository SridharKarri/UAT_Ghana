package com.ceva.controller;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
import com.ceva.bank.common.beans.BillePaymentItems;
import com.ceva.bank.common.beans.BillerFormItems;
import com.ceva.bank.common.beans.BillersBean;
import com.ceva.bank.common.beans.QueryPayment;
import com.ceva.bank.common.beans.Services;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.db.model.Billers;
import com.ceva.db.model.Service;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.BillerService;
import com.nbk.util.CommonUtil;
import com.nbk.util.Constants;

@Controller
@Scope("prototype")
public class BillPaymentController {

	@Autowired
	private BillerService billerService;

	@Autowired
	private LoginDao loginDao;

	private static Logger log = Logger.getLogger(BillPaymentController.class);

	@RequestMapping(value = "/billpayment/services.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadServices(@Valid @RequestBody Services bean, BindingResult bindingResult) {
		log.info("Fetching Services..:"+bean.toString());
		log.info("Validation Result..:"+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		List<Service> services = null;
		try{
			if(!bindingResult.hasErrors()){
				services = billerService.getServices(bean);
				if(services != null){
					log.debug("services from servce layer.. :"+services.size());
					String language=billerService.getLanguage(bean.getUserId());
					log.debug("[loadServices] Language :"+language+", User Id : "+bean.getUserId());
					ListIterator<Service> it=services.listIterator();
					while(it.hasNext()){
						@SuppressWarnings("unchecked")
						Map<Object,Object> a=(Map<Object,Object>)it.next();
						if(language.equals("fr")){
							a.put("serviceName", a.get("frserviceName"));
							a.remove("frserviceName");
						}else{
							a.remove("frserviceName");
						}
					}
					Map<String, Object> data = new HashMap<>();
					data.put("categories", services);
					responseDTO.setData(data);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}else{
					responseDTO.setMessage(Constants.SEARCH_FAIL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}
				log.debug("[loadServices] ResponseDTO :"+responseDTO.toString()+", User Id : "+bean.getUserId());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			services = null;
		}
	}

	@RequestMapping(value = "/billpayment/MMObillers.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> MMObillers(@RequestBody @Valid BillersBean bean, BindingResult bindingResult) {
		log.info("Fetching MMObillers..:"+bean.toString());
		log.info("Validation Result..:"+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		List<Billers> billers = null;
		try{
			if(!bindingResult.hasErrors()){
				billers = billerService.MMObillers(bean);
				if(billers != null){
					String language=billerService.getLanguage(bean.getUserId());
					log.debug("[MMObillers] Language :"+language+", User Id : "+bean.getUserId());
					ListIterator<Billers> it=billers.listIterator();
					while(it.hasNext()){
						@SuppressWarnings("unchecked")
						Map<Object,Object> a=(Map<Object,Object>)it.next();
						if(language.equals("fr")){
							a.put("billerName", a.get("frbillerName"));
							a.remove("frbillerName");
						}else{
							a.remove("frbillerName");
						}
					}
					
					log.debug("billers from servce layer..:"+billers.size());
					responseDTO.setData(billers);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}else{
					responseDTO.setMessage(Constants.SEARCH_FAIL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}
				log.debug("[MMObillers] ResponseDTO :"+responseDTO.toString()+", User Id : "+bean.getUserId());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			billers = null;
		}
	}
	@RequestMapping(value = "/billpayment/mobilerecharge.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> mobileRecharge(@Valid @RequestBody BillPayment bean, BindingResult bindingResult) {
		log.info("mobileRecharge..:"+bean.toString());
		log.info("Validation mobileRecharge Result..:"+bindingResult);
		ResponseDTO responseDTO = null;;
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = validateUser(bean.getUserId(), bean.getPin());
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					responseDTO = billerService.mobileRecharge(bean);
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}else{
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
		}
	}

	@RequestMapping(value = "/billpayment/getbillers.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadBillers(@Valid @RequestBody BillersBean bean, BindingResult bindingResult) {
		log.info("Fetching Billers..:"+bean.toString());
		log.info("Validation Result..:"+bindingResult);
		
		ResponseDTO responseDTO = new ResponseDTO();
		List<Billers> billers = null;
		try{
			if(!bindingResult.hasErrors()){
				billers = billerService.getBillers(bean);
				System.out.println("Before : "+billers);
				if(billers != null){
					String language=billerService.getLanguage(bean.getUserId());
					log.debug("[loadBillers] Language :"+language+", User Id : "+bean.getUserId());
					ListIterator<Billers> it=billers.listIterator();
					while(it.hasNext()){
						@SuppressWarnings("unchecked")
						Map<Object,Object> a=(Map<Object,Object>)it.next();
						if(language.equals("fr")){
							a.put("billerName", a.get("frbillerName"));
							a.remove("frbillerName");
						}else{
							a.remove("frbillerName");
						}
					}
					responseDTO.setData(billers);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}else{
					responseDTO.setMessage(Constants.SEARCH_FAIL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}
				log.debug("[loadBillers] ResponseDTO :"+responseDTO.toString()+", User Id : "+bean.getUserId());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				log.info("Fetching Billers.. failed Validation");
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			billers = null;
		}
	}

	@RequestMapping(value = "/billpayment/billpaymentitems.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadBillPaymentItems(@Valid @RequestBody BillePaymentItems bean, BindingResult bindingResult) {
		log.info("Fetching BillPaymentItems..");
		log.info("Validation Result.."+bindingResult);
		log.info(bean.toString());
		
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = billerService.getBillerPackages(bean);
				log.info(responseDTO.toString());
				log.debug("[loadBillPaymentItems] ResponseDTO :"+responseDTO.toString()+", User Id : "+bean.getUserId());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{

		}
	}
	
	@RequestMapping(value = "/billpayment/paymentformitems.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadPaymentFormItems(@Valid @RequestBody BillerFormItems bean, BindingResult bindingResult) {
		log.info("Fetching BillPaymentItems..");
		log.info("Validation Result.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = billerService.getBillerFormItems(bean);
				log.info(responseDTO.toString());
				log.debug("[loadPaymentFormItems] ResponseDTO :"+responseDTO.toString()+", User Id : "+bean.getUserId());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{

		}
	}

	@RequestMapping(value = "/billpayment/validatepayment.action", method = RequestMethod.POST,
			produces = {"application/json ; charset=utf-8"})
	@ResponseBody
	public ResponseEntity<?> validatePayment(@Valid @RequestBody BillPayment bean,
			BindingResult bindingResult) {
		log.info("validatePayment.." + bean.toString());
		log.info("Validation Result.." + bindingResult);
		try {
			if (!bindingResult.hasErrors()) {
				return new ResponseEntity<>(billerService.validatePaybill(bean), HttpStatus.OK);
			} else {
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
	
	@RequestMapping(value = "/billpayment/confirmpayment.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> confirmPayment(@RequestBody BillPayment bean,
			BindingResult bindingResult) {
		log.info(" confirmPayment.." + bean.toString());
		log.info("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				responseDTO = validateUser(bean.getUserId(), bean.getPin());
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					responseDTO = billerService.commitPaybill(bean);
				}
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.info("billPayment.. failed Validation");
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
	@RequestMapping(value = "/billpayment/querypayment.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> queryPayment(@Valid @RequestBody QueryPayment bean, BindingResult bindingResult) {
		log.info("queryPayment..:"+bean.toString());
		log.info("Validation Result..:"+bindingResult);
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
			/*
				log.info("queryPayment.. passed Validation");
				//responseDTO = quicktellerService.queryPayment(bean.getRefNumber());
				if(responseDTO != null){
					log.info("Response from dao:"+responseDTO.toString());
					Response response = (Response) responseDTO.getData();
					if(response != null){
						return new ResponseEntity(responseDTO, HttpStatus.OK);
					}else{
						return new ResponseEntity(responseDTO, HttpStatus.OK);
					}
					//return new ResponseEntity(response, HttpStatus.OK);
				}else{
					log.info("queryPayment.. null Response From DAO.");
					return new ResponseEntity("Internal Error", HttpStatus.OK);
				}
			*/
				return new ResponseEntity<>("Internal Error", HttpStatus.OK);
			}else{
				log.info("queryPayment.. failed Validation");
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{

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
