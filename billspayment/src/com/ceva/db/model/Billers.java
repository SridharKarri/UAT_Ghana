package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="BILLERS", uniqueConstraints={@UniqueConstraint(columnNames={"BILLERID", "CHANNEL", "STATUS"})})
public class Billers {

	@Id
	private long id;
	private String billerId;
	private String billerName;
	private long sid;
	private long channel;
	private String status;
	private String frbillerName;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getBillerName() {
		return billerName;
	}
	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}
	public long getChannel() {
		return channel;
	}
	public void setChannel(long channel) {
		this.channel = channel;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getFrbillerName() {
		return frbillerName;
	}
	public void setFrbillerName(String frbillerName) {
		this.frbillerName = frbillerName;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Billers [id=");
		builder.append(id);
		builder.append(", billerId=");
		builder.append(billerId);
		builder.append(", billerName=");
		builder.append(billerName);
		builder.append(", sid=");
		builder.append(sid);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", status=");
		builder.append(status);
		builder.append(", frbillerName=");
		builder.append(frbillerName);
		builder.append("]");
		return builder.toString();
	}

}
