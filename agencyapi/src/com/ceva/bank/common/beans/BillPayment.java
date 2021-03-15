package com.ceva.bank.common.beans;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BillPayment")
public class BillPayment {

	private long id;

	private String merchantId;

	private String sid;

	private String channel;

	private String amount;

	private String packId;

	private String mobileNumber;

	private String customerMobile;

	private String customerEmail;

	private String customerId;

	private String userId;

	private String paymentCode;

	private String narration;

	private String billerId;

	private String terminalId;

	private String requestReference;

	private String accountNumber;
	private String billerName;
	private BigDecimal fee;
	private BigDecimal agentIncentive;

	private String txnCode;

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

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
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

	public BigDecimal getAgentIncentive() {
		return agentIncentive;
	}

	public void setAgentIncentive(BigDecimal agentIncentive) {
		this.agentIncentive = agentIncentive;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getRequestString() {
		return billerId + "," + sid + "," + channel + "," + amount + "," + packId + "," + mobileNumber + ","
				+ customerMobile + "," + customerEmail + "," + customerId + "," + paymentCode + "," + terminalId + ","
				+ requestReference;
	}

	@Override
	public String toString() {
		return "BillPayment [id=" + id + ", merchantId=" + merchantId + ", sid=" + sid + ", channel=" + channel
				+ ", amount=" + amount + ", packId=" + packId + ", mobileNumber=" + mobileNumber + ", customerMobile="
				+ customerMobile + ", customerEmail=" + customerEmail + ", customerId=" + customerId + ", userId="
				+ userId + ", paymentCode=" + paymentCode + ", narration=" + narration + ", terminalId=" + terminalId
				+ ", requestReference=" + requestReference + "]";
	}

}
