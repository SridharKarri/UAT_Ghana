package com.ceva.db.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;

@Entity
@Table(name="TXN_LOG")
public class Transaction {

	@Id
	@SequenceGenerator(name="TXN_ID", sequenceName="TXN_ID", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TXN_ID")
	private long id;

	private long feeCode = 0;
	private String refNumber;
	private String errorCode;

	private String responseCode;
	private String bankId;
	private long channel;

	private BigDecimal amount;
	private BigDecimal fee;

	private String narration;
	private String status;
	@Column(name = "userId")
	private String approvedBy;
	private String terminalNumber;
	private String txnCode;
	private String merchantId;
	private String errorDescription;
	private String extSysId;
	@Column(name="AGENTACCOUNTNUMBER")
	private String agentAccountNumber;
	@Column(name="CUSTOMERACCOUNTNUMBER")
	private String customerAccountNumber;
	@Column(name="BENEFICIARYACCOUNTNUMBER")
	private String beneficiaryAccountNumber;
	@Column(name="CHANNEL_REF")
	private String channelRef;
	private String cardNumber;
	
	private BigDecimal incentive = new BigDecimal(0);
	@Formula("incentive+agentCmsn")
	private BigDecimal txnCommission = new BigDecimal(0);
	private BigDecimal agentCmsn = new BigDecimal(0);
	private BigDecimal supAgentCmsn = new BigDecimal(0);
	private BigDecimal bankCmsn = new BigDecimal(0);

	@Temporal(TemporalType.TIMESTAMP)
	private Date txndateTime = new Date();

	private String storeId;
	private String requestString;

	@Column(name = "REQ_DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reqdateTime = new Date();
	@Column(name = "RESP_DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date respDateTime = null;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(long feeCode) {
		this.feeCode = feeCode;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
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

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getTerminalNumber() {
		return terminalNumber;
	}

	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getExtSysId() {
		return extSysId;
	}

	public void setExtSysId(String extSysId) {
		this.extSysId = extSysId;
	}

	public String getAgentAccountNumber() {
		return agentAccountNumber;
	}

	public void setAgentAccountNumber(String agentAccountNumber) {
		this.agentAccountNumber = agentAccountNumber;
	}

	public String getCustomerAccountNumber() {
		return customerAccountNumber;
	}

	public void setCustomerAccountNumber(String customerAccountNumber) {
		this.customerAccountNumber = customerAccountNumber;
	}

	public String getBeneficiaryAccountNumber() {
		return beneficiaryAccountNumber;
	}

	public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public BigDecimal getIncentive() {
		return incentive;
	}

	public void setIncentive(BigDecimal incentive) {
		this.incentive = incentive;
	}

	public BigDecimal getTxnCommission() {
		return txnCommission;
	}

	public void setTxnCommission(BigDecimal txnCommission) {
		this.txnCommission = txnCommission;
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

	public Date getTxndateTime() {
		return txndateTime;
	}

	public void setTxndateTime(Date txndateTime) {
		this.txndateTime = txndateTime;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getRequestString() {
		return requestString;
	}

	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public Date getReqdateTime() {
		return reqdateTime;
	}

	public void setReqdateTime(Date reqdateTime) {
		this.reqdateTime = reqdateTime;
	}

	public Date getRespDateTime() {
		return respDateTime;
	}

	public void setRespDateTime(Date respDateTime) {
		this.respDateTime = respDateTime;
	}

	public String getChannelRef() {
		return channelRef;
	}

	public void setChannelRef(String channelRef) {
		this.channelRef = channelRef;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [id=");
		builder.append(id);
		builder.append(", feeCode=");
		builder.append(feeCode);
		builder.append(", refNumber=");
		builder.append(refNumber);
		builder.append(", errorCode=");
		builder.append(errorCode);
		builder.append(", responseCode=");
		builder.append(responseCode);
		builder.append(", bankId=");
		builder.append(bankId);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", fee=");
		builder.append(fee);
		builder.append(", narration=");
		builder.append(narration);
		builder.append(", status=");
		builder.append(status);
		builder.append(", approvedBy=");
		builder.append(approvedBy);
		builder.append(", terminalNumber=");
		builder.append(terminalNumber);
		builder.append(", txnCode=");
		builder.append(txnCode);
		builder.append(", merchantId=");
		builder.append(merchantId);
		builder.append(", errorDescription=");
		builder.append(errorDescription);
		builder.append(", extSysId=");
		builder.append(extSysId);
		builder.append(", agentAccountNumber=");
		builder.append(agentAccountNumber);
		builder.append(", customerAccountNumber=");
		builder.append(customerAccountNumber);
		builder.append(", beneficiaryAccountNumber=");
		builder.append(beneficiaryAccountNumber);
		builder.append(", cardNumber=");
		builder.append(cardNumber);
		builder.append(", incentive=");
		builder.append(incentive);
		builder.append(", txnCommission=");
		builder.append(txnCommission);
		builder.append(", agentCmsn=");
		builder.append(agentCmsn);
		builder.append(", supAgentCmsn=");
		builder.append(supAgentCmsn);
		builder.append(", bankCmsn=");
		builder.append(bankCmsn);
		builder.append(", txndateTime=");
		builder.append(txndateTime);
		builder.append(", storeId=");
		builder.append(storeId);
		builder.append(", requestString=");
		builder.append(requestString);
		builder.append(", reqdateTime=");
		builder.append(reqdateTime);
		builder.append(", respDateTime=");
		builder.append(respDateTime);
		builder.append(", channelRef=");
		builder.append(channelRef);
		builder.append("]");
		return builder.toString();
	}
}
