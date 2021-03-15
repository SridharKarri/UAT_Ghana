package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="FRAUD_MONITOR")
public class Fraud {

	@Id
	@Column(name="SNO")
	private String id;
	@Column(name="REQUEST_URL")
	private String url;
	@Column(name="IP")
	private String ipAddress;
	@Column(name="ERROR_DESC")
	private String errorDescription;
	@Column(name="REQUEST_CHANNEL")
	private String userChannel;
	private String appId;
	@Column(name="FRAUD_DTTM")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime = new Date();

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getUserChannel() {
		return userChannel;
	}
	public void setUserChannel(String userChannel) {
		this.userChannel = userChannel;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	@Override
	public String toString() {
		return "Fraud [id=" + id + ", url=" + url + ", ipAddress=" + ipAddress
				+ ", errorDescription=" + errorDescription + ", userChannel="
				+ userChannel + ", appId=" + appId + "]";
	}


}
