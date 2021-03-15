package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;
@XmlRootElement(name = "Services")
public class Services {

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;

	@NotNull(message="This Field Is Required.")
	@Size(min=3, max=120, message="This field should be 3 - 120 Characters")
	private String userId;

	private String countryId;

	public Services() {
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Services [channel=");
		builder.append(channel);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", countryId=");
		builder.append(countryId);
		builder.append("]");
		return builder.toString();
	}
}
