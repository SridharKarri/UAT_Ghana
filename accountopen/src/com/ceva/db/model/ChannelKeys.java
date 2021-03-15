package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MOB_CHANNELENCKEYS")
public class ChannelKeys {

	@Id
	@GeneratedValue
	@Column(name = "CHANNEL_ID")
	private long channelId;
	@Column(name = "CHANNEL_NAME")
	private String channelName;
	@Column(name = "KEY_ID")
	private String keyId;
	@Column(name = "PREV_KEY")
	private String prevKey;
	@Column(name = "ENC_KEY")
	private String encKey;

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getPrevKey() {
		return prevKey;
	}

	public void setPrevKey(String prevKey) {
		this.prevKey = prevKey;
	}

	public String getEncKey() {
		return encKey;
	}

	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}

	@Override
	public String toString() {
		return "ChannelKeys [channelId=" + channelId + ", channelName="
				+ channelName + ", keyId=" + keyId + ", prevKey=" + prevKey
				+ ", encKey=" + encKey + "]";
	}

}
