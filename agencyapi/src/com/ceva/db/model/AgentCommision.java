package com.ceva.db.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "agentCommision")
public class AgentCommision implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	private String merchantId;
	private String settlementStatus;
	@Temporal(TemporalType.DATE)
	private Date commisionDate = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	private Date settlementDate;
	private BigDecimal amount = new BigDecimal(0);
	private String responseMessage;
	private String responseCode="00";
	private String batchNumber;
	private String commissionPayoutId;
	private Boolean isAgent = false;

	public AgentCommision() {
		super();
	}

	public AgentCommision(String merchantId, String settlementStatus,
			boolean isAgent) {
		super();
		this.merchantId = merchantId;
		this.settlementStatus = settlementStatus;
		this.isAgent = isAgent;
		this.commissionPayoutId = System.currentTimeMillis()+"";
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

	public String getSettlementStatus() {
		return settlementStatus;
	}

	public void setSettlementStatus(String settlementStatus) {
		this.settlementStatus = settlementStatus;
	}

	public Date getCommisionDate() {
		return commisionDate;
	}

	public void setCommisionDate(Date commisionDate) {
		this.commisionDate = commisionDate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isAgent() {
		return isAgent;
	}

	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getCommissionPayoutId() {
		return commissionPayoutId;
	}

	public void setCommissionPayoutId(String commissionPayoutId) {
		this.commissionPayoutId = commissionPayoutId;
	}

	public Boolean getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(Boolean isAgent) {
		this.isAgent = isAgent;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public String toString() {
		return "AgentCommision [id=" + id + ", merchantId=" + merchantId
				+ ", settlementStatus=" + settlementStatus + ", commisionDate="
				+ commisionDate + ", settlementDate=" + settlementDate
				+ ", amount=" + amount + ", batchNumber=" + batchNumber
				+ ", commissionPayoutId=" + commissionPayoutId + ", isAgent="
				+ isAgent + "]";
	}

}
