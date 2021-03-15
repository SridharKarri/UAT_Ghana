package com.ceva.service.impl;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceva.bank.common.beans.AccountBalance;
import com.ceva.bank.common.beans.TransferRequest;
import com.ceva.bank.services.FinacleService;
import com.ceva.bank.services.OtpBankService;
import com.ceva.bank.utils.TransactionCodes;
import com.ceva.db.model.Alert;
import com.ceva.db.model.Fee;
import com.ceva.db.model.UserLoginCredentials;
import com.ceva.service.NotificationService;
import com.ceva.vo.service.MailService;
import com.ibm.icu.text.SimpleDateFormat;
import com.nbk.util.ConfigLoader;
import com.nbk.util.Constants;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {
	
	static Logger log = Logger.getLogger(NotificationServiceImpl.class);
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private FinacleService finacleService;
	
	@Autowired
	private OtpBankService otpBankService;
	
	private String language;
	
	@PostConstruct
	public void setLanguage(){
		log.debug("doing post construction work...");
		this.language = ConfigLoader.convertResourceBundleToMap("config").get("bank.language");
	}
	
	@Override
	public Alert sendEmail(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		return prepareAlert(transferRequest, credentials, fee);
	}

	@Override
	public Alert sendSMS(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		return prepareSMSAlert(transferRequest, credentials, fee);
	}
	
	private Alert prepareSMSAlert(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		Alert alert = null;
		try {
			if("en".equals(language)){
				alert = prepareEnglsihSMSAlert(transferRequest, credentials, fee);
			}
			if("fr".equals(language)){
				alert = prepareFrenchSMSAlert(transferRequest, credentials, fee);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while sending notification." + e.getLocalizedMessage());
		}
		String agentAccount = getAgentAccount(transferRequest);
		alert = otpBankService.sendTxnSms(alert, agentAccount);
		return alert;
	}

	private String getAgentAccount(TransferRequest transferRequest) {
		if (TransactionCodes.CWDBYACT.toString().equals(transferRequest.getTxnCode())) {
			return transferRequest.getBeneficiaryAccount();
		} else
			return transferRequest.getAccountNumber();
	}

	private Alert prepareAlert(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		Alert alert = null;
		if("en".equals(language)){
			alert = prepareEnglsihAlert(transferRequest, credentials, fee);
		}
		if("fr".equals(language)){
			alert =  prepareFrenchAlert(transferRequest, credentials, fee);
		}
		alert = mailService.sendMail(alert);
		return alert;
	}
	
	private Alert prepareFrenchAlert(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		try {
			message.append("cher <b>").append(credentials.getUserName()).append("</b><br/><br/>");
			if (TransactionCodes.CASHDEP.toString().equals(transferRequest.getTxnCode())) {
				message.append("Votre transaction de dépôt en espèces a été réussie.");
			} else if (TransactionCodes.FTINTERBANK.toString().equals(transferRequest.getTxnCode())) {
				message.append("Transférer à " + transferRequest.getBeneficiaryName() + " Était un succès.");
			} else {
				message.append("Votre transaction de retrait en espèces a été réussie.");
			}

			message.append(
					"<br/> <table class='datatable' bgcolor='#FAFAFA' width='100%' cellspacing='0' cellpadding='0' style=' border: 4px solid rgba(229, 229, 229, 0.88);  color: rgba(98, 103, 114, 0.94); font-family: monospace; font-size: 12px;'>");
			message.append("<tr><td>Numéro de réference  </td><td>")
					.append(transferRequest.getReferenceCode() + "</td></tr>").append("<tr><td>Montant  </td><td>")
					.append(transferRequest.getAmount() + "</td></tr>").append("<tr><td>Commission gagnée  </td><td>")
					.append(fee.getAgntCmsn() + "").append("</td></tr>").append("<tr><td>Date et l'heure  </td><td>")
					.append(new SimpleDateFormat(Constants.MAIL_DATE_FORMAT).format(new Date()) + "</td></tr>");

			alert.setMessage(message.toString());
			alert.setMobile(credentials.getMobileNo());
			alert.setMailTo(credentials.getEmail());
			alert.setAppl("MAIL");
			alert.setTxnType("TRANSUCCESS");
			alert.setSubject("Notification de transaction");
			//alertDao.save(alert);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error :" + e.getLocalizedMessage());
		} 
	return alert;
		
	}

	private Alert prepareEnglsihAlert(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		try {
			message.append("Dear <b>").append(credentials.getUserName()).append("</b><br/><br/>");
			if (TransactionCodes.CASHDEP.toString().equals(transferRequest.getTxnCode())) {
				message.append("Your Cash Deposit Transaction Was Successful.");
			} else if (TransactionCodes.FTINTERBANK.toString().equals(transferRequest.getTxnCode())) {
				message.append("Transfer to " + transferRequest.getBeneficiaryName() + " Was Successful.");
			} else {
				message.append("Your Cash Withdrawal Transaction Was Successful.");
			}

			message.append(
					"<br/> <table class='datatable' bgcolor='#FAFAFA' width='100%' cellspacing='0' cellpadding='0' style=' border: 4px solid rgba(229, 229, 229, 0.88);  color: rgba(98, 103, 114, 0.94); font-family: monospace; font-size: 12px;'>");
			message.append("<tr><td>Reference number  </td><td>")
					.append(transferRequest.getReferenceCode() + "</td></tr>").append("<tr><td>Amount  </td><td>")
					.append(transferRequest.getAmount() + "</td></tr>").append("<tr><td>Commission Earned  </td><td>")
					.append(fee.getAgntCmsn() + "").append("</td></tr>").append("<tr><td>Date  And Time  </td><td>")
					.append(new SimpleDateFormat(Constants.MAIL_DATE_FORMAT).format(new Date()) + "</td></tr>");

			alert.setMessage(message.toString());
			alert.setMobile(credentials.getMobileNo());
			alert.setMailTo(credentials.getEmail());
			alert.setAppl("MAIL");
			alert.setTxnType("TRANSUCCESS");
			alert.setSubject("Transaction Notification");
			//alertDao.save(alert);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error :" + e.getLocalizedMessage());
		} 
		return alert;
	}

	

	private Alert prepareFrenchSMSAlert(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		AccountBalance balance = null;
		try {
			message.append("cher ").append(credentials.getUserName()).append(", ");
			if (TransactionCodes.CASHDEP.toString().equals(transferRequest.getTxnCode())) {
				message.append(
						"Dépôt en espèces en faveur de " + transferRequest.getBeneficiaryAccount() + " a été un succès.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else if (TransactionCodes.FTINTERBANK.toString().equals(transferRequest.getTxnCode())) {
				message.append("Votre transfert à " + transferRequest.getBeneficiaryAccount() + " a été un succès.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else if (TransactionCodes.FTINTRABANK.toString().equals(transferRequest.getTxnCode())) {
				message.append("Votre transfert à " + transferRequest.getBeneficiaryAccount() + " a été un succès.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else if (TransactionCodes.DEPWALLET.toString().equals(transferRequest.getTxnCode())) {
				message.append("Votre transfert à " + transferRequest.getBeneficiaryAccount() + " a été un succès.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else {
				message.append("Votre retrait d'argent en faveur de " + transferRequest.getBeneficiaryAccount()
						+ " a été un succès.");
				balance = finacleService.balanceEnquirey(transferRequest.getBeneficiaryAccount());
			}

			message.append(" Montant : ").append(transferRequest.getAmount() + "").append(", Commission: ")
					.append(fee.getAgntCmsn() + "").append(", Ref: ").append(transferRequest.getReferenceCode() + "")
					.append(", Temps: ")
					.append(new SimpleDateFormat(Constants.CELLULANT_YEAR_FRMT).format(new Date()) + "")
					.append(", Équilibre:")
					// .append(balance.getBalance().subtract(transferRequest.getAmount().add(fee.getFeeVal()))+"");
					.append(balance.getBalance() + "");

			alert.setMessage(message.toString());
			alert.setMobile(credentials.getMobileNo());
			alert.setAppl("SMS");
			alert.setTxnType("TRANSUCCESS");
			//alertDao.save(alert);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while sending notification." + e.getLocalizedMessage());
		}
		return alert;
	}

	private Alert prepareEnglsihSMSAlert(TransferRequest transferRequest, UserLoginCredentials credentials, Fee fee) {
		Alert alert = new Alert();
		StringBuffer message = new StringBuffer();
		AccountBalance balance = null;
		try {
						
			message.append("Dear ").append(credentials.getUserName()).append(", ");
			if (TransactionCodes.CASHDEP.toString().equals(transferRequest.getTxnCode())) {
				message.append(
						"Cash deposit in favour of " + transferRequest.getBeneficiaryAccount() + " Was Successful.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else if (TransactionCodes.FTINTERBANK.toString().equals(transferRequest.getTxnCode())) {
				message.append("Your Transfer to " + transferRequest.getBeneficiaryAccount() + " Was Successful.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else if (TransactionCodes.FTINTRABANK.toString().equals(transferRequest.getTxnCode())) {
				message.append("Your Transfer to " + transferRequest.getBeneficiaryAccount() + " Was Successful.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else if (TransactionCodes.DEPWALLET.toString().equals(transferRequest.getTxnCode())) {
				message.append("Your Transfer to " + transferRequest.getBeneficiaryAccount() + " Was Successful.");
				balance = finacleService.balanceEnquirey(transferRequest.getAccountNumber());
			} else {
				message.append("Your Cash Withdrawal in favour of " + transferRequest.getBeneficiaryAccount()
						+ " Was Successful.");
				balance = finacleService.balanceEnquirey(transferRequest.getBeneficiaryAccount());
			}

			message.append(" Amount : ").append(transferRequest.getAmount() + "").append(", Commission: ")
					.append(fee.getAgntCmsn() + "").append(", Ref: ").append(transferRequest.getReferenceCode() + "")
					.append(", Time: ")
					.append(new SimpleDateFormat(Constants.CELLULANT_YEAR_FRMT).format(new Date()) + "")
					.append(", Balance:")
					// .append(balance.getBalance().subtract(transferRequest.getAmount().add(fee.getFeeVal()))+"");
					.append(balance.getBalance() + "");

			alert.setMessage(message.toString());
			alert.setMobile(credentials.getMobileNo());
			alert.setAppl("SMS");
			alert.setTxnType("TRANSUCCESS");
			//alertDao.save(alert);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while sending notification." + e.getLocalizedMessage());
		}
		return alert;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
