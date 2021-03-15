package com.ceva.db.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FEE_MASTER")
public class Fee {

	@Id
	@Column(name = "FID")
	private long Id;
	@Column(name = "SPID")
	private long spid;
	@Column(name = "FROMVAL")
	private BigDecimal fromVal=new BigDecimal(0);
	@Column(name = "TOVAL")
	private BigDecimal toVal=new BigDecimal(0);
	@Column(name = "FEEVAL")
	private BigDecimal feeVal=new BigDecimal(0);

	@Column(name = "FEECODE")
	private String feeCode="0";
	@Column(name = "FEENAME")
	private String feeName;

	@Column(name = "FEETYPE")
	private String feeType;
	@Column(name = "CHANNEL")
	private String channel;
	@Column(name = "CURRENCY")
	private String currency;
	@Column(name = "TXNTYPE")
	private String txnType;
	@Column(name = "AGENTCMSN")
	private BigDecimal agntCmsn=new BigDecimal(0);
	@Column(name = "SUPAGENTCMSN")
	private BigDecimal supAgentCmsn=new BigDecimal(0);
	@Column(name = "BANKFEE")
	private BigDecimal bankFee=new BigDecimal(0);
	@Column(name = "SERVTAX")
	private BigDecimal servTax;

	@Column(name = "MAKERID")
	private String makerId;
	@Column(name = "CHECKERID")
	private String authId;
	@Column(name = "MAKERDTTM")
	@Temporal(TemporalType.DATE)
	private Date makerDttm;
	@Column(name = "CHECKERDTTM")
	@Temporal(TemporalType.DATE)
	private Date authDttm;

	@Column(name = "BINS")
	private String bins;
	@Column(name = "AUTH_FLAG")
	private String authFlag;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "REF_NUM")
	private String refNum;

	public Fee() {
		super();
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getSpid() {
		return spid;
	}

	public void setSpid(long spid) {
		this.spid = spid;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public BigDecimal getAgntCmsn() {
		return agntCmsn;
	}

	public void setAgntCmsn(BigDecimal agntCmsn) {
		this.agntCmsn = agntCmsn;
	}

	public BigDecimal getSupAgentCmsn() {
		return supAgentCmsn;
	}

	public void setSupAgentCmsn(BigDecimal supAgentCmsn) {
		this.supAgentCmsn = supAgentCmsn;
	}

	public BigDecimal getBankFee() {
		return bankFee;
	}

	public void setBankFee(BigDecimal bankFee) {
		this.bankFee = bankFee;
	}

	public BigDecimal getServTax() {
		return servTax;
	}

	public void setServTax(BigDecimal servTax) {
		this.servTax = servTax;
	}

	public void setFromVal(BigDecimal fromVal) {
		this.fromVal = fromVal;
	}

	public void setToVal(BigDecimal toVal) {
		this.toVal = toVal;
	}

	public void setFeeVal(BigDecimal feeVal) {
		this.feeVal = feeVal;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public Date getMakerDttm() {
		return makerDttm;
	}

	public void setMakerDttm(Date makerDttm) {
		this.makerDttm = makerDttm;
	}

	public Date getAuthDttm() {
		return authDttm;
	}

	public void setAuthDttm(Date authDttm) {
		this.authDttm = authDttm;
	}

	public double getTotalFee() {
		return (agntCmsn.doubleValue() + supAgentCmsn.doubleValue()
				+ bankFee.doubleValue() + servTax.doubleValue());
	}

	public BigDecimal getFromVal() {
		return fromVal;
	}

	public BigDecimal getToVal() {
		return toVal;
	}

	public BigDecimal getFeeVal() {
		return feeVal;
	}

	public String getBins() {
		return bins;
	}

	public void setBins(String bins) {
		this.bins = bins;
	}

	public String getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Fee [Id=" + Id + ", spid=" + spid + ", fromVal=" + fromVal
				+ ", toVal=" + toVal + ", feeVal=" + feeVal + ", feeCode="
				+ feeCode + ", feeName=" + feeName + ", feeType=" + feeType
				+ ", channel=" + channel + ", currency=" + currency
				+ ", txnType=" + txnType + ", agntCmsn=" + agntCmsn
				+ ", supAgentCmsn=" + supAgentCmsn + ", bankFee=" + bankFee
				+ ", servTax=" + servTax + ", makerId=" + makerId + ", authId="
				+ authId + ", makerDttm=" + makerDttm + ", authDttm="
				+ authDttm + ", bins=" + bins + ", status=" + status
				+ ", authFlag=" + authFlag + "]";
	}

}
