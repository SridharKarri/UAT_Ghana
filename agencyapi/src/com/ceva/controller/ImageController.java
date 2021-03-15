package com.ceva.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ceva.bank.common.dao.LoginDao;
import com.ceva.db.model.AccountImage;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.AccountService;
import com.nbk.util.Constants;

@Controller
@Scope("prototype")
@PropertySource("classpath:config.properties")
public class ImageController {

	
	private static final Logger log = Logger.getLogger(CommonController.class);
	
	private File uploadDirRoot;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private LoginDao loginDao;
	 
    @Autowired
    ImageController(@Value("${user.profile.pic.apth}") String uploadDir) {
        this.uploadDirRoot = new File(uploadDir);
    }
	
	@RequestMapping(value = "/image1/mandate.action", method = RequestMethod.POST,
			consumes = { "multipart/form-data" },
			produces = {"application/json ; charset=utf-8"})
	@ResponseBody
	public ResponseEntity<?> loadParameters(
			@RequestParam("userId") String userId,
			@RequestParam("pin") String pin,
            @RequestParam("file") MultipartFile file) {
		try{
			ResponseDTO dto = validateUser(userId, pin);
			if("00".equals(dto.getResponseCode())){
				String content = new String(file.getBytes());
				AccountImage accountImage= new AccountImage();
				accountImage.setMandate(content);
				accountImage.setMaker(userId);
				Long reference = accountService.save(accountImage);
				Map<String, String> data = new HashMap<String, String>();
				data.put("reference", reference+"");
				dto.setData(data);
				return new ResponseEntity<>(dto, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(dto, HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Internal Error", HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/image/idimage.action", method = RequestMethod.POST,
			consumes = { "multipart/form-data" },
			produces = {"application/json ; charset=utf-8"})
	@ResponseBody
	public ResponseEntity<?> loadParameters(@RequestParam("userId") String userId,
			@RequestParam("pin") String pin,
			@RequestParam("ref") Long ref,
            @RequestParam("file") MultipartFile file) {
		try{
			ResponseDTO dto = validateUser(userId, pin);
			if("00".equals(dto.getResponseCode())){
				String content = new String(file.getBytes());
				AccountImage accountImage= accountService.getById(ref);
				accountImage.setIdImage(content);
				accountImage.setMaker(userId);
				boolean isSaved = accountService.update(accountImage);
				if(isSaved){
					dto.setResponseCode(Constants.SUCCESS_RESP_CODE);
					dto.setMessage(Constants.SUCESS_SMALL);
				}else{
					dto.setResponseCode(Constants.FAILURE_RESP_CODE);
					dto.setMessage(Constants.GENERIC_FAILURE_MESS);
				}
				return new ResponseEntity<>(dto, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(dto, HttpStatus.OK);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Internal Error", HttpStatus.OK);
		}
	}
	
	private ResponseDTO validateUser(String userId, String pin) {
		UserLoginCredentials userBean =null;
		ResponseDTO dto = new ResponseDTO();
		try{
			userBean = new UserLoginCredentials();
			userBean.setUserId(userId);
			userBean.setPin(pin);
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
