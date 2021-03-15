package com.ceva.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ceva.dto.ResponseDTO;
import com.ceva.service.AgentService;
import com.nbk.util.Constants;

@Controller
public class AgentController {

	Logger log = Logger.getLogger(AgentController.class);

	@Autowired
	AgentService agentService;

	@RequestMapping(value = "/agent/load.action/{channel}/{userId}/{mobileNumber}/{agentId}",
			method = RequestMethod.POST, produces = {"application/json ; charset=utf-8","application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> loadAgent(@PathVariable String agentId) {
		log.info("loadAgent.." + agentId);
		ResponseDTO responseDTO = null;
		try {
				log.info("loadAgent..");
				responseDTO = agentService.loagAgent(agentId);
				log.info(responseDTO.toString());
				if (!Constants.SUCESS_SMALL.equals(responseDTO.getMessage())) {
					log.info("success in loadAgent.");
					log.info("response..:"+ responseDTO.toString());
					//log.info(responseDTO.toString());
				}
				return new ResponseEntity<>(responseDTO, HttpStatus.OK);

		} catch (Exception e) {
			log.error("Error..:" + e.getLocalizedMessage());
			e.printStackTrace();
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.OK);
		} finally {

		}
	}

}
