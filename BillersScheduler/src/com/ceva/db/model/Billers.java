package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(name = "BILLERS", uniqueConstraints = {@UniqueConstraint(columnNames = {"BILLERID", "CHANNEL", "STATUS"})})
public class Billers
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BILLER_ID")
	@SequenceGenerator(name = "BILLER_ID", sequenceName = "BILLER_ID", allocationSize = 50)
	private long id;
	private String billerId;
	private String billerName;
	private long sid;
	private long channel;
	private String status;

	public Billers(String billerId, String billerName, long sid) {
		this.billerId = billerId;
		this.billerName = billerName;
		this.sid = sid;
		this.status = "A";
	}



	public Billers() {}


	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBillerId() {
		return this.billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getBillerName() {
		return this.billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public long getSid() {
		return this.sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public long getChannel() {
		return this.channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


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
		builder.append("]");
		return builder.toString();
	}
}