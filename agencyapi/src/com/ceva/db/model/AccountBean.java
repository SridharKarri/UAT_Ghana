package com.ceva.db.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Accounts")
public class AccountBean implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long Id;

	private String salutation;

	private String firstName;

	private String lastName;

	private String midName;

	private String maritalStatus;

	private String dob;

	private String email;
	
	private String gender;
	
	private String state;
	
	private String city;
	
	private String address;
	
	private String phone;

	private long channel;

	private String merchantId;

	private String userId;
	
	private String mobileNumber;
	
	private String accountNumber;
	
	private String status;
	
	private String storeId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate=new Date();

	public AccountBean() {
		super();
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStoreId() {
		return storeId;
	}
	

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	

	public Date getCreationDate() {
		return creationDate;
	}
	

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	

	@Override
	public String toString() {
		return "AccountBean [salutation=" + salutation + ", firstName="
				+ firstName + ", lastName=" + lastName + ", midName=" + midName
				+ ", maritalStatus=" + maritalStatus + ", dob=" + dob
				+ ", email=" + email + ", gender=" + gender + ", state="
				+ state + ", city=" + city + ", address=" + address
				+ ", mobileNumber=" + mobileNumber + ", channel=" + channel
				+ ", merchantId=" + merchantId + ", userId=" + userId
				+ ", phone =" + phone + "]";
	}
}
