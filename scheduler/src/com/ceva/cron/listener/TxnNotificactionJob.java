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
import com.ceva.bank.utils.CreateTemplate;
import com.ceva.db.model.Alert;
import com.ceva.service.MailService;
import com.ceva.vo.AbstractVO;

//@Component("txnNotificactionJob")
public class TxnNotificactionJob {
	private static Logger log = Logger.getLogger(TxnNotificactionJob.class);

	@Autowired
	private AlertDao alertDao;

	@Autowired
	private MailService mailService;
	
	//@Scheduled(fixedRate = 1000)
	public void emailJob() {
		log.debug("Startig txnNotificactionJob.");
		List<Alert> alerts = alertDao.getAll("MAIL");
		if(alerts!= null && alerts.size()>0){
			log.info("records for email.."+alerts.size());
			processParallel(alerts);
		}
	}

	public void processParallel(List<Alert> alerts) {

        // Fixed thread number
        ExecutorService service = Executors.newFixedThreadPool(20);
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
		AbstractVO vo = CreateTemplate.createTemplateVO(alert);
		try {
			log.info("sending email ..: "+alert.getId());
			log.info("email to..: "+alert.getMailTo());
			boolean sent = mailService.sendMail(vo);
			if (sent) {
				alert.setDeliveryStatus("Success");
				alert.setFetchStatus("S");
			} else {
				alert.setDeliveryStatus("Failed");
				alert.setRetryCount(alert.getRetryCount() + 1);
				if (alert.getRetryCount() <= 2) {
					alert.setFetchStatus("P");
				} else {
					alert.setFetchStatus("F");
				}
			}
		} catch (Exception e) {
			log.error("Error email for record:" + alert.getId());
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
