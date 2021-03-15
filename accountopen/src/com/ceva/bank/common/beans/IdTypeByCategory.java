package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;

@XmlRootElement(name = "IdTypes")
public class IdTypeByCategory {

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;
	@NotNull(message="This Field Is Required.")
	private String userId;
	@NotNull(message="This Field Is Required.")
	private String countryId;
	@NotNull(message="This Field Is Required.")
	private String catId;

	public IdTypeByCategory() {
		super();
	}

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Services [channel=");
		builder.append(channel);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", countryId=");
		builder.append(countryId);
		builder.append(", catId=");
		builder.append(catId);
		builder.append("]");
		return builder.toString();
	}
}
