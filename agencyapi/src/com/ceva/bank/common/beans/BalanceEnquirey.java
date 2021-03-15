package com.ceva.bank.common.beans;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;

@XmlRootElement(name = "Enquirey")
public class BalanceEnquirey implements Serializable {

	/*@NotNull(message="This Field Is Required.")
	@Size(min=10, max=10, message="This field should be 10 Numeric Digits")*/
	private String accountNumber;

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;

	@NotNull(message="This Field Is Required.")
	private String merchantId;

	@NotNull(message="This Field Is Required.")
	private String userId;

	@NotNull(message="This Field Is Required.")
	private String mobileNumber;

	public BalanceEnquirey() {
		super();
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long string() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}


	public long getChannel() {
		return channel;
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

	@Override
	public String toString() {
		return "BalanceEnquirey [accountNumber=" + accountNumber + ", channel="
				+ channel + ", merchantId=" + merchantId + ", userId=" + userId
				+ ", mobileNumber=" + mobileNumber + "]";
	}



	}
