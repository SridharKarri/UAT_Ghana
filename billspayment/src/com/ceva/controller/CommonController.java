package com.ceva.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.dto.ResponseDTO;
import com.nbk.util.Constants;

@Controller
@Scope("request")
public class CommonController {

	private Logger log = Logger.getLogger(CommonController.class);


	@RequestMapping(value = "/echo", method = RequestMethod.GET, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> commonRequest(final HttpServletRequest request) {
		try {

			log.debug("************************************************** Starts");
			ResponseDTO responseDTO = new ResponseDTO(Constants.SUCESS_SMALL, Constants.SUCCESS_RESP_CODE);
			
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);

		} catch (Exception e) {
			log.debug(" The exception is ::::: " + e.getMessage());
			e.printStackTrace();

		} 
		return null;
	}
	@RequestMapping(value = "/reg/errorPage/{message}", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT}, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> errorRequest(final HttpServletRequest request, @PathVariable String message) {
		ResponseDTO responseDTO = null;
		try {

			log.debug("************************************************** Starts");

			responseDTO =new ResponseDTO();
			responseDTO.setMessage(message);
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
			log.info(responseDTO.toString());
		} catch (NullPointerException e) {
			log.debug(" NullPointer Exception is ::::: " + e.getMessage());
			e.printStackTrace();

		} catch (Exception e) {
			log.debug(" The exception is ::::: " + e.getMessage());
			e.printStackTrace();

		} finally {

		}
		return new ResponseEntity(responseDTO, HttpStatus.OK);
	}

}
