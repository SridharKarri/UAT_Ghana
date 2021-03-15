package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYNC_PROPERTIES")
public class SyncProperties {

	@Id
	@GeneratedValue
	private Long Id;
	private String propertyName;
	private Long syncStatus;
	private Date sysnDate;
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Long getSyncStatus() {
		return syncStatus;
	}
	public void setSyncStatus(Long syncStatus) {
		this.syncStatus = syncStatus;
	}
	public Date getSysnDate() {
		return sysnDate;
	}
	public void setSysnDate(Date sysnDate) {
		this.sysnDate = sysnDate;
	}
	@Override
	public String toString() {
		return "SyncProperties [Id=" + Id + ", propertyName=" + propertyName
				+ ", syncStatus=" + syncStatus + ", sysnDate=" + sysnDate + "]";
	}
	
}
