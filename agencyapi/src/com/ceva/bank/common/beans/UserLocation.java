package com.ceva.bank.common.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userLocation")
public class UserLocation {

	private String latitude;
	private String longitude;
	private String channel;
	private String userId;
	private String storeId;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

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

	public String getStoreId() {
		return storeId;
	}
	

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	

	@Override
	public String toString() {
		return "UserLocation [latitude=" + latitude + ", longitude=" + longitude + ", channel=" + channel + ", userId="
				+ userId + "]";
	}
}
