package com.ceva.bank.common.beans;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@XmlRootElement(name = "Transfer")
public class TransferRequest {

	@NotNull(message = "This Field is Required.")
	@Length(min = 10, max = 13, message = "This Field Length Should be 10 -13 Numeric Digits.")
	private String accountNumber;

	@NotNull(message = "This Field is Required.")
//	@Range(min = 10, max = 100000, message = "Range Should Between 10 to 100000")
	private BigDecimal amount;

	@NotNull(message = "This Field is Required.")
	private String dateTime;

	@NotNull(message = "This Field is Required.")
	@Length(min = 10, max = 13, message = "This Field Length Should be 10 - 13 Numeric Digits.")
	private String beneficiaryAccount;

	@NotNull(message = "This Field is Required.")
	private String beneficiaryName;

	@NotNull(message = "This Field is Required.")
	private String bankCode;

	private String referenceCode;

	@NotNull(message = "This Field is Required.")
	private String narration;

	@NotNull(message = "This Field is Required.")
	private String makerId;

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;

	@NotNull(message = "This Field is Required.")
	private String serviceId;

	@NotNull(message = "This Field is Required.")
	private String merchantId;

	@NotNull(message="This Field Is Required.")
	@Size(min=10, max=11, message="This field should be 10 Numeric Digits")
	private String mobileNumber;

	private String txnCode;

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

	@Override
	public String toString() {
		return "TransferRequest [accountNumber=" + accountNumber + ", amount="
				+ amount + ", dateTime=" + dateTime + ", beneficiaryAccount="
				+ beneficiaryAccount + ", beneficiaryName=" + beneficiaryName
				+ ", bankCode=" + bankCode + ", referenceCode=" + referenceCode
				+ ", narration=" + narration + ", makerId=" + makerId
				+ ", channel=" + channel + ", serviceId=" + serviceId
				+ ",merchantId=" + merchantId+" ]";
	}

}
