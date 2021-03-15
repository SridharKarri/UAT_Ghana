package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "APP_VERSION")
public class AppVersion {

	@Id
	@GeneratedValue
	private Integer id;
	private String channelId;
	private String minVersion;
	private String message;
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	private String creator;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getMinVersion() {
		return minVersion;
	}
	

	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}
	

	@Override
	public String toString() {
		return "AppVersion [id=" + id + ", channelId=" + channelId + ", minVersion=" + minVersion + ", message=" + message
				+ ", createdDate=" + createdDate + ", creator=" + creator + "]";
	}

}
