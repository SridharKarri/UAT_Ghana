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

	@NotNull(message="This Field Is Required.")
	private Long billerId;

	@NotNull(message="This Field Is Required.")
	@Size(min=3, max=120, message="This field should be 3 - 120 Characters")
	private String userId;

	//@NotNull(message="This Field Is Required.")
	private String countryId;

	public BillePaymentItems() {
		super();
	}

	public long getChannel() {
		return channel;
	}
	
	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
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

	public Long getBillerId() {
		return billerId;
	}

	public void setBillerId(Long billerId) {
		this.billerId = billerId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BillePaymentItems [id=");
		builder.append(id);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", billerId=");
		builder.append(billerId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", countryId=");
		builder.append(countryId);
		builder.append("]");
		return builder.toString();
	}

}
