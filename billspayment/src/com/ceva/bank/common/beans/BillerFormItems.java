package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;
@XmlRootElement(name="BillePaymentItems")
public class BillerFormItems {

	private long id;

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;

	@NotNull(message="This Field Is Required.")
	private Long packId;

	@NotNull(message="This Field Is Required.")
	@Size(min=3, max=120, message="This field should be 3 - 120 Characters")
	private String userId;

	//@NotNull(message="This Field Is Required.")
	private String countryId;

	public BillerFormItems() {
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


	public Long getPackId() {
		return packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BillePaymentItems [id=");
		builder.append(id);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", packId=");
		builder.append(packId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", countryId=");
		builder.append(countryId);
		builder.append("]");
		return builder.toString();
	}

}
