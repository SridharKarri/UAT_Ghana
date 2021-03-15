package com.ceva.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ceva.bank.common.dao.LoginDao;
import com.ceva.db.model.AccountImage;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.dto.ResponseDTO;
import com.ceva.service.AccountService;
import com.nbk.util.Constants;

import net.sf.json.JSONObject;

public class IdImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;
    Logger logger= Logger.getLogger(IdImageServlet.class);
    private LoginDao loginDao;    
    private AccountService accountService; 

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		this.accountService=(AccountService) webApplicationContext.getBean("accountService");
		this.loginDao=(LoginDao) webApplicationContext.getBean("loginDao");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		}
		response.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		ResponseDTO dto = null;
		try {
			String userId = request.getParameter("userId");
			String ref = request.getParameter("ref");
			String secret = request.getHeader("secret");
			dto = validateUser(userId, secret);
			if(Constants.SUCCESS_RESP_CODE.equals(dto.getResponseCode())){
				List<FileItem> fileItemsList = uploader.parseRequest(request);
				Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
				while(fileItemsIterator.hasNext()){
					FileItem fileItem = fileItemsIterator.next();
					logger.info("FileName="+fileItem.getName());
					String image = fileItem.getString();
					saveImage(image, userId, ref);
					jsonObject.put("responseCode", Constants.SUCCESS_RESP_CODE);
					jsonObject.put("message", Constants.SUCESS_SMALL);
				}
				response.setStatus(200);
				response.getOutputStream().write(jsonObject.toString().getBytes());
				response.flushBuffer();
			}else{
				response.setStatus(400);
				response.getOutputStream().write("Invalid Credentials Supplied".getBytes());
				response.flushBuffer();
			}
		} catch (FileUploadException e) {
			logger.error("Error..!"+e.getLocalizedMessage());
			//out.write("Exception in uploading file.");
		} catch (Exception e) {
			logger.error("Error..!"+e.getLocalizedMessage());
		}
	}
	
	private String saveImage(String image, String userId, String ref) {
		try{
			AccountImage accountImage= accountService.getById(Long.valueOf(ref));
			accountImage.setIdImage(image);
			boolean isSaved = accountService.update(accountImage);
			if(isSaved){
				return Constants.SUCCESSFULL_RESP_MESSAGE;
			}else{
				return Constants.FAILURE_RESP_CODE;
			}
		}catch(Exception e){
			e.getLocalizedMessage();
			logger.error("Error..!"+e.getLocalizedMessage());
			return e.getLocalizedMessage();	
		}finally{
			
		}
	}

	private ResponseDTO validateUser(String userId, String pin) {
		logger.info("loading by user id");
		ResponseDTO dto = null;
		try {
			dto = loginDao.validate(new UserLoginCredentials(userId, pin, "A"));
		} catch (Exception e) {
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {
		}
		return dto;
	}

}
