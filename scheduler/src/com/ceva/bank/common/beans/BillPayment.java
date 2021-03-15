package com.ceva.bank.common.beans;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;

@XmlRootElement(name = "BillPayment")
public class BillPayment {

	@NotNull(message = "This Field Is Required.")
	private long id;

	@Size(message = "Merchant Id Shuold Not empty.")
	private String merchantId;

	@Size(message = "Service Id Shuold Not empty.")
	@Length(min = 1, max = 4, message = "This Field Should between 1-4 Numeric Characters.")
	private String sid;

	@Size(message = "Channel Should Not empty.")
	@Length(min = 1, max = 5, message = "This Field Should between 1-5 Numeric Characters.")
	private String channel;

	@Size(message = "This Field Should Not empty.")
	private String amount;

	@Size(message = "This Field Should Not empty.")
	private String packId;

	@Size(message = "This Field Should Not empty.")
	@Length(min = 10, max = 11, message = "This Field Should between 11 Numeric Characters.")
	private String mobileNumber;

	@Size(message = "This Field Should Not empty.")
	@Length(min = 10, max = 11, message = "This Field Should between 11 Numeric Characters.")
	private String customerMobile;

	@Size(message = "This Field Should Not empty.")
	private String customerEmail;

	@Size(message = "This Field Should Not empty.")
	private String customerId;

	@Size(message = "MakerId Should Not empty.")
	@Length(min = 1, max = 120, message = "This Field Should between 1-120 Characters.")
	private String userId;

	@Size(message = "This Field Should Not empty.")
	private String paymentCode;
	
	private String billerId;

	private String accountNumber;
	private String billerName;
	private BigDecimal fee;
	private String narration;

	private String txnCode;

	private String terminalId;

	private String requestReference;

	public BillPayment() {
		super();
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getRequestReference() {
		return requestReference;
	}

	public void setRequestReference(String requestReference) {
		this.requestReference = requestReference;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}


	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	@Override
	public String toString() {
		return "BillPayment [id=" + id + ", merchantId=" + merchantId
				+ ", paymentCode=" + paymentCode + ", sid=" + sid
				+ ", channel=" + channel + ", amount=" + amount + ", packId="
				+ packId + ", mobileNumber=" + mobileNumber
				+ ", customerMobile=" + customerMobile + ", customerEmail="
				+ customerEmail + ", customerId=" + customerId + ", userId="
				+ userId + ", accountNumber=" + accountNumber + ", billerName="
				+ billerName + ", fee=" + fee + ", narration=" + narration
				+ ", terminalId=" + terminalId + ", requestReference="
				+ requestReference + ", billerId ="+billerId+" ]";
	}


}
