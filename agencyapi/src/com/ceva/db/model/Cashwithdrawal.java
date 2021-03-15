package com.ceva.db.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "cashwithdrawal")
public class Cashwithdrawal {

	@Id
	@SequenceGenerator(name = "CWD_ID", sequenceName = "CWD_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CWD_ID")
	private long id;
	private String channel;
	private String makerId;
	private String merchantId;
	private String mobileNumber;
	private String serviceId;
	private String customerAccountNumber;
	private String customerName;
	private String txnCode;
	private String phone;
	private String secondaryPhone;
	private String status;
	private String smsRef;

	private BigDecimal amount;
	private BigDecimal fee;
	private BigDecimal agentCmsn = new BigDecimal(0);
	private BigDecimal supAgentCmsn = new BigDecimal(0);
	private BigDecimal bankCmsn = new BigDecimal(0);

	public Cashwithdrawal() {
		super();
	}

	public Cashwithdrawal(long id, String channel, String makerId, String merchantId, String mobileNumber,
			String serviceId, String customerAccountNumber, String customerName, String txnCode, String phone,
			String secondaryPhone) {
		super();
		this.id = id;
		this.channel = channel;
		this.makerId = makerId;
		this.merchantId = merchantId;
		this.mobileNumber = mobileNumber;
		this.serviceId = serviceId;
		this.customerAccountNumber = customerAccountNumber;
		this.customerName = customerName;
		this.txnCode = txnCode;
		this.phone = phone;
		this.secondaryPhone = secondaryPhone;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCustomerAccountNumber() {
		return customerAccountNumber;
	}

	public void setCustomerAccountNumber(String customerAccountNumber) {
		this.customerAccountNumber = customerAccountNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSecondaryPhone() {
		return secondaryPhone;
	}

	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getAgentCmsn() {
		return agentCmsn;
	}

	public void setAgentCmsn(BigDecimal agentCmsn) {
		this.agentCmsn = agentCmsn;
	}

	public BigDecimal getSupAgentCmsn() {
		return supAgentCmsn;
	}

	public void setSupAgentCmsn(BigDecimal supAgentCmsn) {
		this.supAgentCmsn = supAgentCmsn;
	}

	public BigDecimal getBankCmsn() {
		return bankCmsn;
	}

	public void setBankCmsn(BigDecimal bankCmsn) {
		this.bankCmsn = bankCmsn;
	}

	public String getSmsRef() {
		return smsRef;
	}

	public void setSmsRef(String smsRef) {
		this.smsRef = smsRef;
	}

	@Override
	public String toString() {
		return "Cashwithdrawal [id=" + id + ", channel=" + channel + ", makerId=" + makerId + ", merchantId="
				+ merchantId + ", mobileNumber=" + mobileNumber + ", serviceId=" + serviceId
				+ ", customerAccountNumber=" + customerAccountNumber + ", customerName=" + customerName + ", txnCode="
				+ txnCode + ", phone=" + phone + ", secondaryPhone=" + secondaryPhone + ", status=" + status + "]";
	}

}
