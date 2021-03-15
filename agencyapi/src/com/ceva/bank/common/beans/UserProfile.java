package com.ceva.bank.common.beans;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="userProfile")
public class UserProfile implements Serializable {

	@NotNull(message = "This Field Is Required.")
	private String userId;
	@NotNull(message = "This Field Is Required.")
	private String merchantId;
	@NotNull(message = "This Field Is Required.")
	private String mobileNumber;
	@NotNull(message = "This Field Is Required.")
	private String channel;

	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


}
