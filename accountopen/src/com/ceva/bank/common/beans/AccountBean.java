package com.ceva.bank.common.beans;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@XmlRootElement(name = "Account")
public class AccountBean implements Serializable {

	
	@Size(message = "This Field Should Not empty.")
	private String dob;

	@Size(message = "This Field Should Not empty.")
	private String email;

	@Size(message = "This Field Should Not empty.")
	@Length(min = 2, max = 40, message = "This Field Should be 2 - 40 Characters.")
	private String firstName;

	@Size(message = "This Field Should Not empty.")
	private String lastName;

	@Size(message = "This Field Should Not empty.")
	private String midName;

	@Size(message = "This Field Should Not empty.")
	private String maritalStatus;

	@Size(message = "This Field Should Not empty.")
	private String gender;

	@Size(message = "This Field Should Not empty.")
	private String state;

	@Size(message = "This Field Should Not empty.")
	private String city;
	
	@Size(message = "This Field Should Not empty.")
	private String country;

	@Size(message = "This Field Should Not empty.")
	private String address;
	
	@Size(message = "This Field Should Not empty.")
	private String houseNumber;
	
	@Size(message = "This Field Should Not empty.")
	private String occupation;
	
	@Size(message = "This Field Should Not empty.")
	private String streetName;
	
	@Size(message = "This Field Should Not empty.")
	private String salutation;
	
	@Size(message = "This Field Should Not empty.")
	private String nationality;

	@Size(message = "This Field Should Not empty.")
	private String idNumber;
	
	@Size(message = "This Field Should Not empty.")
	private String idType;
	
	@Size(message = "This Field Should Not empty.")
	private String idRefNumber;
	
	@Size(message = "This Field Should Not empty.")
	private String phone;
	
	private String idImage;

	private String mandateCard;

	@Range(min = 1, max = 5, message = "This field should be 1 - 5 Numbers")
	private long channel;

	private String merchantId;

	@NotNull(message = "This Field Is Required.")
	private String userId;

	@NotNull(message = "This Field Is Required.")
	private String pin;

	@NotNull(message = "This Field Is Required.")
	private String otp;

	private String mobileNumber;

	private String status;

	private String accountNumber;

	@NotNull(message = "This Field Is Required.")
	private String solId;

	public AccountBean() {
		super();
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

	public String getMandateCard() {
		return mandateCard;
	}

	public void setMandateCard(String mandateCard) {
		this.mandateCard = mandateCard;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getSolId() {
		return solId;
	}

	public void setSolId(String solId) {
		this.solId = solId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdImage() {
		return idImage;
	}

	public void setIdImage(String idImage) {
		this.idImage = idImage;
	}

	public String getIdRefNumber() {
		return idRefNumber;
	}

	public void setIdRefNumber(String idRefNumber) {
		this.idRefNumber = idRefNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountBean [dob=");
		builder.append(dob);
		builder.append(", email=");
		builder.append(email);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", midName=");
		builder.append(midName);
		builder.append(", maritalStatus=");
		builder.append(maritalStatus);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", state=");
		builder.append(state);
		builder.append(", city=");
		builder.append(city);
		builder.append(", country=");
		builder.append(country);
		builder.append(", address=");
		builder.append(address);
		builder.append(", houseNumber=");
		builder.append(houseNumber);
		builder.append(", occupation=");
		builder.append(occupation);
		builder.append(", streetName=");
		builder.append(streetName);
		builder.append(", salutation=");
		builder.append(salutation);
		builder.append(", nationality=");
		builder.append(nationality);
		builder.append(", idNumber=");
		builder.append(idNumber);
		builder.append(", idType=");
		builder.append(idType);
		builder.append(", idImage=");
		builder.append(idImage);
		builder.append(", mandateCard=");
		builder.append(mandateCard);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", merchantId=");
		builder.append(merchantId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", pin=");
		builder.append(pin);
		builder.append(", otp=");
		builder.append(otp);
		builder.append(", mobileNumber=");
		builder.append(mobileNumber);
		builder.append(", status=");
		builder.append(status);
		builder.append(", accountNumber=");
		builder.append(accountNumber);
		builder.append(", solId=");
		builder.append(solId);
		builder.append(", phone=");
		builder.append(phone);
		builder.append("]");
		return builder.toString();
	}

}
