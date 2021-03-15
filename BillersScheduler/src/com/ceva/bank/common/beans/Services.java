package com.ceva.bank.common.beans;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Services")
public class Services {

	private long channel;

	private String merchantId;

	private String userId;

	private String mobileNumber;

	public Services() {
		super();
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
		return "Services [channel=" + channel + ", merchantId=" + merchantId
				+ ", userId=" + userId + ", mobileNumber=" + mobileNumber + "]";
	}

}
