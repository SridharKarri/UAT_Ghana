package com.ceva.bank.common.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "appVersion")
public class AppVersion {

	private String channel;
	private String userId;
	private String appId;
	private String version;

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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "AppVersion [channel=" + channel + ", userId=" + userId + ", appId=" + appId + ", version=" + version
				+ "]";
	}

}
