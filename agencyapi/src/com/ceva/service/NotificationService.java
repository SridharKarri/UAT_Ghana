package com.ceva.service;

import com.ceva.bank.common.beans.TransferRequest;
import com.ceva.db.model.Alert;
import com.ceva.db.model.Fee;
import com.ceva.db.model.UserLoginCredentials;

public interface NotificationService {
	
	public Alert sendEmail(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee);

	public Alert sendSMS(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee);
}
