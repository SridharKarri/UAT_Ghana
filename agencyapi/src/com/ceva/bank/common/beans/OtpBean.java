package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Otp")
public class OtpBean {

	@NotNull(message = "This Field Is Required.")
	@Size(min = 1, max = 2, message = "This field should be 2 Numeric Digits")
	private String channel;

	@NotNull(message = "This Field Is Required.")
	private String userId;

	private String merchantId;

	private String mobileNumber;

	private String txncode;

	private String narration;

	private String refNumber;

	private String otp;

	private String accountNumber;

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

	public String getTxncode() {
		return txncode;
	}

	public void setTxncode(String txncode) {
		this.txncode = txncode;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "OtpBean [channel=" + channel + ", mobileNumber=" + mobileNumber + ", txncode=" + txncode
				+ ", narration=" + narration + ", userId=" + userId + ", refNumber=" + refNumber + ", otp=" + otp + "]";
	}

}
