package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.print.attribute.standard.Severity;

@Entity
@Table(name="BILLERS", uniqueConstraints={@UniqueConstraint(columnNames={"BILLERID", "CHANNEL", "STATUS"})})
public class Billers {

	@Id
	private long id;
	private String billerId;
	private String billerName;
	private long sid;
	private long channel;
	private String acountNumber;
	private String accountNumber;
	private String billerDesc;
	private String supportEmail;
	private String supportContact;
	private String address;
	private String status;

	/*@ManyToOne
	@JoinColumn(name="sid")
	private Service service;*/

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
	public String getAcountNumber() {
		return acountNumber;
	}
	public void setAcountNumber(String acountNumber) {
		this.acountNumber = acountNumber;
	}
	public String getBillerDesc() {
		return billerDesc;
	}
	public void setBillerDesc(String billerDesc) {
		this.billerDesc = billerDesc;
	}
	public String getSupportEmail() {
		return supportEmail;
	}
	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}
	public String getSupportContact() {
		return supportContact;
	}
	public void setSupportContact(String supportContact) {
		this.supportContact = supportContact;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	@Override
	public String toString() {
		return "Billers [id=" + id + ", billerId=" + billerId + ", billerName="
				+ billerName + ", channel=" + channel
				+ ", acountNumber=" + acountNumber + ", billerDesc="
				+ billerDesc + ", supportEmail=" + supportEmail
				+ ", supportContact=" + supportContact + ", address=" + address
				+ ", status=" + status + "]";
	}


}
