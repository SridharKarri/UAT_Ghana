package com.ceva.bank.utils;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ceva.db.model.Alert;
import com.ceva.db.model.Notification;
import com.ceva.vo.AbstractVO;
import com.ceva.vo.TemplateVO;


public class CreateTemplate {


	protected static Logger logger = Logger.getLogger(CreateTemplate.class);

	private static ResourceBundle configBundle;
	private static String language = null;
	
	static {
		configBundle = ResourceBundle.getBundle("config");
		language = configBundle.getString("bank.language");
	}

	public static AbstractVO createTemplateVO(Alert emailBean) {
		TemplateVO vo = new TemplateVO();
		vo.setSenderEmailAddress(emailBean.getMailFrom());
		String[] to = null;
		String[] cc = null;
		String[] bcc = null;
		String supportMailId = configBundle.getString("mail.support");
		String bankName = configBundle.getString("mail.client");
		String sender = configBundle.getString("mail.from");


		if (emailBean.getMailTo() != null) {
			if (emailBean.getMailTo().indexOf(',') != -1) {
				to = emailBean.getMailTo().split(",");
			} else {
				to = new String[1];
				to[0] = emailBean.getMailTo();
			}
		}
		if (emailBean.getMailcc() != null) {
			if (emailBean.getMailcc().indexOf(',') != -1) {
				cc = emailBean.getMailcc().split(",");
			} else {
				cc = new String[1];
				cc[0] = emailBean.getMailcc();
			}
		}
		if (emailBean.getMailBcc() != null) {
			if (emailBean.getMailBcc().indexOf(',') != -1) {
				bcc = emailBean.getMailBcc().split(",");
			} else {
				bcc = new String[1];
				bcc[0] = emailBean.getMailBcc();
			}
		}

		vo.setToAddress(to);
		vo.setToCC(cc);
		vo.setToBcc(bcc);
		vo.setSubject(emailBean.getSubject());
		//vo.setSenderEmailAddress(emailBean.getMailFrom());
		vo.setSenderEmailAddress(sender);
		vo.setMessage(emailBean.getMessage());
		vo.setVmTemplate("com/ceva/" + language + "/templates/" + emailBean.getTxnType() + ".vm");
		File[] arr = null;
		String filepath = null;
		if (configBundle != null) {
			logger.debug("configBundle file type is " + configBundle.getString(String.valueOf(emailBean.getTxnType()) + "_FILE") + "]. ");
			if (!"N".equalsIgnoreCase(configBundle.getString(emailBean.getTxnType() + "_FILE"))) {
				filepath = emailBean.getAttachementLocation();
				arr = new File[]{new File(filepath)};
				vo.setAttachment(arr);
			} else {
				vo.setAttachment(null);
			}
		}

		vo.setHelpdeskMail(supportMailId);
		vo.setBankName(bankName);

		return vo;

	}
	
	public static AbstractVO createTemplateVO(Notification emailBean) {
		TemplateVO vo = new TemplateVO();
		vo.setSenderEmailAddress(emailBean.getMailFrom());
		String[] to = null;
		String[] cc = null;
		String[] bcc = null;
		String supportMailId = configBundle.getString("mail.support");
		String bankName = configBundle.getString("mail.client");
		String sender = configBundle.getString("mail.from");


		if (emailBean.getMailTo() != null) {
			if (emailBean.getMailTo().indexOf(',') != -1) {
				to = emailBean.getMailTo().split(",");
			} else {
				to = new String[1];
				to[0] = emailBean.getMailTo();
			}
		}
		if (emailBean.getMailcc() != null) {
			if (emailBean.getMailcc().indexOf(',') != -1) {
				cc = emailBean.getMailcc().split(",");
			} else {
				cc = new String[1];
				cc[0] = emailBean.getMailcc();
			}
		}
		if (emailBean.getMailBcc() != null) {
			if (emailBean.getMailBcc().indexOf(',') != -1) {
				bcc = emailBean.getMailBcc().split(",");
			} else {
				bcc = new String[1];
				bcc[0] = emailBean.getMailBcc();
			}
		}

		vo.setToAddress(to);
		vo.setToCC(cc);
		vo.setToBcc(bcc);
		vo.setSubject(emailBean.getSubject());
		//vo.setSenderEmailAddress(emailBean.getMailFrom());
		vo.setSenderEmailAddress(sender);
		vo.setMessage(emailBean.getMessage());
		vo.setVmTemplate("com/ceva/" + language + "/templates/" + emailBean.getTxnType() + ".vm");
		File[] arr = null;
		String filepath = null;
		if (configBundle != null) {
			logger.debug("configBundle file type is [" + configBundle.getString(String.valueOf(emailBean.getTxnType()) + "_FILE") + "]. ");
			if (!"N".equalsIgnoreCase(configBundle.getString(emailBean.getTxnType() + "_FILE"))) {
				filepath = emailBean.getAttachementLocation();
				arr = new File[]{new File(filepath)};
				vo.setAttachment(arr);
			} else {
				logger.debug("Create Template With Out Attachment. ");
				vo.setAttachment(null);
			}
		}

		vo.setHelpdeskMail(supportMailId);
		vo.setBankName(bankName);

		return vo;

	}


}
