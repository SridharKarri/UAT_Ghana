package com.ceva.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceva.bank.common.beans.ChatItem;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.ChatService;
import com.nbk.util.CommonUtil;

@RestController
public class ChatController {

	private Logger log = Logger.getLogger(ChatController.class);

	@Autowired
	ChatService chatService;

	@RequestMapping(value = "/chat/savechat.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{message}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> save(@Valid ChatItem bean, BindingResult bindingResult) {
		log.info("chat send.."+bean.toString());
		log.info("chat send.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("Fetching Ads.. passed Validation");
				responseDTO = chatService.save(bean);
				if(responseDTO != null){
					return new ResponseEntity(responseDTO, HttpStatus.OK);
				}else{
					return new ResponseEntity("Internal Error", HttpStatus.OK);
				}
			}else{
				log.info("Fetching Ads.. failed Validation");
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

	@RequestMapping(value = "/chat/loadchat.action/{channel}/{userId}/{merchantId}/{mobileNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> load(@PathVariable("userId") String userId) {
		log.info("chat load.."+userId);
		ResponseDTO responseDTO = null;
		try{
			if(userId.length()>0){
				log.info("Fetching Ads.. passed Validation");
				responseDTO = chatService.listAll(userId);
				if(responseDTO != null){
					return new ResponseEntity(responseDTO, HttpStatus.OK);
				}else{
					return new ResponseEntity("Internal Error", HttpStatus.OK);
				}
			}else{
				log.info("Fetching chats.. failed Validation");
				return new ResponseEntity<>("Invalid Input", HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Input", HttpStatus.BAD_REQUEST);
		}finally{

		}
	}

	@RequestMapping(value = "/chat/updatechat.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{id}/{message}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> update(@Valid ChatItem bean, BindingResult bindingResult) {
		log.info("chat send.."+bean.toString());
		log.info("chat send.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("Fetching Ads.. passed Validation");
				responseDTO = chatService.update(bean);
				if(responseDTO != null){
					return new ResponseEntity(responseDTO, HttpStatus.OK);
				}else{
					return new ResponseEntity("Internal Error", HttpStatus.OK);
				}
			}else{
				log.info("Fetching Ads.. failed Validation");
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

}
