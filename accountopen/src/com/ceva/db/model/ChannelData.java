package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_CHANNEL_DATA")
public class ChannelData {

	@Id
	@GeneratedValue
	private long id;
	private String commonId;
	private String macAddr;
	private String deviceIp;
	private String imeiNo;
	private String serialNo;
	private String version;
	private String deviceType;
	private String status;
	private String osType;
	private String pkStatus;
	private String pKey;
	private String pIv;
	private String appId;
	private String sessionId;
	private String pToken;
	private String sIv;
	private String pId;
	private long activeLogin=0;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCommonId() {
		return commonId;
	}

	public void setCommonId(String commonId) {
		this.commonId = commonId;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getPkStatus() {
		return pkStatus;
	}

	public void setPkStatus(String pkStatus) {
		this.pkStatus = pkStatus;
	}

	public String getpKey() {
		return pKey;
	}

	public void setpKey(String pKey) {
		this.pKey = pKey;
	}

	public String getpIv() {
		return pIv;
	}

	public void setpIv(String pIv) {
		this.pIv = pIv;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getpToken() {
		return pToken;
	}

	public void setpToken(String pToken) {
		this.pToken = pToken;
	}

	public String getsIv() {
		return sIv;
	}

	public void setsIv(String sIv) {
		this.sIv = sIv;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public long getActiveLogin() {
		return activeLogin;
	}

	public void setActiveLogin(long activeLogin) {
		this.activeLogin = activeLogin;
	}

	@Override
	public String toString() {
		return "ChannelData [id=" + id + ", commonId=" + commonId
				+ ", macAddr=" + macAddr + ", deviceIp=" + deviceIp
				+ ", imeiNo=" + imeiNo + ", serialNo=" + serialNo
				+ ", version=" + version + ", deviceType=" + deviceType
				+ ", status=" + status + ", osType=" + osType + ", pkStatus="
				+ pkStatus + ", pKey=" + pKey + ", pIv=" + pIv + ", appId="
				+ appId + ", sessionId=" + sessionId + ", pToken=" + pToken
				+ ", sIv=" + sIv + ", pId=" + pId + "]";
	}

}
