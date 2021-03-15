package com.ceva.bank.common.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Account")
public class QueryAccount {

	private String phone;
	private String email;
	private String accountNumber;
	private String accountName;
	private String schemeDesc;
	private String schemeType;
	private String subProductCode;
	private String branchCode;
	private String acctCurrCode;
	private String bvn;
	private String relationshipMgr;
	private String state;
	private String customerId;
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getSchemeDesc() {
		return schemeDesc;
	}

	public void setSchemeDesc(String schemeDesc) {
		this.schemeDesc = schemeDesc;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getSubProductCode() {
		return subProductCode;
	}

	public void setSubProductCode(String subProductCode) {
		this.subProductCode = subProductCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getAcctCurrCode() {
		return acctCurrCode;
	}

	public void setAcctCurrCode(String acctCurrCode) {
		this.acctCurrCode = acctCurrCode;
	}

	public String getBvn() {
		return bvn;
	}

	public void setBvn(String bvn) {
		this.bvn = bvn;
	}

	public String getRelationshipMgr() {
		return relationshipMgr;
	}

	public void setRelationshipMgr(String relationshipMgr) {
		this.relationshipMgr = relationshipMgr;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueryAccount [phone=");
		builder.append(phone);
		builder.append(", accountNumber=");
		builder.append(accountNumber);
		builder.append(", accountName=");
		builder.append(accountName);
		builder.append(", subProductCode=");
		builder.append(subProductCode);
		builder.append(", relationshipMgr=");
		builder.append(relationshipMgr);
		builder.append(", customerId=");
		builder.append(customerId);
		builder.append("]");
		return builder.toString();
	}


}
