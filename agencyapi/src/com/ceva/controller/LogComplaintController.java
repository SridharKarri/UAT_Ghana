package com.ceva.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.bank.common.beans.LogComplaint;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.LogComplaintService;

@Controller
@Scope("prototype")
public class LogComplaintController {
	private Logger log = Logger.getLogger(LogComplaintController.class);

	@Autowired
	LogComplaintService logComplaintService;

	@RequestMapping(value = "/complaint/log.action/{channel}/{userId}/{agentId}/{customerAccountNumber}/{amount}/{transactionDateTime}/{description}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8", "application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> LogComplaint(LogComplaint bean) {
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		responseDTO = logComplaintService.save(bean);
		return new ResponseEntity(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/complaint/getAllComplaints.action/{channel}/{userId}/{agentId}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8", "application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> getAll(LogComplaint bean) {
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		responseDTO = logComplaintService.listAll(bean.getUserId());
		return new ResponseEntity(responseDTO, HttpStatus.OK);
	}

}
