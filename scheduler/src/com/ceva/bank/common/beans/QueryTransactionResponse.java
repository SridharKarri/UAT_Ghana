package com.ceva.bank.common.beans;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transactionResponse")
public class QueryTransactionResponse {

	private String accountNumber;
	private BigDecimal amount;
	private String dateTime;
	private String beneficiaryName;
	private String beneficiaryAccount;
	private String referenceNumber;
	private String responseMesage;
	private String fee;
	private String commision;
	private String status;

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

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getResponseMesage() {
		return responseMesage;
	}

	public void setResponseMesage(String responseMesage) {
		this.responseMesage = responseMesage;
	}

	public String getBeneficiaryAccount() {
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(String beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getCommision() {
		return commision;
	}

	public void setCommision(String commision) {
		this.commision = commision;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "QueryTransactionResponse [accountNumber=" + accountNumber
				+ ", amount=" + amount + ", dateTime=" + dateTime
				+ ", beneficiaryName=" + beneficiaryName
				+ ", beneficiaryAccount=" + beneficiaryAccount
				+ ", referenceNumber=" + referenceNumber + ", responseMesage="
				+ responseMesage + ", fee=" + fee + ", status = " + status
				+ ",  commision=" + commision + "]";
	}
}
