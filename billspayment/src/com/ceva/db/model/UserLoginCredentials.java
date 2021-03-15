package com.ceva.db.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER_LOGIN_CREDENTIALS")
@SecondaryTable(name = "USER_INFORMATION")
public class UserLoginCredentials {

	@Id
	@GeneratedValue
	@Column(name = "COMMON_ID")
	private String commonId;

	@NotNull(message = "This Field Is Required.")
	@Size(min = 1, max = 20, message = "This field should be 1- 20 Characters")
	@Column(name = "LOGIN_USER_ID")
	private String userId;
	
	/*@Column(name = "USER_ID_OLD")
	private String userIdOld;*/

	@Column(name = "PASSWORD")
	private String password;

	@NotNull(message = "This Field Is Required.")
	@Column(name = "PIN")
	private String pin;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "LAST_LOGED_IN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoggedIn;

	@Column(name = "INCORRECT_PASSWD_CNT")
	private Long invalidPwdCount= new Long(0);

	@Column(name = "USER_NAME", table = "USER_INFORMATION")
	private String userName;

	@Column(name = "USER_STATUS", table = "USER_INFORMATION")
	private String userStatus;

	@Column(name = "EMAIL", table = "USER_INFORMATION")
	private String email;

	@Column(name = "USER_TYPE", table = "USER_INFORMATION")
	private String userType;

	@NotNull(message = "This Field Is Required.")
	@Column(name = "MOBILE_NO", table = "USER_INFORMATION")
	private String mobileNo;

	@Column(name = "STORE_ID", table = "USER_INFORMATION")
	private String store;

	@Column(name = "MERCHANT_ID", table = "USER_INFORMATION")
	private String agent;

	@Column(name = "SECANS1", table = "USER_INFORMATION")
	private String secAns1;

	@Column(name = "SECANS2", table = "USER_INFORMATION")
	private String secAns2;

	@Column(name = "SECANS3", table = "USER_INFORMATION")
	private String secAns3;

	@Column(name = "PROFILE_PIC", table = "USER_INFORMATION")
	private String pic;


	public UserLoginCredentials() {
		super();
	}

	public UserLoginCredentials(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

	public UserLoginCredentials(String userId, String pin, String userStatus) {
		super();
		this.userId = userId;
		this.pin = pin;
		this.userStatus = userStatus;
	}

	public String getCommonId() {
		return commonId;
	}

	public void setCommonId(String commonId) {
		this.commonId = commonId;
	}

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

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
/*
	public Set<ChannelUsers> getChannelUsers() {
		return channelUsers;
	}

	public void setChannelUsers(Set<ChannelUsers> channelUsers) {
		this.channelUsers = channelUsers;
	}*/

	public String getSecAns1() {
		return secAns1;
	}

	public void setSecAns1(String secAns1) {
		this.secAns1 = secAns1;
	}

	public String getSecAns2() {
		return secAns2;
	}

	public void setSecAns2(String secAns2) {
		this.secAns2 = secAns2;
	}

	public String getSecAns3() {
		return secAns3;
	}

	public void setSecAns3(String secAns3) {
		this.secAns3 = secAns3;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getInvalidPwdCount() {
		return invalidPwdCount;
	}

	public void setInvalidPwdCount(Long invalidPwdCount) {
		this.invalidPwdCount = invalidPwdCount;
	}

	@Override
	public String toString() {
		return "UserLoginCredentials [commonId=" + commonId + ", userId="
				+ userId + ", password=" + password + ", pin=" + pin
				+ ", status=" + status + ", lastLoggedIn=" + lastLoggedIn
				+ ", userName=" + userName + ", userStatus=" + userStatus
				+ ", userType=" + userType + ", email=" + email + ", mobileNo="
				+ mobileNo + ", store=" + store + ", invalidPwdCount="
				+ invalidPwdCount + ", agent=" + agent + ", pic=" + pic + "]";
	}
}
