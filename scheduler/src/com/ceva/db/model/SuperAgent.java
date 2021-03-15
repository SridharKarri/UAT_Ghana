package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUPER_AGENT")
public class SuperAgent {

	@Id
	private long id;
	private String accountNumber;
	private String accountName;
	private String status;
	private String mobileNumber;
	private String email;
	private String cbnAgentId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCbnAgentId() {
		return cbnAgentId;
	}

	public void setCbnAgentId(String cbnAgentId) {
		this.cbnAgentId = cbnAgentId;
	}

	@Override
	public String toString() {
		return "SuperAgent [id=" + id + ", accountNumber=" + accountNumber
				+ ", accountName=" + accountName + ", status=" + status
				+ ", mobileNumber=" + mobileNumber + ", email=" + email
				+ ", cbnAgentId=" + cbnAgentId + "]";
	}

}
