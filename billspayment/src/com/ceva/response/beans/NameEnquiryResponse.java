package com.ceva.response.beans;

public class NameEnquiryResponse {

	private String accountName;
	private String accountNumber;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "NameEnquiryResponse [accountName=" + accountName + ", accountNumber=" + accountNumber + "]";
	}

}
