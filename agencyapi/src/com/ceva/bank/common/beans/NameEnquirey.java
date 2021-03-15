package com.ceva.bank.common.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Enquirey")
public class NameEnquirey {

	private String bankCode;

	private String accountNumber;

	private long channel;

	private String merchantId;

	private String userId;

	private String mobileNumber;

	private String currency;
	
	private String language;

	public NameEnquirey() {
		super();
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	

	public String getLanguage() {
		return language;
	}
	

	public void setLanguage(String language) {
		this.language = language;
	}
	

	@Override
	public String toString() {
		return "NameEnquirey [bankCode=" + bankCode + ", accountNumber=" + accountNumber + ", channel=" + channel
				+ ", merchantId=" + merchantId + ", userId=" + userId + ", mobileNumber=" + mobileNumber + "]";
	}

}
