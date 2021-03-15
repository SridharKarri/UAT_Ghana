package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "MERCHANT_MASTER")
public class Merchant {
	@Id
	@Column(name = "MERCHANT_ID")
	private String merchantID;

	@Column(name = "MERCHANT_NAME")
	private String merchantName;

	/*
	 * @Column(name = "BRANCH_LOCATION") private String location;
	 */

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "B_OWNER_NAME")
	private String managerName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "LONGITUDE")
	private String langitude;

	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "MOBILE_NO")
	private String mobileNumber;


	@Column(name = "STATUS")
	private String Status;

	@Column(name = "SUPER_AGENT")
	private String superAgent;

	@Column(name = "ALERTSTATUS")
	private String notiFicationType;

	@Column(name = "BRANCH_LOCATION")
	private String solId;
	
	@Column(name = "CAN_OPEN_ACCOUNT")
	private String canOpenAccount;

	/*
	 * @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY) private
	 * Set<Store> stores;
	 */


	public Merchant() {
		super();
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	/*
	 * public String getLocation() { return location; }
	 * 
	 * public void setLocation(String location) { this.location = location; }
	 */

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getLangitude() {
		return langitude;
	}

	public void setLangitude(String langitude) {
		this.langitude = langitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}


	public String getSuperAgent() {
		return superAgent;
	}

	public void setSuperAgent(String superAgent) {
		this.superAgent = superAgent;
	}

	public String getNotiFicationType() {
		return notiFicationType;
	}

	public void setNotiFicationType(String notiFicationType) {
		this.notiFicationType = notiFicationType;
	}

	public String getSolId() {
		return solId;
	}

	public void setSolId(String solId) {
		this.solId = solId;
	}

	public String getCanOpenAccount() {
		return canOpenAccount;
	}
	

	public void setCanOpenAccount(String canOpenAccount) {
		this.canOpenAccount = canOpenAccount;
	}
	
	@Override
	public String toString() {
		return "Merchant [merchantID=" + merchantID + ", merchantName=" + merchantName + ", product=" + product
				+ ", managerName=" + managerName + ", email=" + email + ", langitude=" + langitude + ", latitude="
				+ latitude + "," + " accountNumber=" + accountNumber + ", mobileNumber=" + mobileNumber + ", Status="
				+ Status + ", superAgent=" + superAgent + ", notiFicationType=" + notiFicationType + ", solId=" + solId
				+ "]";
	}

}
