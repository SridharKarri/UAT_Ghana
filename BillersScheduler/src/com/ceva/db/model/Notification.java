package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {

	@Id
	@Column(name = "TXN_REF_NO")
	private String id;
	@Column(name = "TXN_TYPE")
	private String txnType;
	@Column(name = "FETCH_STATUS")
	private String fetchStatus;
	@Column(name = "MESSAGE")
	private String message;
	@Column(name = "MOBILE_NO")
	private String mobile;
	@Column(name = "APPL")
	private String appl;
	@Column(name = "EMAIL_ID")
	private String mailTo;
	@Column(name = "MAILFROM")
	private String mailFrom;
	@Column(name = "SUBJECT")
	private String subject;
	@Column(name = "MAILCC")
	private String mailcc;
	@Column(name = "MAILBCC")
	private String mailBcc;
	@Column(name = "RETRY_COUNT")
	private Integer retryCount;
	@Column(name = "MSG_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date messageDate;
	
	@Column(name = "ATTACHEMENT_LOC")
	private String attachementLocation;

	@Column(name = "DELIVERY_STATUS")
	private String deliveryStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getFetchStatus() {
		return fetchStatus;
	}

	public void setFetchStatus(String fetchStatus) {
		this.fetchStatus = fetchStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAppl() {
		return appl;
	}

	public void setAppl(String appl) {
		this.appl = appl;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}


	public String getMailcc() {
		return mailcc;
	}

	public void setMailcc(String mailcc) {
		this.mailcc = mailcc;
	}

	public String getMailBcc() {
		return mailBcc;
	}

	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}

	public String getAttachementLocation() {
		return attachementLocation;
	}

	public void setAttachementLocation(String attachementLocation) {
		this.attachementLocation = attachementLocation;
	}

	@Override
	public String toString() {
		return "Alert [id=" + id + ", txnType=" + txnType + ", fetchStatus="
				+ fetchStatus + ", message=" + message + ", mobile=" + mobile
				+ ", appl=" + appl + ", mailTo=" + mailTo + ", mailFrom="
				+ mailFrom + ", subject=" + subject + ", retryCount="
				+ retryCount + ", messageDate=" + messageDate
				+ ", deliveryStatus=" + deliveryStatus + "]";
	}

}
