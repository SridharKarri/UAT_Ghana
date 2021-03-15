package com.ceva.bank.common.beans;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;

@XmlRootElement(name = "ValidateCustomer")
public class ValidateCustomer {
	
	@Size(message = "Merchant Id Shuold Not empty.")
	private String merchantId;

	@Size(message = "Service Id Shuold Not empty.")
	@Length(min = 1, max = 4, message = "This Field Should between 1-4 Numeric Characters.")
	private String sid;

	@Size(message = "Channel Should Not empty.")
	@Length(min = 1, max = 5, message = "This Field Should between 1-5 Numeric Characters.")
	private String channel;

	@Size(message = "This Field Shuoul Not empty.")
	@Length(min = 10, max = 11, message = "This Field Should between 11 Numeric Characters.")
	private String mobileNumber;

	@Size(message = "This Field Shuoul Not empty.")
	private String customerId;
	
	@Size(message = "This Field Shuoul Not empty.")
	private String billerId;

	@Size(message = "MakerId Should Not empty.")
	@Length(min = 1, max = 120, message = "This Field Should between 1-120 Characters.")
	private String userId;
	
	@Size(message = "This Field Shuoul Not empty.")
	@Length(min = 1, max = 120, message = "This Field Should between 1-120 Characters.")
	private String paymentCode;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	


	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	@Override
	public String toString() {
		return "ValidateCustomer [merchantId=" + merchantId + ", sid=" + sid
				+ ", channel=" + channel + ", mobileNumber=" + mobileNumber
				+ ", customerId=" + customerId + ", billerId=" + billerId
				+ ", userId=" + userId + ", paymentCode ="+paymentCode+" ]";
	}


}
