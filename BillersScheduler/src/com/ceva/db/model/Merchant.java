package com.ceva.db.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

	@Column(name = "BRANCH_LOCATION")
	private String location;

	@Column(name = "AGENT_ID")
	private String kraPin;

	@Column(name = "MERCHANT_TYPE")
	private String merchantType;

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "B_OWNER_NAME")
	private String managerName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "ADDRESS")
	private String addressLine1;

	private String addressLine2;

	@Column(name = "PO_BOX_NO")
	private String localGovernment;

	@Column(name = "STATE")
	private String state;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "CITY")
	private String city;

	@Column(name = "TELEPHONE_NO")
	private String telephoneNumber1;

	@Column(name = "FAX_NO")
	private String telephoneNumber2;

	@Column(name = "POSTALCODE")
	private String dob;

	@Column(name = "LRNUMBER")
	private String gender;

	@Column(name = "LONGITUDE")
	private String langitude;

	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "PRI_MOB_NUM")
	private String IDType;

	@Column(name = "OTP")
	private String IDNumber;

	@Column(name = "TELCOTYPE")
	private String telco;

	@Column(name = "AGEN_OR_BILLER")
	private String nationality;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "RELATIONSHIP_MANAGER_NUMBER")
	private String relationShipManagerNumber;

	@Column(name = "RELATIONSHIP_MANAGER_NAME")
	private String relationShipManagerName;

	@Column(name = "RELATIONSHIP_MANAGER_EMAIL")
	private String relationShipManagerEmail;

	@Column(name = "PWD")
	private String password;

	@Column(name = "ENC_PWD")
	private String encryptPassword;

	@Column(name = "PIN")
	private String otp;

	@Column(name = "PRIMARY_PERSON")
	private String prmContactPerson;

	@Column(name = "PRIMARY_CONTACT_NUM")
	private String prmContactNumber;

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

	/*
	 * @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY) private
	 * Set<Store> stores;
	 */

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationVal() {
		return locationVal;
	}

	public void setLocationVal(String locationVal) {
		this.locationVal = locationVal;
	}

	public String getKraPin() {
		return kraPin;
	}

	public void setKraPin(String kraPin) {
		this.kraPin = kraPin;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
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

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getLocalGovernment() {
		return localGovernment;
	}

	public void setLocalGovernment(String localGovernment) {
		this.localGovernment = localGovernment;
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

	public String getTelephoneNumber1() {
		return telephoneNumber1;
	}

	public void setTelephoneNumber1(String telephoneNumber1) {
		this.telephoneNumber1 = telephoneNumber1;
	}

	public String getTelephoneNumber2() {
		return telephoneNumber2;
	}

	public void setTelephoneNumber2(String telephoneNumber2) {
		this.telephoneNumber2 = telephoneNumber2;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getIDType() {
		return IDType;
	}

	public void setIDType(String iDType) {
		IDType = iDType;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
	}

	public String getTelco() {
		return telco;
	}

	public void setTelco(String telco) {
		this.telco = telco;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getRelationShipManagerNumber() {
		return relationShipManagerNumber;
	}

	public void setRelationShipManagerNumber(String relationShipManagerNumber) {
		this.relationShipManagerNumber = relationShipManagerNumber;
	}

	public String getRelationShipManagerName() {
		return relationShipManagerName;
	}

	public void setRelationShipManagerName(String relationShipManagerName) {
		this.relationShipManagerName = relationShipManagerName;
	}

	public String getRelationShipManagerEmail() {
		return relationShipManagerEmail;
	}

	public void setRelationShipManagerEmail(String relationShipManagerEmail) {
		this.relationShipManagerEmail = relationShipManagerEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptPassword() {
		return encryptPassword;
	}

	public void setEncryptPassword(String encryptPassword) {
		this.encryptPassword = encryptPassword;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPrmContactPerson() {
		return prmContactPerson;
	}

	public void setPrmContactPerson(String prmContactPerson) {
		this.prmContactPerson = prmContactPerson;
	}

	public String getPrmContactNumber() {
		return prmContactNumber;
	}

	public void setPrmContactNumber(String prmContactNumber) {
		this.prmContactNumber = prmContactNumber;
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

	@Override
	public String toString() {
		return "Merchant [merchantID=" + merchantID + ", merchantName="
				+ merchantName + ", location=" + location + ", kraPin="
				+ kraPin + ", merchantType=" + merchantType + ", product="
				+ product + ", managerName=" + managerName + ", email=" + email
				+ ", addressLine1=" + addressLine1 + ", addressLine2="
				+ addressLine2 + ", localGovernment=" + localGovernment
				+ ", state=" + state + ", country=" + country + ", city="
				+ city + ", telephoneNumber1=" + telephoneNumber1
				+ ", telephoneNumber2=" + telephoneNumber2 + ", dob=" + dob
				+ ", gender=" + gender + ", langitude=" + langitude
				+ ", latitude=" + latitude + ", IDType=" + IDType
				+ ", IDNumber=" + IDNumber + ", telco=" + telco
				+ ", nationality=" + nationality + ", accountNumber="
				+ accountNumber + ", relationShipManagerNumber="
				+ relationShipManagerNumber + ", relationShipManagerName="
				+ relationShipManagerName + ", relationShipManagerEmail="
				+ relationShipManagerEmail + ", password=" + password
				+ ", encryptPassword=" + encryptPassword + ", otp=" + otp
				+ ", prmContactPerson=" + prmContactPerson
				+ ", prmContactNumber=" + prmContactNumber + ", mobileNumber="
				+ mobileNumber + ", maker=" + maker + ", Status=" + Status
				+ ", superAgent=" + superAgent + ",, telcoText=" + telcoText
				+ ", nationalityText=" + nationalityText + ", IDTypeText="
				+ IDTypeText + ", genderText=" + genderText
				+ ", merchantTypeVal=" + merchantTypeVal + ", locationVal="
				+ locationVal + ", productType=" + productType
				+ ", remoteAddr=" + remoteAddr + ", notiFicationType = "
				+ notiFicationType + "]";
	}

}
