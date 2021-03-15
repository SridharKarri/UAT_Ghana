package com.ceva.cron.listener;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.utils.CreateTemplate;
import com.ceva.db.model.Alert;
import com.ceva.service.MailService;
import com.ceva.vo.AbstractVO;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class EmailJob
{
	Logger log = Logger.getLogger(EmailJob.class);

	@Autowired
	AlertDao alertDao;

	@Autowired
	MailService mailService;

	public void emailJob() {
		this.log.debug("Startig pin email.");
		List<Alert> alerts = this.alertDao.getAll("MAIL");
		if (alerts != null && alerts.size() > 0) {
			this.log.info("records for email.." + alerts.size());
			updateStatusToProgress(alerts);
			for (Alert alert : alerts) {
				this.log.info("Sending email for record:" + alert.getId());
				AbstractVO vo = CreateTemplate.createTemplateVO(alert);
				try {
					boolean sent = this.mailService.sendMail(vo);
					if (sent) {
						alert.setDeliveryStatus("Success");
						alert.setFetchStatus("S");
					} else {
						alert.setDeliveryStatus("Failed");
						alert.setRetryCount(alert.getRetryCount() + 1L);
						if (alert.getRetryCount() <= 2L) {
							alert.setFetchStatus("P");
						} else {
							alert.setFetchStatus("E");
						} 
					} 
				} catch (Exception e) {
					alert.setDeliveryStatus("Error");
					alert.setRetryCount(alert.getRetryCount() + 1L);
					if (alert.getRetryCount() <= 2L) {
						alert.setFetchStatus("P");
					} else {
						alert.setFetchStatus("E");
					} 
				} 
				this.alertDao.update(alert);
				this.log.debug(new Date() + " This task runs in fixed delay by emailJob");
			} 
		} 
	}

	public void pinJob() {
		this.log.info("Startig pin JOB.");
		List<Alert> alerts = this.alertDao.getAllUserPendingEmail("USERID,TRANSACTIONPIN,INVALIDPIN,LOCKACCOUNT,PINRESET");
		if (alerts != null && alerts.size() > 0) {
			this.log.info("records for pin email.." + alerts.size());
			this.log.debug("updating Fetchstatus to I.");
			updateStatusToProgress(alerts);
			this.log.debug("updating Fetchstatus to I DONE.");
			for (Alert alert : alerts) {
				this.log.info("Sending pin for record:" + alert.getId());
				AbstractVO vo = CreateTemplate.createTemplateVO(alert);
				try {
					boolean sent = this.mailService.sendMail(vo);
					if (sent) {
						alert.setDeliveryStatus("Success");
						alert.setFetchStatus("S");
					} else {
						alert.setDeliveryStatus("Failed");
						alert.setRetryCount(alert.getRetryCount() + 1L);
						if (alert.getRetryCount() <= 2L) {
							alert.setFetchStatus("P");
						} else {
							alert.setFetchStatus("E");
						} 
					} 
				} catch (Exception e) {
					alert.setDeliveryStatus("Error");
					alert.setRetryCount(alert.getRetryCount() + 1L);
					if (alert.getRetryCount() <= 2L) {
						alert.setFetchStatus("P");
					} else {
						alert.setFetchStatus("E");
					} 
				} 
				this.alertDao.update(alert);
			} 
		} 
	}

	private void updateStatusToProgress(List<Alert> alerts) {
		for (Alert alert : alerts) {
			alert.setFetchStatus("I");
			this.alertDao.update(alert);
		} 
	}
}