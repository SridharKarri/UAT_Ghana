package com.ceva.bank.common.beans;

public abstract class TransactionRequestReqBean extends FundsRequestReqBean {

	protected String cardnumber = null;

	protected String pin = null;

	protected String mobileno = null;

	protected String emailid = null;

	protected String internetbanking = " ";

	protected String mobilebanking = " ";

	protected String firstname = null;

	protected String lastname = null;

	protected String yearofbirth = null;

	protected String producttype = null;

	protected String synchronizecheck = null;

	protected String channelid = null;

	protected String accesspointid = null;

	protected String transactionalcode = null;

	protected String docid = null;

	protected String id = null;

	protected String docType = null;

	protected String otherName = null;

	protected String dob = null;

	protected String citizenship = null;

	protected String clan = null;

	protected String dateofdeath = null;

	protected String dateofissue = null;

	protected String errordesc = null;

	protected String errorstatus = null;

	protected String ethnicgroup = null;

	protected String expirydate = null;

	protected String family = null;

	protected String fingerprint = null;

	protected String gender = null;

	protected String idnumber = null;

	protected String occupation = null;

	protected String ipin = null;

	protected String placeofbirth = null;

	protected String placeofdeath = null;

	protected String placeoflive = null;

	protected String regoffice = null;

	protected String serialnumber = null;

	protected String signature = null;

	protected String salutation = null;

	protected String photo = null;

	protected String msisdn = null;

	protected String accountFrom = null;

	protected String accountTo = null;

	protected String amount = null;

	protected String languageID = null;

	protected String referenceid = null;

	protected String otpcode = null;

	protected String loginid = null;

	protected String institute = null;

	protected String customerid = null;

	protected String oldpin = null;

	protected String newpin = null;

	protected String accountType = null;

	protected String typeoffund = null;

	protected Integer count = 10;

	protected String macaddress = null;

	protected String ipaddress = null;

	protected String imeinumbers = null;

	protected String socialnetworkid = null;

	protected Integer imeicount = 0;

	protected String disableflag = null;

	protected String customercode = null;

	// added parameters for open account in agency banking

	protected String agentid = null;

	protected String terminalid = null;

	protected String transactionAmount = null;

	protected String reason = null;

	protected String providername = null;

	protected String email = null;

	protected String imei = null;

	protected String ipadress = null;

	protected String sessid = null;

	protected String appid = null;

	protected String token = null;

	protected String motherName = null;

	protected String maritalStatus = null;

	protected String state = null;

	protected String accountNumber = null;

	protected String fromDate = null;

	protected String toDate = null;


	public String getToken() {
		return token;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getProvidername() {
		return providername;
	}

	public void setProvidername(String providername) {
		this.providername = providername;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getIpadress() {
		return ipadress;
	}

	public void setIpadress(String ipadress) {
		this.ipadress = ipadress;
	}

	public String getSessid() {
		return sessid;
	}

	public void setSessid(String sessid) {
		this.sessid = sessid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSocialnetworkid() {
		return socialnetworkid;
	}

	public void setSocialnetworkid(String socialnetworkid) {
		this.socialnetworkid = socialnetworkid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public TransactionRequestReqBean() {
	}

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getInternetbanking() {
		return internetbanking;
	}

	public void setInternetbanking(String internetbanking) {
		this.internetbanking = internetbanking;
	}

	public String getMobilebanking() {
		return mobilebanking;
	}

	public void setMobilebanking(String mobilebanking) {
		this.mobilebanking = mobilebanking;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getYearofbirth() {
		return yearofbirth;
	}

	public void setYearofbirth(String yearofbirth) {
		this.yearofbirth = yearofbirth;
	}

	public String getProducttype() {
		return producttype;
	}

	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}

	public String getSynchronizecheck() {
		return synchronizecheck;
	}

	public void setSynchronizecheck(String synchronizecheck) {
		this.synchronizecheck = synchronizecheck;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getAccesspointid() {
		return accesspointid;
	}

	public void setAccesspointid(String accesspointid) {
		this.accesspointid = accesspointid;
	}

	public String getTransactionalcode() {
		return transactionalcode;
	}

	public void setTransactionalcode(String transactionalcode) {
		this.transactionalcode = transactionalcode;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	public String getClan() {
		return clan;
	}

	public void setClan(String clan) {
		this.clan = clan;
	}

	public String getDateofdeath() {
		return dateofdeath;
	}

	public void setDateofdeath(String dateofdeath) {
		this.dateofdeath = dateofdeath;
	}

	public String getDateofissue() {
		return dateofissue;
	}

	public void setDateofissue(String dateofissue) {
		this.dateofissue = dateofissue;
	}

	public String getErrordesc() {
		return errordesc;
	}

	public void setErrordesc(String errordesc) {
		this.errordesc = errordesc;
	}

	public String getErrorstatus() {
		return errorstatus;
	}

	public void setErrorstatus(String errorstatus) {
		this.errorstatus = errorstatus;
	}

	public String getEthnicgroup() {
		return ethnicgroup;
	}

	public void setEthnicgroup(String ethnicgroup) {
		this.ethnicgroup = ethnicgroup;
	}

	public String getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getIpin() {
		return ipin;
	}

	public void setIpin(String ipin) {
		this.ipin = ipin;
	}

	public String getPlaceofbirth() {
		return placeofbirth;
	}

	public void setPlaceofbirth(String placeofbirth) {
		this.placeofbirth = placeofbirth;
	}

	public String getPlaceofdeath() {
		return placeofdeath;
	}

	public void setPlaceofdeath(String placeofdeath) {
		this.placeofdeath = placeofdeath;
	}

	public String getPlaceoflive() {
		return placeoflive;
	}

	public void setPlaceoflive(String placeoflive) {
		this.placeoflive = placeoflive;
	}

	public String getRegoffice() {
		return regoffice;
	}

	public void setRegoffice(String regoffice) {
		this.regoffice = regoffice;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}

	public String getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getLanguageID() {
		return languageID;
	}

	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}

	public String getReferenceid() {
		return referenceid;
	}

	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}

	public String getOtpcode() {
		return otpcode;
	}

	public void setOtpcode(String otpcode) {
		this.otpcode = otpcode;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getOldpin() {
		return oldpin;
	}

	public void setOldpin(String oldpin) {
		this.oldpin = oldpin;
	}

	public String getNewpin() {
		return newpin;
	}

	public void setNewpin(String newpin) {
		this.newpin = newpin;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getTypeoffund() {
		return typeoffund;
	}

	public void setTypeoffund(String typeoffund) {
		this.typeoffund = typeoffund;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getMacaddress() {
		return macaddress;
	}

	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getImeinumbers() {
		return imeinumbers;
	}

	public void setImeinumbers(String imeinumbers) {
		this.imeinumbers = imeinumbers;
	}

	public Integer getImeicount() {
		return imeicount;
	}

	public void setImeicount(Integer imeicount) {
		this.imeicount = imeicount;
	}

	public String getDisableflag() {
		return disableflag;
	}

	public void setDisableflag(String disableflag) {
		this.disableflag = disableflag;
	}

	public String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(String customercode) {
		this.customercode = customercode;
	}

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getTerminalid() {
		return terminalid;
	}

	public void setTerminalid(String terminalid) {
		this.terminalid = terminalid;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/*public String getKinFirstName() {
		return kinFirstName;
	}

	public void setKinFirstName(String kinFirstName) {
		this.kinFirstName = kinFirstName;
	}

	public String getKinMiddleName() {
		return kinMiddleName;
	}

	public void setKinMiddleName(String kinMiddleName) {
		this.kinMiddleName = kinMiddleName;
	}

	public String getKinLastName() {
		return kinLastName;
	}

	public void setKinLastName(String kinLastName) {
		this.kinLastName = kinLastName;
	}

	public String getKinDateOfBirth() {
		return kinDateOfBirth;
	}

	public void setKinDateOfBirth(String kinDateOfBirth) {
		this.kinDateOfBirth = kinDateOfBirth;
	}

	public String getKinMobile() {
		return kinMobile;
	}

	public void setKinMobile(String kinMobile) {
		this.kinMobile = kinMobile;
	}

	public String getKinGender() {
		return kinGender;
	}

	public void setKinGender(String kinGender) {
		this.kinGender = kinGender;
	}

	public String getKinRelation() {
		return kinRelation;
	}

	public void setKinRelation(String kinRelation) {
		this.kinRelation = kinRelation;
	}

	public String getKinAddress() {
		return kinAddress;
	}

	public void setKinAddress(String kinAddress) {
		this.kinAddress = kinAddress;
	}

	public String getSecurityQues1() {
		return securityQues1;
	}

	public void setSecurityQues1(String securityQues1) {
		this.securityQues1 = securityQues1;
	}

	public String getSecurityAns1() {
		return securityAns1;
	}

	public void setSecurityAns1(String securityAns1) {
		this.securityAns1 = securityAns1;
	}

	public String getSecurityQues2() {
		return securityQues2;
	}

	public void setSecurityQues2(String securityQues2) {
		this.securityQues2 = securityQues2;
	}

	public String getSecurityAns2() {
		return securityAns2;
	}

	public void setSecurityAns2(String securityAns2) {
		this.securityAns2 = securityAns2;
	}

	public String getSecurityQues3() {
		return securityQues3;
	}

	public void setSecurityQues3(String securityQues3) {
		this.securityQues3 = securityQues3;
	}

	public String getSecurityAns3() {
		return securityAns3;
	}

	public void setSecurityAns3(String securityAns3) {
		this.securityAns3 = securityAns3;
	}

	public String getBvn() {
		return bvn;
	}

	public void setBvn(String bvn) {
		this.bvn = bvn;
	}

	public String getSol() {
		return sol;
	}

	public void setSol(String sol) {
		this.sol = sol;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}*/

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
