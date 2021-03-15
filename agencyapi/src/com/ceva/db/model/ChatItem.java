package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="CHATITEM")
public class ChatItem {

	@Id
	@SequenceGenerator(name="CHAT_ID", sequenceName="CHAT_ID", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CHAT_ID")
	private long id;
	private String message;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	private String userId;
	private String channel;
	private String responseMessage;
	private String makerId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date responseTime;

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
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	@Override
	public String toString() {
		return "ChatItem [id=" + id + ", message=" + message + ", status="
				+ status + ", creationDate=" + creationDate + ", userId="
				+ userId + ", channel=" + channel + ", responseMessage="
				+ responseMessage + ", makerId=" + makerId + ", responseTime="
				+ responseTime + "]";
	}

}
