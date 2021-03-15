package com.ceva.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ceva.bank.common.dao.LoginDao;
import com.ceva.bank.common.dao.MerchantDao;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.LoginService;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@Controller
public class ProfilePicController {
	Logger log = Logger.getLogger(ProfilePicController.class);

	@Autowired
	LoginService loginService;

	@Autowired
	LoginDao loginDao;

	@Autowired
	MerchantDao merchantDao;

	@RequestMapping(value = "/profile/pic.action/{channel}/{userId}", method = RequestMethod.POST, produces = {
			"application/json ; charset=utf-8",
			"application/xml ; charset=utf-8" })
	@ResponseBody
	public ResponseEntity<?> upload(@PathVariable("userId") String userId, @PathVariable("channel") String channel, @RequestParam("file") MultipartFile file) {
		log.info("pic upload.."+userId);
		ResponseDTO responseDTO = null;
		String destPath = null;
		String fileName = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			if(file.getName().length()>0){
				log.info("pic upload .. passed Validation");
				destPath = ConfigLoader.convertResourceBundleToMap("config").get("user.profile.pic.apth");
				try{
					log.info("path:"+destPath);
					fileName = destPath+file.getOriginalFilename();
					File destFile = new File(fileName);
					if(!destFile.exists()){
						destFile.createNewFile();
					}
					file.transferTo(destFile);
					data.put("userId", userId);
					data.put("channel", channel);
					data.put("fileName", fileName);
					responseDTO = loginService.updateProfilePicture(data);
				}catch(Exception e){
					log.info("Error While uploading pic.");
					log.error("Error..:"+e.getLocalizedMessage());
				}
				return new ResponseEntity(responseDTO, HttpStatus.OK);
			}else{
				log.info("userLogin.. failed Validation");
				responseDTO = new ResponseDTO();
				responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
				responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
				return new ResponseEntity(responseDTO, HttpStatus.OK);
			}
		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Constants.FAILURE_RESP_CODE);
			responseDTO.setMessage(Constants.GENERIC_FAILURE_MESS);
			return new ResponseEntity(responseDTO, HttpStatus.OK);
		}finally{
			destPath = null;
			fileName = null;
			data = null;
		}

	}
	@RequestMapping(value = "/profile/getpic.action/{channel}/{userId}", method = RequestMethod.POST, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	@ResponseBody
	public ResponseEntity<?> download(HttpServletResponse response, @PathVariable("userId") String userId, @PathVariable("channel") String channel){
		log.info("pic download.."+userId);
		File file=null;
		String path =null;
		UserLoginCredentials credentials = null;
		InputStream inputStream = null;
		try{
			credentials = loginDao.getById(userId);
			path = credentials.getPic();
			log.info("image found path..:"+path);
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

		}catch(Exception e){
			log.error("Error..:"+e.getLocalizedMessage());
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.OK);
		}finally{
			 path =null;
			 credentials = null;
			 file= null;
		}
	}

}
