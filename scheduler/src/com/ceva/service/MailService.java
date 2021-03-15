package com.ceva.service;

import org.springframework.mail.MailException;

import com.ceva.vo.AbstractVO;

public interface MailService {

	public boolean sendMail(AbstractVO commonObject) throws MailException;

	public void sendMailWithAttachment(AbstractVO commonObject);
}
