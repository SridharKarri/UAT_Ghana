package com.ceva.bank.common.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cashWithdrawalInit")
public class CashWithdrawalInit implements Serializable{

	private String channel;
	private String makerId;
	private String merchantId;
	private String mobileNumber;
	private String serviceId;
	private String customerAccountNumber;
	private String customerName;
	private String customerPhone;
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
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
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	
	@Override
	public String toString() {
		return "CashWithdrawalInit [channel=" + channel + ", makerId="
				+ makerId + ", merchantId=" + merchantId + ", mobileNumber="
				+ mobileNumber + ", serviceId=" + serviceId
				+ ", customerAccountNumber=" + customerAccountNumber
				+ ", customerName=" + customerName + "]";
	}

}