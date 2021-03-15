package com.ceva.db.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Formula;




@Entity
@Table(name = "TXN_LOG")
public class Transaction
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	private long feeCode = 0L;

	private String refNumber;

	private String errorCode;

	private String responseCode;
	private String bankId;
	private String fromAccountNum;
	private String toAcNum;
	private long channel;
	private BigDecimal amount;
	private BigDecimal fee;
	private String txnType;
	private String narration;
	private String status;
	@Column(name = "userId")
	private String approvedBy;
	private String terminalNumber;
	private String txnCode;
	private String merchantId;
	private String errorDescription;
	private String extSysId;
	private String billerId;
	private String billerChannel;
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
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTxnType() {
		return this.txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public BigDecimal getAmount() {
		return this.amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFee() {
		return this.fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public long getFeeCode() {
		return this.feeCode;
	}
	public void setFeeCode(long feeCode) {
		this.feeCode = feeCode;
	}
	public String getNarration() {
		return this.narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getRefNumber() {
		return this.refNumber;
	}
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	public String getErrorCode() {
		return this.errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return this.errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getExtSysId() {
		return this.extSysId;
	}
	public void setExtSysId(String extSysId) {
		this.extSysId = extSysId;
	}

	public String getResponseCode() {
		return this.responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getApprovedBy() {
		return this.approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getTerminalNumber() {
		return this.terminalNumber;
	}
	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}
	public String getTxnCode() {
		return this.txnCode;
	}
	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}
	public String getMerchantId() {
		return this.merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public long getChannel() {
		return this.channel;
	}
	public void setChannel(long channel) {
		this.channel = channel;
	}
	public Date getTxndateTime() {
		return this.txndateTime;
	}
	public void setTxndateTime(Date txndateTime) {
		this.txndateTime = txndateTime;
	}


	public String getBillerId() {
		return this.billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getBillerChannel() {
		return this.billerChannel;
	}
	public void setBillerChannel(String billerChannel) {
		this.billerChannel = billerChannel;
	}


	public BigDecimal getAgentCmsn() {
		return this.agentCmsn;
	}
	public void setAgentCmsn(BigDecimal agentCmsn) {
		this.agentCmsn = agentCmsn;
	}

	public BigDecimal getSupAgentCmsn() {
		return this.supAgentCmsn;
	}
	public void setSupAgentCmsn(BigDecimal supAgentCmsn) {
		this.supAgentCmsn = supAgentCmsn;
	}
	public BigDecimal getBankCmsn() {
		return this.bankCmsn;
	}
	public void setBankCmsn(BigDecimal bankCmsn) {
		this.bankCmsn = bankCmsn;
	}

	public String getBankId() {
		return this.bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getFromAccountNum() {
		return this.fromAccountNum;
	}
	public void setFromAccountNum(String fromAccountNum) {
		this.fromAccountNum = fromAccountNum;
	}
	public String getToAcNum() {
		return this.toAcNum;
	}
	public void setToAcNum(String toAcNum) {
		this.toAcNum = toAcNum;
	}

	public BigDecimal getIncentive() {
		return this.incentive;
	}
	public void setIncentive(BigDecimal incentive) {
		this.incentive = incentive;
	}
	public BigDecimal getTxnCommission() {
		return this.txnCommission;
	}
	public void setTxnCommission(BigDecimal txnCommission) {
		this.txnCommission = txnCommission;
	}

	public String getStoreId() {
		return this.storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getRequestString() {
		return this.requestString;
	}
	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public Date getReqdateTime() {
		return this.reqdateTime;
	}

	public void setReqdateTime(Date reqdateTime) {
		this.reqdateTime = reqdateTime;
	}

	public Date getRespDateTime() {
		return this.respDateTime;
	}

	public void setRespDateTime(Date respDateTime) {
		this.respDateTime = respDateTime;
	}


	public String toString() {
		return "Transaction [id=" + id + ", feeCode=" + feeCode + 
				", refNumber=" + refNumber + ", errorCode=" + errorCode + 
				", responseCode=" + responseCode + ", bankId=" + bankId + 
				", toAcNum=" + toAcNum + ", channel=" + channel + ", amount=" + 
				amount + ", fee=" + fee + ", txnType=" + txnType + 
				", narration=" + narration + ", status=" + status + 
				", approvedBy=" + approvedBy + ", terminalNumber=" + 
				terminalNumber + ", txnCode=" + txnCode + ", merchantId=" + 
				merchantId + ", errorDescription=" + errorDescription + 
				", extSysId=" + extSysId + ", billerId=" + billerId + 
				", billerChannel=" + billerChannel + ", agentCmsn=" + 
				agentCmsn + ", supAgentCmsn=" + supAgentCmsn + ", bankCmsn=" + 
				bankCmsn + ", txndateTime=" + txndateTime + "]";
	}
}