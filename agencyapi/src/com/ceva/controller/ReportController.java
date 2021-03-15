package com.ceva.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceva.bank.common.beans.BillersBean;
import com.ceva.bank.common.beans.OtpBean;
import com.ceva.bank.common.beans.TransactionReport;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.OtpService;
import com.ceva.service.ReportService;
import com.nbk.util.CommonUtil;
import com.nbk.util.Constants;

@RestController
@Scope("prototype")
public class ReportController {
	Logger log = Logger.getLogger(ReportController.class);

	@Autowired
	OtpService otpService;

	@Autowired
	ReportService reportService;
	
	@RequestMapping(value = "/echo/{channel}", method = {RequestMethod.POST, RequestMethod.GET}, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	public ResponseEntity<?> echo(@PathVariable String channel) {
		log.info("echo " + channel);
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage(Constants.SUCESS_SMALL);
		responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/otp/generateotp.action/{channel}/{userId}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	public ResponseEntity<?> generateOtp(@Valid OtpBean bean,
			BindingResult bindingResult) {
		log.info("Generating otp .." + bean.toString());
		log.debug("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				log.debug("Generating otp .. passed Validation");
				bean.setTxncode(TransactionCodes.REGOTP.toString());
				responseDTO = otpService.sendOtp(bean);
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.debug("Generating otp.. failed Validation");
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

	@RequestMapping(value = "/otp/generatecustomerotp.action/{channel}/{userId}/{merchantId}/{mobileNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	public ResponseEntity<?> generateCustomerOtp(@Valid OtpBean bean,
			BindingResult bindingResult) {
		log.info("generateCustomerOtp .." + bean.toString());
		log.debug("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				log.debug("Generating otp .. passed Validation");
				bean.setTxncode(TransactionCodes.ACTOTP.toString());
				responseDTO = otpService.sendOtp(bean);
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.debug("Generating otp.. failed Validation");
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

	@RequestMapping(value = "/otp/resendotp.action", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	public ResponseEntity<?> resendOtp(@Valid @RequestBody BillersBean bean,
			BindingResult bindingResult) {
		log.info("Fetching Billers.." + bean.toString());
		log.debug("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				log.debug("Fetching Billers.. passed Validation");
				// responseDTO = quicktellerService.getBillers();
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.debug("Fetching Billers.. failed Validation");
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

	@RequestMapping(value = "/report/genrpt.action/{channel}/{userId}/{reportType}/{fromDate}/{toDate}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	public ResponseEntity<?> generateReport(@Valid TransactionReport bean,
			BindingResult bindingResult) {
		log.info("generateReport.." + bean.toString());
		log.debug("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				log.debug("generateReport.. passed Validation");
				responseDTO = reportService.generate(bean);
				//log.debug(responseDTO.toString());
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.debug("generateReport.. failed Validation");
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

	@RequestMapping(value = "/report/inbox.action/{channel}/{userId}/{reportType}/{fromDate}/{toDate}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	public ResponseEntity<?> inbox(@Valid TransactionReport bean,
			BindingResult bindingResult) {
		log.info("generateReport.." + bean.toString());
		log.debug("Validation Result.." + bindingResult);
		ResponseDTO responseDTO = null;
		try {
			if (!bindingResult.hasErrors()) {
				log.debug("generateReport.. passed Validation");
				responseDTO = reportService.generate(bean);
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			} else {
				log.debug("generateReport.. failed Validation");
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

	@RequestMapping(value = "/core/states.action/{channel}/{userId}/{merchantId}/{mobileNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadStates() {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = otpService.listStates();
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			return new ResponseEntity<>("Internal System Error." + e.getMessage(), HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/core/cities.action/{channel}/{userId}/{mobileNumber}/{stateid}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadCities(@PathVariable String stateid) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = otpService.listCities(stateid);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			return new ResponseEntity<>(
					"Internal System Error." + e.getMessage(), HttpStatus.OK);
		}

	}
}
