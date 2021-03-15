package com.ceva.fbn.upload;

import com.ceva.db.model.UserLoginCredentials;
import com.nbk.util.CommonUtil;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ImageServlet
extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletFileUpload uploader = null;
	Logger logger = Logger.getLogger(ImageServlet.class);
	private SessionFactory factory = null;

	private DataSource dataSource;


	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File)getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		uploader = new ServletFileUpload((FileItemFactory)fileFactory);
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		dataSource = (DataSource)webApplicationContext.getBean("dataSource");
		factory = (SessionFactory)webApplicationContext.getBean("sessionFactory");
		logger.info("factory..:" + this.factory.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new ServletException("Content type is not multipart/form-data");
		}
		logger.info("account image upload..!");
		response.setContentType("text/html");
		String result = "FAILED";
		try {
			String userId = request.getParameter("userId");
			String secret = request.getHeader("secret");
			logger.info("userId..!" + userId);
			boolean isValid = validateUser(userId, secret);
			if (isValid) {
				List<FileItem> fileItemsList = this.uploader.parseRequest(request);
				Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
				while (fileItemsIterator.hasNext()) {
					FileItem fileItem = fileItemsIterator.next();
					logger.info("FileName=" + fileItem.getName());
					String image = fileItem.getString();
					String refnumber = saveImage(image, userId);
					result = (new StringBuilder(String.valueOf(refnumber))).toString();
				} 
				response.setStatus(200);
				response.getOutputStream().write(result.getBytes());
				response.flushBuffer();
			} else {
				logger.info("Invalid Credentials supplied..!");
				response.setStatus(400);
				response.getOutputStream().write("Invalid Credentials Supplied".getBytes());
				response.flushBuffer();
			} 
		} catch (FileUploadException e) {
			result = e.getLocalizedMessage();
			logger.error("Error..!" + e.getLocalizedMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error..!" + e.getLocalizedMessage());
			result = e.getLocalizedMessage();
		} 
		logger.info("result..:" + result);
	}

	private String saveImage(String image, String userId) {
		Long id = Long.valueOf(System.nanoTime());
		Connection connection = null;
		try {
			connection = this.dataSource.getConnection();
			String sql = "insert into ACCOUNTIMAGE (ID, IMAGE, MAKER) values (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id.longValue());
			statement.setString(2, image);
			statement.setString(3, userId);
			statement.execute();
			return String.valueOf(id);
		} catch (Exception e) {
			e.getLocalizedMessage();
			logger.error("Error..!" + e.getLocalizedMessage());
			return e.getLocalizedMessage();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		} 
	}

	private boolean validateUser(String userId, String pin) {
		boolean isValid = false;
		logger.info("loading by user id");
		UserLoginCredentials userLoginCredentials = null;
		Session session = null;

		try {
			session = this.factory.openSession();
			Criteria criteria = session
					.createCriteria(UserLoginCredentials.class);
			criteria.add((Criterion)Restrictions.eq("userId", userId));
			criteria.add((Criterion)Restrictions.eq("status", "A"));
			userLoginCredentials = (UserLoginCredentials)criteria
					.uniqueResult();
		}
		catch (Exception e) {

			logger.error("Error While validating user by id.");
			logger.error("Reason..:" + e.getLocalizedMessage());
		} finally {

			session.close();
			session = null;
		} 
		isValid = CommonUtil.validatePin(pin, userLoginCredentials.getPin(), "1");

		return isValid;
	}
}