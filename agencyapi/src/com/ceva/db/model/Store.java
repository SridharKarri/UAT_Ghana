package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nbk.util.CommonUtil;

@Entity
@Table(name = "STORE_MASTER")
public class Store {
	@Id
	@Column(name = "STORE_ID")
	private String storeId;
	@Column(name = "STORE_NAME")
	private String storeName;
	@Column(name = "ADDRESS")
	private String location;
	@Column(name = "LATITUDE")
	private String latitude;
	@Column(name = "LONGITUDE")
	private String longitude;
	@Column(name = "TELEPHONE_NO")
	private String phone;
	@Column(name = "B_OWNER_NAME")
	private String contactPerson;
	@Column(name = "CITY")
	private String city;
	@Column(name = "STATUS")
	private String status;

	@Column(name = "ACCNUM")
	private String accountNumber;
	@Column(name = "ACCNAME")
	private String accountName;

	@Column(name = "SOLID")
	private String solId;

	@Column(name = "CURRENCY")
	private String currency;

	public Store() {
		super();
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getSolId() {
		return solId;
	}

	public void setSolId(String solId) {
		this.solId = solId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Store [storeId=" + storeId + ", storeName=" + storeName + ", location=" + location + ", latitude="
				+ latitude + ", longitude=" + longitude + ", phone=" + phone + ", contactPerson=" + contactPerson
				+ ", city=" + city + ", status=" + status + ", accouuntNumber="
				+ CommonUtil.maskAccountNumber(accountNumber, "*") + ", accountName=" + accountName + ", currency="+currency+"]";
	}

}
