package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "changePin")
public class ChangePin {

	@NotNull(message = "This Field Is Required.")
	private String userId;
	@NotNull(message = "This Field Is Required.")
	private String oldPin;
	@NotNull(message = "This Field Is Required.")
	private String newPin;
	private String mobileNo;
	@NotNull(message = "This Field Is Required.")
	private String channel;
	private String merchantId;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOldPin() {
		return oldPin;
	}
	public void setOldPin(String oldPin) {
		this.oldPin = oldPin;
	}
	public String getNewPin() {
		return newPin;
	}
	public void setNewPin(String newPin) {
		this.newPin = newPin;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public String toString() {
		return "ChangePin [userId=" + userId + ", oldPin=" + oldPin
				+ ", newPin=" + newPin + ", mobileNo=" + mobileNo
				+ ", channel=" + channel + "]";
	}

}