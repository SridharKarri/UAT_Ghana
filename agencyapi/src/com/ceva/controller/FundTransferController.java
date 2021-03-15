package com.ceva.controller;

import java.util.Date;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceva.bank.common.beans.NameEnquirey;
import com.ceva.bank.common.beans.QueryPayment;
import com.ceva.bank.common.beans.TransferRequest;
import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.TransactionDao;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.FundTransferService;
import com.nbk.util.CommonUtil;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@RestController
@Scope("prototype")
public class FundTransferController {

	@Autowired
	private FundTransferService fundTransferService;

	@Autowired
	TransactionDao transactionDao;

	@Autowired
	LoginDao loginDao;

	private Logger log = Logger.getLogger(FundTransferController.class);

	@RequestMapping(value = "/transfer/getbanks.action/{userid}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadBanks() {
		log.info("Fetching Banks..");
		ResponseDTO responseDTO = new ResponseDTO();
		String bankCode = null;
		try{
			bankCode = ConfigLoader.convertResourceBundleToMap("config").get("bank.load.banks");
			responseDTO.setData(transactionDao.getBanks("2"));
			//responseDTO.setData(fundTransferService.getBanks().getData());
			responseDTO.setMessage(Constants.SUCESS_SMALL);
			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
		}catch(Exception e){
			responseDTO.setMessage(Constants.ERROR_MESSAGE_VALUE);
			responseDTO.setResponseCode(Constants.ERROR_CODE_NUMBER);
		}
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/transfer/getwallets.action/{userid}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadWallets() {
		ResponseDTO responseDTO = new ResponseDTO();
		String walletCode = null;
		try{
			walletCode = ConfigLoader.convertResourceBundleToMap("config").get("bank.load.wallets");
			//responseDTO.setData(transactionDao.getBanks("10"));
			responseDTO.setData(transactionDao.getBanks(walletCode));
			responseDTO.setMessage(Constants.SUCESS_SMALL);
			responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
		}catch(Exception e){
			responseDTO.setMessage(Constants.ERROR_MESSAGE_VALUE);
			responseDTO.setResponseCode(Constants.ERROR_CODE_NUMBER);
		}
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/transfer/nameenq.action/{channel}/{userId}/{bankCode}/{accountNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> nameEnquiry(@Valid NameEnquirey enquirey, BindingResult bindingResult) {
		log.debug("bean Validation..:"+bindingResult);
		log.info(enquirey.toString());
		ResponseDTO responseDTO = null;
		if(!bindingResult.hasErrors()){
			try{
				log.debug("calling service.");
				if("0".equals(enquirey.getBankCode())){
					enquirey.setBankCode("");
					responseDTO = fundTransferService.nameEnquirey(enquirey);
				}else{
					responseDTO = fundTransferService.nameEnquireyOtherBank(enquirey);
				}
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}finally{
				enquirey = null;
			}
		}else{
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/transfer/interbank.action/{channel}/{makerId}/{amount}/{bankCode}/{beneficiaryAccount}/{beneficiaryName}/{narration}/{pin}",
			method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> processInterBank(
			@Valid TransferRequest transferRequest, @PathVariable String pin,
			BindingResult bindingResult) {
		log.debug("bean Validation..:" + bindingResult);
		log.info(transferRequest.toString());
		ResponseDTO responseDTO = null;

		if(transferRequest.getBankCode() == null ){
			log.debug("bankCode is null..:");
			bindingResult.addError(new ObjectError("bankCode", "This Field Is Required."));
		}
		if(pin == null){
			log.debug("pin is null..:");
			bindingResult.addError(new ObjectError("pin", "Please Enter Pin."));
		}
		if (!bindingResult.hasErrors()) {
			try{
				responseDTO = validateUser(transferRequest, pin);
				if(Constants.SUCCESS_RESP_CODE.equals(responseDTO.getResponseCode())){
					UserLoginCredentials credentials = (UserLoginCredentials) responseDTO.getData();
					transferRequest.setMobileNumber(credentials.getMobileNo());
					transferRequest.setTxnCode(TransactionCodes.FTINTERBANK.toString());
					transferRequest.setDateTime(new Date()+"");
					responseDTO = fundTransferService.processInterBank(transferRequest);
					log.info("response ...:"+responseDTO);
					return new ResponseEntity<>(responseDTO,HttpStatus.OK);
				}else{
					return new ResponseEntity<>(responseDTO,HttpStatus.OK);
				}
			}catch(Exception e){
				log.error("Error..:"+e.getLocalizedMessage());
				return new ResponseEntity<>("Internal System Error."+e.getMessage(), HttpStatus.OK);
			}
		}else{
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult),HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/transfer/intrabank.action/{channel}/{makerId}/{serviceId}/{amount}/{beneficiaryAccount}/{beneficiaryName}/{narration}/{pin}", 
			method = RequestMethod.POST, produces = {"application/json ; charset=utf-8", "application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> processIntraBank(
			@Valid TransferRequest transferRequest,
			BindingResult bindingResult, @PathVariable String pin) {
		log.debug("bean Validation..:" + bindingResult);
		log.info(transferRequest.toString());
		ResponseDTO responseDTO = null;
		if(pin == null){
			bindingResult.addError(new ObjectError("pin", "Please Enter Pin."));
		}
		if (!bindingResult.hasErrors()) {
			try{
				transferRequest.setBankCode(ConfigLoader.convertResourceBundleToMap("config").get("fin.homebank.code"));
				if("2".equals(transferRequest.getServiceId())){
					transferRequest.setTxnCode(TransactionCodes.CASHDEP.toString());
				}else{
					transferRequest.setTxnCode(TransactionCodes.FTINTRABANK.toString());
				}
				transferRequest.setDateTime(new Date()+"");
				responseDTO = validateUser(transferRequest, pin);
				if(Constants.SUCCESS_RESP_CODE.equals(responseDTO.getResponseCode())){
					UserLoginCredentials credentials = (UserLoginCredentials) responseDTO.getData();
					transferRequest.setMobileNumber(credentials.getMobileNo());
					responseDTO = fundTransferService.processIntraBank(transferRequest);
					log.info("response ...:"+responseDTO);
					return new ResponseEntity<>(responseDTO,HttpStatus.OK);
				}else{
					return new ResponseEntity<>(responseDTO,HttpStatus.OK);
				}
			}catch(Exception e){
				responseDTO =new ResponseDTO();
				responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
				responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
				log.error("Error..:"+e.getLocalizedMessage());
			}
			return new ResponseEntity<>(responseDTO,HttpStatus.OK);
		}else{
			log.debug("Bean Validation Failed....");
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult),HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/transfer/txnenquirey.action/{channel}/{makerId}/{refNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> transactionStatusQuery(
			@Valid QueryPayment transfer,
			BindingResult bindingResult) {
		log.debug("bean Validation..:" + bindingResult);
		log.info(transfer.toString());
		ResponseDTO responseDTO = null;
		if (!bindingResult.hasErrors()) {
			responseDTO = fundTransferService.transactionStatusQuery(transfer);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(CommonUtil.getErrorsInASaneFormat(bindingResult),HttpStatus.BAD_REQUEST);
		}

	}

	private ResponseDTO validateUser(TransferRequest transferRequest, String pin) {
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
		}
		return dto;
	}

}
