package com.nbk.util;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ceva.db.model.Alert;
import com.ceva.vo.AbstractVO;
import com.ceva.vo.TemplateVO;

public class CreateTemplate {

	protected static Logger logger = Logger.getLogger(CreateTemplate.class);

	private static ResourceBundle configBundle;
	private static String language;
	private static String supportMailId ;
	private static String bankName;
	private static String sender;

	static {
		logger.info("loading staic content..");
		configBundle = ResourceBundle.getBundle("config");
		language = configBundle.getString("bank.language");
		supportMailId = configBundle.getString("mail.support");
		bankName = configBundle.getString("mail.client");
		sender = configBundle.getString("mail.from");
	}

	public static AbstractVO createTemplateVO(Alert emailBean) {
		logger.debug("Creating Template VO. ");
		TemplateVO vo = new TemplateVO();
		vo.setSenderEmailAddress(emailBean.getMailFrom());
		String[] to = null;
		String[] cc = null;
		String[] bcc = null;

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
		vo.setSenderEmailAddress(sender);
		vo.setMessage(emailBean.getMessage());
		vo.setVmTemplate("com/ceva/"+language+"/templates/" + emailBean.getTxnType()+ ".vm");
		if (configBundle != null) {
			vo.setAttachment(null);
		}
		vo.setHelpdeskMail(supportMailId);
		vo.setBankName(bankName);

		return vo;

	}

}
