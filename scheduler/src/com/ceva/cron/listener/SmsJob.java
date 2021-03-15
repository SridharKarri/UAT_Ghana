package com.ceva.cron.listener;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.services.OtpBankService;
import com.ceva.db.model.Alert;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;





public class SmsJob
{
	@Autowired
	AlertDao alertDao;
	@Autowired
	OtpBankService otpBankService;
	Logger log = Logger.getLogger(SmsJob.class);

	public void pinSms() {
		log.info("Startig pin sms.");
		List<Alert> alerts = alertDao.getAll("SMS");
		if (alerts != null && alerts.size() > 0) {
			log.info("updating Fetchstatus to I.");
			updateStatusToProgress(alerts);
			log.info("updating Fetchstatus to I DONE.");
			for (Alert alert : alerts) {
				log.info("Sending sms for record:" + alert.getId());
				alert = otpBankService.postSMS(alert);
				alertDao.update(alert);
			} 
		} 

		log.info(new Date() + 
				" This task runs in fixed delay for SMS sending");
	}

	private void updateStatusToProgress(List<Alert> alerts) {
		for (Alert alert : alerts) {
			alert.setFetchStatus("I");
			alertDao.update(alert);
		} 
	}
}