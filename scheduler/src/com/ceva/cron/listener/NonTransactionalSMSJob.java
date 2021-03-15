package com.ceva.cron.listener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.dao.AlertDao;
import com.ceva.bank.services.OtpBankService;
import com.ceva.db.model.Alert;

//@Component("nonTransactionalSMSJob")
public class NonTransactionalSMSJob {

	@Autowired
	AlertDao alertDao;

	@Autowired
	OtpBankService otpBankService;

	Logger log = Logger.getLogger(NonTransactionalSMSJob.class);
	
	//@Scheduled(fixedRate=(15*60*1000))
	public void onboardSms() {
		List<Alert> alerts = alertDao.getPinEmail("ONBOARD", "SMS");
		processParallel(alerts);
	}
	
	public void processParallel(List<Alert> alerts) {

        ExecutorService service = Executors.newFixedThreadPool(5);
        for (Alert o : alerts) {
            service.execute(new MyTask(o));
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class MyTask implements Runnable {
    	private Alert target;

        public MyTask(Alert target) {
            this.target = target;
        }

        @Override
        public void run() {
        	try{
        		processAlert(target);
        	}catch (Exception e) {
        		log.error("error.:"+e.getLocalizedMessage());
				e.printStackTrace();
			}
        }
    }
    
    private void processAlert(Alert alert) {
		try {
			log.info("sending sms ..: "+alert.getId());
			alert = otpBankService.postSMS(alert);
		} catch (Exception e) {
			log.error("Error sms for record:" + alert.getId());
			alert.setDeliveryStatus("Error");
			alert.setRetryCount(alert.getRetryCount() + 1);
			if (alert.getRetryCount() <= 2) {
				alert.setFetchStatus("P");
			} else {
				alert.setFetchStatus("E");
			}
		}
		alertDao.update(alert);
	}
}
