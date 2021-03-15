package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserBean {

	@NotNull(message = "This Field Is Required.")
	@Size(min = 1, max = 20, message = "This field should be 1- 20 Characters")
	private String userId;
	private String password;
	private String userStatus;
	private String makerId;
	private String makerDttm;
	@NotNull(message = "This Field Is Required.")
	@Size(min = 4, max = 20, message = "This field should be 2 Numeric Digits")
	private String pin;
	private String userLokTime;
	private String lastLogin;
	private String lastLoginLocatoion;

	@NotNull(message = "This Field Is Required.")
	@Pattern(regexp="((234)[0-9]{10})", message="Country code Should match And Length Should be 13 Numeric Digits")
	private String mobile;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getMakerId() {
		return makerId;
	}
	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}
	public String getMakerDttm() {
		return makerDttm;
	}
	public void setMakerDttm(String makerDttm) {
		this.makerDttm = makerDttm;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getUserLokTime() {
		return userLokTime;
	}
	public void setUserLokTime(String userLokTime) {
		this.userLokTime = userLokTime;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getLastLoginLocatoion() {
		return lastLoginLocatoion;
	}
	public void setLastLoginLocatoion(String lastLoginLocatoion) {
		this.lastLoginLocatoion = lastLoginLocatoion;
	}

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Override
	public String toString() {
		return "UserBean [userId=" + userId + ", password=" + password
				+ ", userStatus=" + userStatus + ", makerId=" + makerId
				+ ", makerDttm=" + makerDttm + ", pin=" + pin
				+ ", userLokTime=" + userLokTime + ", lastLogin=" + lastLogin
				+ ", lastLoginLocatoion=" + lastLoginLocatoion + ", mobile= "+mobile+"]";
	}


}
