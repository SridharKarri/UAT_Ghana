package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;

@XmlRootElement(name = "Billers")
public class BillersBean {

	@Range(min = 1, max = 2000000, message = "This field should be 1 - 2000000 Numbers")
	private long serviceId;

	private String billerChannel;

	@Range(min = 1, max = 5, message = "This field should be 1 - 5 Numbers")
	private long channel;

	@NotNull(message = "This Field Is Required.")
	@Size(min = 3, max = 120, message = "This field should be 3 - 120 Characters")
	private String userId;

	public BillersBean() {
		super();
	}

	public String getBillerChannel() {
		return billerChannel;
	}

	public void setBillerChannel(String billerChannel) {
		this.billerChannel = billerChannel;
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

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "BillersBean [serviceId=" + serviceId + ", billerChannel=" + billerChannel + ", channel=" + channel
				+ ", userId=" + userId + "]";
	}

}
