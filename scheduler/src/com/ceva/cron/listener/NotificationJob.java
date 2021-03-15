package com.ceva.cron.listener;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ceva.bank.common.dao.NotificationDao;
import com.ceva.bank.utils.CreateTemplate;
import com.ceva.db.model.Notification;
import com.ceva.service.MailService;
import com.ceva.vo.AbstractVO;

//@Component("notificationJob")
public class NotificationJob {
	Logger log = Logger.getLogger(NotificationJob.class);
	
	@Autowired
	NotificationDao notificationDao;

	@Autowired
	MailService mailService;

	//@Scheduled(fixedRate=100000)
	public void generateNotification() {
		log.debug("Startig notification email.");
		List<Notification> notifications = notificationDao.getAll("MAIL");
		if(notifications!= null && notifications.size()>0){
			log.info("records for notification.."+notifications.size());
			updateStatusToProgress(notifications);
			for (Notification notification : notifications) {
				log.info("Sending email for record:" + notification.getId());
				AbstractVO vo =CreateTemplate.createTemplateVO(notification);
				try{
					boolean sent = mailService.sendMail(vo);
					if(sent){
						notification.setDeliveryStatus("Success");
						notification.setFetchStatus("S");
					}else{
						notification.setDeliveryStatus("Failed");
						notification.setRetryCount(notification.getRetryCount()+1);
						if(notification.getRetryCount()<=2){
							notification.setFetchStatus("P");
						}else{
							notification.setFetchStatus("F");
						}
					}
				}catch(Exception e){
					notification.setDeliveryStatus("Error");
					notification.setRetryCount(notification.getRetryCount()+1);
					if(notification.getRetryCount()<=2){
						notification.setFetchStatus("P");
					}else{
						notification.setFetchStatus("E");
					}
				}
				notificationDao.update(notification);
			}
		}
	}

	private void updateStatusToProgress(List<Notification> notifications) {
		for (Notification notification : notifications) {
			notification.setFetchStatus("I");
			notificationDao.update(notification);
		}
	}
}
