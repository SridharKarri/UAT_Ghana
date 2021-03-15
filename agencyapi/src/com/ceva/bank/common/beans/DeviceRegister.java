package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;

public class DeviceRegister {

	@NotNull(message = "This Field is Reqired.")
	private long channel;
	@NotNull(message = "This Field is Reqired.")
	private String userId;
	//@NotNull(message = "This Field is Reqired.")
	private String mobileNumber;
	@NotNull(message = "This Field is Reqired.")
	private String pin;
	@NotNull(message = "This Field is Reqired.")
	private String secans1;
	@NotNull(message = "This Field is Reqired.")
	private String secans2;
	//@NotNull(message = "This Field is Reqired.")
	private String secans3;
	@NotNull(message = "This Field is Reqired.")
	private String macAddr;
	@NotNull(message = "This Field is Reqired.")
	private String deviceIp;
	@NotNull(message = "This Field is Reqired.")
	private String imeiNo;
	@NotNull(message = "This Field is Reqired.")
	private String serialNo;
	@NotNull(message = "This Field is Reqired.")
	private String version;
	@NotNull(message = "This Field is Reqired.")
	private String deviceType;
	@NotNull(message = "This Field is Reqired.")
	private String otp;
	private String language;
	
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

	/*
	 * public String getMerchantId() { return merchantId; } public void
	 * setMerchantId(String merchantId) { this.merchantId = merchantId; }
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getSecans1() {
		return secans1;
	}

	public void setSecans1(String secans1) {
		this.secans1 = secans1;
	}

	public String getSecans2() {
		return secans2;
	}

	public void setSecans2(String secans2) {
		this.secans2 = secans2;
	}

	public String getSecans3() {
		return secans3;
	}

	public void setSecans3(String secans3) {
		this.secans3 = secans3;
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
	
	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "DeviceRegister [channel=" + channel + ", userId=" + userId
				+ ", mobileNumber=" + mobileNumber + ", pin=" + pin
				+ ", secans1=" + secans1 + ", secans2=" + secans2
				+ ", secans3=" + secans3 + ", macAddr=" + macAddr
				+ ", deviceIp=" + deviceIp + ", imeiNo=" + imeiNo
				+ ", serialNo=" + serialNo + ", version=" + version
				+ ", deviceType=" + deviceType + "]";
	}

}
