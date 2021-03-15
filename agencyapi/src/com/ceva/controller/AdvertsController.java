package com.ceva.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceva.bank.common.beans.BalanceEnquirey;
import com.ceva.db.model.Adverts;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.AdvertsService;
import com.nbk.util.CommonUtil;

@RestController
public class AdvertsController {

	private Logger log = Logger.getLogger(AdvertsController.class);

	@Autowired
	AdvertsService advertsService;

	@RequestMapping(value = "/adverts/ads.action/{channel}/{userId}/{merchantId}/{mobileNumber}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> fetchAds(@Valid BalanceEnquirey bean, BindingResult bindingResult) {
		log.info("Fetching fetchAds.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("Fetching Ads.. passed Validation");
				responseDTO = advertsService.listAdverts(bean);
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

	@RequestMapping(value = "/adverts/update.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{id}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> deactiveAdd(@Valid Adverts bean, BindingResult bindingResult) {
		log.info("Fetching fetchAds.."+bean.toString());
		log.info("Validation Result.."+bindingResult);
		log.info(bean.toString());
		ResponseDTO responseDTO = null;
		try{
			if(!bindingResult.hasErrors()){
				log.info("Fetching Ads.. passed Validation");
				responseDTO = advertsService.update(bean);
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


	@RequestMapping(value = "/adverts/pic.action/{channel}/{userId}/{merchantId}/{mobileNumber}/{id}", method = RequestMethod.POST, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	@ResponseBody
	public ResponseEntity<?> upload(@PathVariable("id") long id, @PathVariable("channel") String channel, HttpServletResponse response) {
		log.info("addwards image download.."+id);
		File file=null;
		String path =null;
		Adverts adds = null;
		InputStream inputStream = null;
		try{
			log.info("addwards image loading image..");
			adds = advertsService.getById(id);
			if(adds !=null){
				path = adds.getImgPath();
				log.debug("addwards image path.:"+path);
				file = new File(path);
				if(file.exists()){
					log.info("image found at location.");
					response.setContentType("application/octet-stream");
					response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
					response.setContentLength((int)file.length());
				    inputStream = new BufferedInputStream(new FileInputStream(file));
				    //FileCopyUtils.copy(inputStream, response.getOutputStream());
				    return new ResponseEntity<byte[]>(IOUtils.toByteArray(inputStream), HttpStatus.OK);
				}else{
					log.info("image not found at location");
					return new ResponseEntity<>("image not found at location", HttpStatus.OK);
				}
			}else{
				log.info("object not found in db");
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.OK);
		}finally{
			 path =null;
			 file= null;
			 adds=null;
		}
	}

}
