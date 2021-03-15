package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "NON_FIN_TXNLOG")
public class NonFinTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	private long refNumber;
	private long bankId = 0;
	private long toAcNum = 0;
	private long channel;
	@Column(name = "userId")
	private String approvedBy;
	private String terminalNumber;
	private String txnCode;
	private String merchantId;
	private long errorCode = 0;
	private String errorDescription;
	private String extSysId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date txndateTime = new Date();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(long refNumber) {
		this.refNumber = refNumber;
	}

	public long getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}

	public long getBankId() {
		return bankId;
	}

	public void setBankId(long bankId) {
		this.bankId = bankId;
	}

	public long getToAcNum() {
		return toAcNum;
	}

	public void setToAcNum(long toAcNum) {
		this.toAcNum = toAcNum;
	}

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
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

	public Date getTxndateTime() {
		return txndateTime;
	}

	public void setTxndateTime(Date txndateTime) {
		this.txndateTime = txndateTime;
	}

	@Override
	public String toString() {
		return "NonFinTransaction [id=" + id + ", refNumber=" + refNumber
				+ ", errorCode=" + errorCode + ", bankId=" + bankId
				+ ", toAcNum=" + toAcNum + ", channel=" + channel
				+ ", approvedBy=" + approvedBy + ", terminalNumber="
				+ terminalNumber + ", txnCode=" + txnCode + ", merchantId="
				+ merchantId + ", errorDescription=" + errorDescription
				+ ", extSysId=" + extSysId + "]";
	}
}
