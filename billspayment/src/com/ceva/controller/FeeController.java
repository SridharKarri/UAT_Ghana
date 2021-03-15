package com.ceva.controller;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceva.dto.ResponseDTO;
import com.ceva.service.FeeService;
import com.nbk.util.Constants;

@RestController
public class FeeController {

	private Logger log = Logger.getLogger(FeeController.class);

	@Autowired
	FeeService feeService;

	@RequestMapping(value = "/fee/getfee.action/{channel}/{userId}/{merchantId}/{serviceCode}/{amount}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadFee(@PathVariable String userId,
			@PathVariable String merchantId, @PathVariable String serviceCode,
			@PathVariable String amount) {
		log.info("getFee userId.." + userId);
		log.info("getFee merchantId.." + merchantId);
		log.info("getFee serviceCode.." + serviceCode);
		ResponseDTO responseDTO = null;
		try {
			log.info("getFee.. ");
			responseDTO = feeService.getFee(serviceCode, merchantId,
					new BigDecimal(amount), userId);
			if(responseDTO != null){
				//responseDTO.setMessage(Constants.SUCESS_SMALL);
				//responseDTO.setResponseCode(Constants.SUCCESS_RESP_CODE);
				log.info(responseDTO.toString());
				//responseDTO.setFee(fee.getFeeVal());
			}else{
				log.info("internal error..:");
				responseDTO =  new ResponseDTO();
				responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
				responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
			}
			log.info(responseDTO.toString());
			return new ResponseEntity(responseDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			e.printStackTrace();
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
}
