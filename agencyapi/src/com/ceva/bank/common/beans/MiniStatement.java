package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@XmlRootElement(name = "Statement")
public class MiniStatement {

	@Size(message = "This Field Should Not empty.")
	private String accountNumber;
	@NotNull(message = "This Field Should Not empty.")
	private String fromDate;
	@NotNull(message = "This Field Should Not empty.")
	private String endDate;

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;

	@NotNull(message="This Field Is Required.")
	private String merchantId;

	@NotNull(message="This Field Is Required.")
	private String userId;

	@NotNull(message="This Field Is Required.")
	private String mobileNumber;

	public MiniStatement() {
		super();
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	@Override
	public String toString() {
		return "MiniStatement [accountNumber=" + accountNumber + ", fromDate="
				+ fromDate + ", endDate=" + endDate + ", channel=" + channel
				+ ", merchantId=" + merchantId + ", userId=" + userId
				+ ", mobileNumber=" + mobileNumber + "]";
	}

}
