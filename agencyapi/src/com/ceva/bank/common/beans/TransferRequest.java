package com.ceva.bank.common.beans;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Transfer")
public class TransferRequest {

	private String accountNumber;
	private BigDecimal amount;
	private String dateTime;
	private String beneficiaryAccount;
	private String beneficiaryName;
	private String bankCode;
	private String referenceCode;
	private String narration;
	private String makerId;
	private long channel;
	private String serviceId;
	private String merchantId;
	private String mobileNumber;
	private String txnCode;
	private String language;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getBeneficiaryAccount() {
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(String beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "TransferRequest [accountNumber=" + accountNumber + ", amount=" + amount + ", dateTime=" + dateTime
				+ ", beneficiaryAccount=" + beneficiaryAccount + ", beneficiaryName=" + beneficiaryName + ", bankCode="
				+ bankCode + ", referenceCode=" + referenceCode + ", narration=" + narration + ", makerId=" + makerId
				+ ", channel=" + channel + ", serviceId=" + serviceId + ",merchantId=" + merchantId + "]";
	}

	public String getRequestString() {
		return accountNumber + "," + amount + "," + beneficiaryAccount + "," + beneficiaryName + "," + bankCode + ","
				+ referenceCode + "," + narration + "," + makerId + "," + channel + "," + serviceId + "," + merchantId;
	}

}
