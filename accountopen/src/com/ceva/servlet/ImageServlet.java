package com.ceva.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class ImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;
    Logger logger= Logger.getLogger(ImageServlet.class);
    
    private AccountService accountService; 
    
    private LoginDao loginDao;

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
		ResponseDTO dto = null;
		try {
			String userId = request.getParameter("userId");
			String secret = request.getHeader("secret");
			logger.info("userId..!"+userId);
			dto = validateUser(userId, secret);
			if(Constants.SUCCESS_RESP_CODE.equals(dto.getResponseCode())){
				List<FileItem> fileItemsList = uploader.parseRequest(request);
				Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
				while(fileItemsIterator.hasNext()){
					FileItem fileItem = fileItemsIterator.next();
					logger.info("FileName="+fileItem.getName());
					String image = fileItem.getString();
					dto = saveImage(image, userId);
				}
				JSONObject object= JSONObject.fromObject(dto);
				response.setStatus(200);
				response.getOutputStream().write(object.toString().getBytes());
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
	
	private ResponseDTO saveImage(String content, String userId) {
		Long id = System.nanoTime();
		ResponseDTO dto = new ResponseDTO(Constants.SUCCESSFULL_RESP_MESSAGE, Constants.SUCCESS_RESP_CODE);
		try{
			
			AccountImage accountImage= new AccountImage();
			accountImage.setMandate(content);
			accountImage.setMaker(userId);
			Long reference = accountService.save(accountImage);
			Map<String, String> data = new HashMap<String, String>();
			data.put("reference", reference+"");
			dto.setData(data);
			return dto;
			/*
			connection = dataSource.getConnection();
			String sql = "insert into ACCOUNTIMAGE (ID, IMAGE, MAKER) values (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.setString(2, image);
			statement.setString(3, userId);
			statement.execute();
			return id+"";*/
		}catch(Exception e){
			e.getLocalizedMessage();
			logger.error("Error..!"+e.getLocalizedMessage());
			return null;	
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
