package com.ceva.vo.service;

import org.springframework.mail.MailException;

import com.ceva.db.model.Alert;
import com.ceva.vo.AbstractVO;

public interface MailService {

	public Alert sendMail(Alert alert) throws MailException;
	public boolean sendMail(AbstractVO commonObject) throws MailException;

}
