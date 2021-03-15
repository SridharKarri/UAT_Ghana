package com.ceva.db.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "LOG_COMPLAINT")
public class LogComplaint {

	@Id
	@SequenceGenerator(name="LOG_ID", sequenceName="LOG_COMPLAINT_ID", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="LOG_ID")
	private Long id;
	private String channel;
	private String userId;
	private String agentId;
	private String customerAccountNumber;
	private BigDecimal amount;
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDateTime;
	private String description;
	private String status;
	@Temporal(TemporalType.DATE)
	private Date createdDate= new Date();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public String getAgentId() {
		return agentId;
	}
	
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	public String getCustomerAccountNumber() {
		return customerAccountNumber;
	}
	
	public void setCustomerAccountNumber(String customerAccountNumber) {
		this.customerAccountNumber = customerAccountNumber;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Date getTransactionDateTime() {
		return transactionDateTime;
	}
	
	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}
	

	public void setStatus(String status) {
		this.status = status;
	}
	

	@Override
	public String toString() {
		return "LogComplaint [id=" + id + ", channel=" + channel + ", userId=" + userId + ", agentId=" + agentId
				+ ", customerAccountNumber=" + customerAccountNumber + ", amount=" + amount + ", transactionDateTime="
				+ transactionDateTime + ", description=" + description + ", createdDate=" + createdDate + "]";
	}
	
}
