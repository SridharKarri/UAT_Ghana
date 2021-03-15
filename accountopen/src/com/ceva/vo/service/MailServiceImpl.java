package com.ceva.vo.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
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

import com.ceva.db.model.Alert;
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
				log.debug("Inside prepare : " + mimeMessage);
				MimeMessageHelper message = new MimeMessageHelper(
						mimeMessage);

				if (commonObject.getToAddress() != null) {
					message.setTo(commonObject.getToAddress());
				}

				if (commonObject.getToCC() != null) {
					message.setCc(commonObject.getToCC());
				}

				if (commonObject.getToBcc() != null) {
					message.setBcc(commonObject.getToBcc());
				}

				message.setFrom(commonObject.getSenderEmailAddress(),
						commonObject.getSenderEmailAddress());
				log.debug("from address..:"+commonObject.getSenderEmailAddress());
				log.debug("To address..:"+commonObject.getToAddress());
				log.debug("After Setting From to ...... mime message Helper");
				message.setSubject(commonObject.getSubject());
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("VO", commonObject);

				String text = VelocityEngineUtils
						.mergeTemplateIntoString(velocityEngine, commonObject.getVmTemplate(), model);
				message.setText(text, true);
			}
		};
		try {
			String[] toaddrs = commonObject.getToAddress();
			for(String toAddr : toaddrs){
				log.info("toAddr.:"+toAddr);
				if(toAddr.endsWith("firstbanknigeria.com")){
					log.info("Sending through internal mail sernder configuration channel");
					mailSender.send(preparator);
				}else{
					log.info("Sending through external mail sernder configuration channel");
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
	public Alert sendMail(Alert alert) throws MailException {
		AbstractVO vo = com.nbk.util.CreateTemplate.createTemplateVO(alert);
		
		boolean sent=false;
		try{
			sent = sendMail(vo);
			if(sent){
				alert.setDeliveryStatus("Success");
				alert.setFetchStatus("S");
			}else{
				alert.setDeliveryStatus("Failed");
				alert.setRetryCount(alert.getRetryCount()+1);
				if(alert.getRetryCount()<=2){
					alert.setFetchStatus("P");
				}else{
					alert.setFetchStatus("F");
				}
			}
		}catch (Exception e) {
			alert.setDeliveryStatus("Failed");
			alert.setDetails(e.getCause().getMessage());
			alert.setRetryCount(alert.getRetryCount()+1);
			if(alert.getRetryCount()<=2){
				alert.setFetchStatus("P");
			}else{
				alert.setFetchStatus("F");
			}
			
		}
		
		return alert;
	}


}
