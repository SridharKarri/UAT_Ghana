package com.ceva.bank.common.beans;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class BillPayment {

	private long id;
	private String merchantId;
	private String countryId;
	private String currency;
	private String channel;
	private BigDecimal amount;
	private String billerId;
	private String packId;
	private String customerMobile;
	private String mobileNumber;
	private String customerId;
	private String userId;
	private String narration;
	private String requestReference;
	private String accountNumber;
	private String billerName;
	private BigDecimal fee;
	private BigDecimal agentIncentive;
	private String pin;
	private String txnCode;
	@XmlElement(name="formItems")
	private List<ProductFormItems> formItems;
	
	
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public List<ProductFormItems> getFormItems() {
		return formItems;
	}

	public void setFormItems(List<ProductFormItems> formItems) {
		this.formItems = formItems;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getRequestString() {
		return billerId + "," + channel + "," + amount + "," + packId + "," + mobileNumber + ","
				+ customerMobile + "," + customerId + "," + userId + "," + requestReference+", currency="+currency;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "BillPayment [id=" + id + ", countryId=" + countryId + ", merchantId=" + merchantId + ", channel="
				+ channel + ", amount=" + amount + ", packId=" + packId + ", mobileNumber=" + mobileNumber
				+ ", customerMobile=" + customerMobile + ", customerId=" + customerId + ", userId=" + userId
				+ ", narration=" + narration + ", requestReference=" + requestReference + ", currency="+currency+""
				+ ", formItems="+formItems+", billerId="+billerId+", billerName="+billerName+", accountNumber="+accountNumber+", fee="+fee
				+ ", agentIncentive="+agentIncentive+", pin="+pin+", txnCode="+txnCode+"]";
	}

}
