package com.ceva.bank.common.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cashWithdrawalConfirm")
public class CashWithdrawalConfirm implements Serializable{

	private long channel;
	private String makerId;
	private String merchantId;
	private String mobileNumber;
	private String customerAccountNumber;
	private String customerName;
	private String pin;
	private String otp;
	private String txnRef;
	private double amount;
	private String narration;
	private String serviceId;
	private String txnCode;

	public long getChannel() {
		return channel;
	}
	public void setChannel(long channel) {
		this.channel = channel;
	}
	public String getMakerId() {
		return makerId;
	}
	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCustomerAccountNumber() {
		return customerAccountNumber;
	}
	public void setCustomerAccountNumber(String customerAccountNumber) {
		this.customerAccountNumber = customerAccountNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getTxnRef() {
		return txnRef;
	}
	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getTxnCode() {
		return txnCode;
	}
	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}
	@Override
	public String toString() {
		return "CashWithdrawalConfirm [channel=" + channel + ", makerId="
				+ makerId + ", merchantId=" + merchantId + ", mobileNumber="
				+ mobileNumber + ", customerAccountNumber="
				+ customerAccountNumber + ", customerName=" + customerName
				+ ", pin=" + pin + ", otp=" + otp + ", txnRef=" + txnRef + "]";
	}


}
