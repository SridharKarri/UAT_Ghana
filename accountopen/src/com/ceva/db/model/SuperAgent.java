package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUPER_AGENT")
public class SuperAgent {

	@Id
	@Column(name = "ID")
	private String superAgentId;

	@Column(name = "MERCHANTTYPE")
	private String managerName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ACCOUNTNUMBER")
	private String accountNumber;

	@Column(name = "MOBILENUMBER")
	private String mobileNumber;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "FEEVAL")
	private String feeVal;

	@Column(name = "FEETYPE")
	private String feeType;

	public SuperAgent() {
		super();
	}

	public SuperAgent(String feeVal, String feeType) {
		super();
		this.feeVal = feeVal;
		this.feeType = feeType;
	}

	public String getSuperAgentId() {
		return superAgentId;
	}

	public void setSuperAgentId(String superAgentId) {
		this.superAgentId = superAgentId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeeVal() {
		return feeVal;
	}

	public void setFeeVal(String feeVal) {
		this.feeVal = feeVal;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	@Override
	public String toString() {
		return "SuperAgent [superAgentId=" + superAgentId + ", email=" + email 
				+ ", status=" + status + ", feeVal=" + feeVal + ", feeType=" + feeType
				+ "]";
	}

}