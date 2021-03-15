package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;
@XmlRootElement(name="BillePaymentItems")
public class BillePaymentItems {

	private long id;

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;

	//@NotNull(message="This Field Is Required.")
	//@Size(min=3, max=20, message="This field should be 3 - 20 Characters")
	private String billerId;

	@NotNull(message="This Field Is Required.")
	@Size(min=3, max=120, message="This field should be 3 - 120 Characters")
	private String merchantId;

	@NotNull(message="This Field Is Required.")
	@Size(min=3, max=120, message="This field should be 3 - 120 Characters")
	private String userId;

	@NotNull(message="This Field Is Required.")
	@Size(min=10, max=10, message="This field should be 10 Numeric Digits")
	private String mobileNumber;

	public BillePaymentItems() {
		super();
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

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	@Override
	public String toString() {
		return "BillePaymentItems [id=" + id + ", channel=" + channel
				+ ", billerId=" + billerId + ", merchantId=" + merchantId
				+ ", userId=" + userId + ", mobileNumber=" + mobileNumber + "]";
	}

}
