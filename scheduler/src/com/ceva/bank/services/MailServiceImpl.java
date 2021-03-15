package com.ceva.bank.services;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ceva.service.MailService;
import com.ceva.vo.AbstractVO;

@Service("mailService")
public class MailServiceImpl implements MailService {

	Logger log = Logger.getLogger(MailService.class);

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	JavaMailSender externalMailSender;


	@Autowired
	VelocityEngine velocityEngine;

	@Override
	public boolean sendMail(final AbstractVO commonObject) throws MailException {
		MimeMessagePreparator preparator = null;
		boolean sent = false;
		preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws MessagingException,
					UnsupportedEncodingException, VelocityException {
				MimeMessageHelper message =null;
				
				File fileMul [] =  commonObject.getAttachment();
				if(fileMul != null && fileMul.length>0){
					message = new MimeMessageHelper(
							mimeMessage, true);
				}else{
					message = new MimeMessageHelper(
							mimeMessage);
				}

				if (commonObject.getToAddress() != null) {
					message.setTo(commonObject.getToAddress());
				}

				if (commonObject.getToCC() != null) {
					message.setCc(commonObject.getToCC());
				}

				if (commonObject.getToBcc() != null) {
					message.setBcc(commonObject.getToBcc());
				}

				/*message.setFrom(commonObject.getSenderEmailAddress(),
						commonObject.getSenderEmailAddress());*/
				message.setFrom(new InternetAddress("Firstmonie <"+commonObject.getSenderEmailAddress()+">"));
				message.setSubject(commonObject.getSubject());
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("VO", commonObject);

				String text = VelocityEngineUtils
						.mergeTemplateIntoString(velocityEngine, commonObject.getVmTemplate(), model);
				message.setText(text, true);
				
				if(fileMul != null && fileMul.length>0){
					message.addAttachment(fileMul[0].getName(), fileMul[0]);
				}
				fileMul = null;
			}
		};
		try {
			String[] toaddrs = commonObject.getToAddress();
			for(String toAddr : toaddrs){
				if(toAddr.endsWith("firstbanknigeria.com")){
					mailSender.send(preparator);
				}else{
					externalMailSender.send(preparator);
				}
			}
			sent= true;
		} catch (MailException e) {
			log.error("[sendConfirmationEmail] Error in SendConfirmation mail ::: " + e.getMessage());
			e.printStackTrace();
			sent= false;
			throw e;
		}
		return sent;
	}
	@Override
	public void sendMailWithAttachment(AbstractVO commonObject) {
		// TODO Auto-generated method stub

	}

}
