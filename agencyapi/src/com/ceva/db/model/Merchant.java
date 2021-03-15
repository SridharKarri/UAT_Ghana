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

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "B_OWNER_NAME")
	private String managerName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "CITY")
	private String city;

	@Column(name = "LONGITUDE")
	private String langitude;

	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "MOBILE_NO")
	private String mobileNumber;

	@Column(name = "MAKER_ID")
	private String maker;

	@Column(name = "STATUS")
	private String Status;

	@Column(name = "SUPER_AGENT")
	private String superAgent;

	@Column(name = "SCHEMA_TYPE")
	private String schemaType;

	@Column(name = "ALERTSTATUS")
	private String notiFicationType;

	@Column(name = "BRANCH_LOCATION")
	private String solId;

	@Column(name = "CAN_OPEN_ACCOUNT")
	private String canOpenAccount;

	@Column(name = "CURRENCY")
	private String currency;

	@Transient
	private String telcoText;
	@Transient
	private String nationalityText;
	@Transient
	private String IDTypeText;
	@Transient
	private String genderText;
	@Transient
	private String merchantTypeVal;
	@Transient
	private String locationVal;
	@Transient
	private String productType;
	@Transient
	private String remoteAddr;

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

	public String getLocationVal() {
		return locationVal;
	}

	public void setLocationVal(String locationVal) {
		this.locationVal = locationVal;
	}

	public String getMerchantTypeVal() {
		return merchantTypeVal;
	}

	public void setMerchantTypeVal(String merchantTypeVal) {
		this.merchantTypeVal = merchantTypeVal;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getTelcoText() {
		return telcoText;
	}

	public void setTelcoText(String telcoText) {
		this.telcoText = telcoText;
	}

	public String getNationalityText() {
		return nationalityText;
	}

	public void setNationalityText(String nationalityText) {
		this.nationalityText = nationalityText;
	}

	public String getIDTypeText() {
		return IDTypeText;
	}

	public void setIDTypeText(String iDTypeText) {
		IDTypeText = iDTypeText;
	}

	public String getGenderText() {
		return genderText;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public void setGenderText(String genderText) {
		this.genderText = genderText;
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

	public String getSchemaType() {
		return schemaType;
	}

	public void setSchemaType(String schemaType) {
		this.schemaType = schemaType;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Merchant [merchantID=" + merchantID + ", merchantName=" + merchantName + ", product=" + product
				+ ", email=" + email + ", accountNumber=" + accountNumber + ", Status=" + Status + ", notiFicationType="
				+ notiFicationType + ", solId=" + solId + ", canOpenAccount=" + canOpenAccount + ", productType="
				+ productType + "]";
	}


}
