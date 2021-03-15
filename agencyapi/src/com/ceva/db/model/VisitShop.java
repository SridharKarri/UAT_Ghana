package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "VISIT_SHOP")
public class VisitShop {

	@Id
	@SequenceGenerator(name="VISIT_ID", sequenceName="VISIT_SHOP_ID", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="VISIT_ID")
	private Long id;
	private String channel;
	private String userId;
	private String agentId;
	private String requestedVisitDate;
	private String reason;
	private String status;
	private String dsaInAction;
	private Long agentFeedBack;
	private Date agentFeedBackDate;
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

	public String getRequestedVisitDate() {
		return requestedVisitDate;
	}

	public void setRequestedVisitDate(String requestedVisitDate) {
		this.requestedVisitDate = requestedVisitDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDsaInAction() {
		return dsaInAction;
	}

	public void setDsaInAction(String dsaInAction) {
		this.dsaInAction = dsaInAction;
	}

	public Long getAgentFeedBack() {
		return agentFeedBack;
	}

	public void setAgentFeedBack(Long agentFeedBack) {
		this.agentFeedBack = agentFeedBack;
	}

	public Date getAgentFeedBackDate() {
		return agentFeedBackDate;
	}

	public void setAgentFeedBackDate(Date agentFeedBackDate) {
		this.agentFeedBackDate = agentFeedBackDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "VisitShop [id=" + id + ", channel=" + channel + ", userId="
				+ userId + ", agentId=" + agentId + ", requestedVisitDate="
				+ requestedVisitDate + ", reason=" + reason + ", status="
				+ status + ", dsaInAction=" + dsaInAction + ", agentFeedBack="
				+ agentFeedBack + ", agentFeedBackDate=" + agentFeedBackDate
				+ ", createdDate=" + createdDate + "]";
	}

}
