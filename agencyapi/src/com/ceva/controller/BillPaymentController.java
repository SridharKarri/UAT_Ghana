package com.ceva.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.bank.common.beans.BillPayment;
import com.ceva.bank.common.beans.BillePaymentItems;
import com.ceva.bank.common.beans.BillersBean;
import com.ceva.bank.common.beans.QueryPayment;
import com.ceva.bank.common.beans.Services;
import com.ceva.bank.common.beans.ValidateCustomer;
import com.ceva.bank.common.dao.BillPaymentDao;
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
	BillerService billerService;

	@Autowired
	BillPaymentDao billPaymentDao;

	@Autowired
	LoginDao loginDao;

	private Logger log = Logger.getLogger(BillPaymentController.class);

	@RequestMapping(value = "/billpayment/services.action/{channel}/{userId}/{merchantId}/{mobileNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadServices(@Valid Services bean, BindingResult bindingResult) {
		log.info("Fetching Services.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		List<Service> services = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("Fetching Services.. passed Validation.");
				bean.setServiceType("PAYBILL");
				services = billerService.getServices(bean);
				if(services != null){
					log.debug("services from servce layer.. :"+services.size());
					responseDTO.setData(services);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}else{
					log.info("Fetching services.. querying database failed.");
					responseDTO.setMessage(Constants.SEARCH_FAIL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}
				return new ResponseEntity(responseDTO, HttpStatus.OK);
			}else{
				log.info("Fetching services.. failed Validation");
				return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			bindingResult.addError(new ObjectError("Error While Processing Request", e.getLocalizedMessage()));
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}finally{
			services = null;
		}
	}

	@RequestMapping(value = "/billpayment/MMObillers.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{serviceId}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> MMObillers(@Valid BillersBean bean, BindingResult bindingResult) {
		log.info("Fetching MMObillers..:"+bean.toString());
		log.info("Validation Result..:"+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		List<Billers> billers = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("Fetching Billers.. passed Validation.");
				billers = billerService.MMObillers(bean);
				if(billers != null){
					log.debug("billers from servce layer.. "+billers.size());
					responseDTO.setData(billers);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}else{
					log.info("Fetching Billers.. resposnse is null.");
					responseDTO.setMessage(Constants.SEARCH_FAIL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}
				return new ResponseEntity(responseDTO, HttpStatus.OK);
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
	@RequestMapping(value = "/billpayment/mobileRecharge.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{id}/{sid}/{amount}/{packId}/{customerMobile}/{customerEmail}/{customerId}/{paymentCode}/{pin}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> mobileRecharge(@Valid BillPayment bean, @PathVariable String pin, BindingResult bindingResult) {
		log.info("mobileRecharge..:"+bean.toString());
		log.info("Validation mobileRecharge Result..:"+bindingResult);
		ResponseDTO responseDTO = null;;
		Billers biller = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("pin validation started..");
				responseDTO = validateUser(bean, pin);
				log.info("pin validation end..");
				log.info("Fetching Billers.. passed Validation.");
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					//biller=billPaymentDao.getBiller(bean.getId());
					/*if("0".equals(bean.getPackId())){
						bean.setPackId("01");
					}
					if("0".equals(bean.getPaymentCode())){
						bean.setPackId(biller.getBillerId()+"01");
					}*/
					responseDTO = billerService.mobileRecharge(bean);
					log.info(responseDTO.toString());
					if (responseDTO != null) {
						return new ResponseEntity<>(responseDTO, HttpStatus.OK);
					}
				}else{
					log.info("pin validation failed..");
					log.info(responseDTO.toString());
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}
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
		}
	}

	@RequestMapping(value = "/billpayment/getbillers.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{serviceId}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadBillers(@Valid BillersBean bean, BindingResult bindingResult) {
		log.info("Fetching Billers.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		ResponseDTO responseDTO = new ResponseDTO();
		List<Billers> billers = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("Fetching Billers.. passed Validation.");
				billers = billerService.getBillers(bean);
				if(billers != null){
					log.debug("billers from servce layer.. "+billers.size());
					responseDTO.setData(billers);
					responseDTO.setMessage(Constants.SUCESS_SMALL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}else{
					log.info("Fetching Billers.. passed Validation");
					responseDTO.setMessage(Constants.SEARCH_FAIL);
					responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				}
				return new ResponseEntity(responseDTO, HttpStatus.OK);
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

	@RequestMapping(value = "/billpayment/billpaymentitems.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{id}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadBillPaymentItems(@Valid BillePaymentItems bean, BindingResult bindingResult) {
		log.info("Fetching BillPaymentItems..");
		log.info("Validation Result.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				log.info("getbillPaymentItems.. passed Validation");
				responseDTO = billerService.getBillerPackages(bean);
				log.info(responseDTO.toString());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				log.info("getbillPaymentItems.. failed Validation");
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
	
	@RequestMapping(value = "/billpayment/lpam.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{id}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> getPaymentItemsAndMarkets(@Valid BillePaymentItems bean, BindingResult bindingResult) {
		log.info("Fetching getPaymentItemsAndMarkets..");
		log.debug("Validation Result.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = new ResponseDTO();
		try{
			if(!bindingResult.hasErrors()){
				log.debug("getPaymentItemsAndMarkets.. passed Validation");
				responseDTO = billerService.getPaymentItemsAndMarkets(bean);
				log.info(responseDTO.toString());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}else{
				log.info("getbillPaymentItems.. failed Validation");
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


	@RequestMapping(value = "/billpayment/dobillpayment.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{id}/{sid}/{amount}/{packId}/{customerMobile}/{customerEmail}/{customerId}/{paymentCode}/{pin}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> billPayment(@Valid BillPayment bean, @PathVariable String pin,
			BindingResult bindingResult) {
		log.info(" billPayment.." + bean.toString());
		log.info("Validation Result.." + bindingResult);
		log.info(bean.toString());
		Billers biller = null;
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				log.info("billPayment.. passed Validation");

				log.info("pin validation started..");
				responseDTO = validateUser(bean, pin);
				log.info("billPayment.. passed Validation.");
				if(Constants.SUCESS_SMALL.equals(responseDTO.getMessage())){
					/*biller=billPaymentDao.getBiller(bean.getId());
					bean.setPackId(biller.getBillerId()+"01");*/
					/*if("0".equals(bean.getPackId())){
						log.info("Setting default pack id.");
						bean.setPackId(biller.getBillerId()+"01");
					}*/
					responseDTO = billerService.payBill(bean);
					if (responseDTO != null) {
						log.info(responseDTO.toString());
						//return new ResponseEntity<>(responseDTO, HttpStatus.OK);
					}
				}else{
					log.info("pin validation failed..");
					log.info(responseDTO.toString());
					//return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}
				return new ResponseEntity(responseDTO, HttpStatus.OK);
			} else {
				log.info("billPayment.. failed Validation");
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

		}
	}

	@RequestMapping(value = "/billpayment/validateCustomer.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{billerId}/{customerId}/{paymentCode}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> validateCustomer(@Valid ValidateCustomer bean, BindingResult bindingResult) {
		log.info("validateCustomer.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				responseDTO = billerService.validateCustomer(bean);
				if(responseDTO != null){
					log.info(responseDTO.toString());
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}else{
					responseDTO = new ResponseDTO();
					responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
					responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
					return new ResponseEntity<>(responseDTO, HttpStatus.OK);
				}
			}else{
				log.info("validateCustomer.. failed Validation");
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
	@RequestMapping(value = "/billpayment/querypayment.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> queryPayment(@Valid @RequestBody QueryPayment bean, BindingResult bindingResult) {
		log.info("queryPayment.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){/*
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
				return new ResponseEntity("Internal Error", HttpStatus.OK);
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

	private ResponseDTO validateUser(BillPayment bean, String pin) {
		log.info("validateUser..:");
		UserLoginCredentials userBean =null;
		ResponseDTO dto = new ResponseDTO();
		try{
			userBean = new UserLoginCredentials();
			userBean.setUserId(bean.getUserId());
			userBean.setPin(pin);
			userBean.setMobileNo(bean.getMobileNumber());
			userBean.setAgent(bean.getMerchantId());
			dto = loginDao.validate(userBean);
		}catch(Exception e){
			e.printStackTrace();
			dto.setMessage(Constants.INVALID_CREDENTIALS_MESSAGE);
			dto.setResponseCode(Constants.INVALID_CREDENTIALS);
			log.error("Error while validating pin, reason.."+e.getLocalizedMessage());
		}finally{
			userBean = null;
			bean=null;
		}
		return dto;
	}

}
