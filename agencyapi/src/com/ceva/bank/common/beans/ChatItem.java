package com.ceva.bank.common.beans;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

public class ChatItem extends UserProfile{

	private long id;
	@NotNull(message = "This Field Is Required.")
	private String message;
	private String responseMessage;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime = new Date();

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}


	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "ChatItem [id=" + id + ", message=" + message + ", status="
				+ status + ", dateTime=" + dateTime + ", channel="
				+ getChannel() + ", UserId=" + getUserId()+ ",MerchantId="
				+ getMerchantId() + ", getMobileNumber()=" + getMobileNumber()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
