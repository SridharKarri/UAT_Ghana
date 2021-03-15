package com.ceva.bank.common.beans;

import java.util.Date;

public class BillTransaction {

	private long accountNumber;
	private long billAmount;
	private long fee;
	private long billReference;
	private String billerId;
	private String billerName;
	private String cateGoryId;
	private Date dateTime;
	private String email;
	private String mobile;
	private String narration;
	private String paymentCode;
	private String refCode;

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(long billAmount) {
		this.billAmount = billAmount;
	}

	public long getFee() {
		return fee;
	}

	public void setFee(long fee) {
		this.fee = fee;
	}

	public long getBillReference() {
		return billReference;
	}

	public void setBillReference(long billReference) {
		this.billReference = billReference;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public String getCateGoryId() {
		return cateGoryId;
	}

	public void setCateGoryId(String cateGoryId) {
		this.cateGoryId = cateGoryId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	@Override
	public String toString() {
		return "BillTransaction [accountNumber=" + accountNumber
				+ ", billAmount=" + billAmount + ", fee=" + fee
				+ ", billReference=" + billReference + ", billerId=" + billerId
				+ ", billerName=" + billerName + ", cateGoryId=" + cateGoryId
				+ ", dateTime=" + dateTime + ", email=" + email + ", mobile="
				+ mobile + ", narration=" + narration + ", paymentCode="
				+ paymentCode + ", refCode=" + refCode + "]";
	}

}
