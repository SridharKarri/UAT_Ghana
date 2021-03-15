package com.ceva.db.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;







@Entity
@Table(name = "SERVICE_MASTER")
public class Service
{
	@Id
	@Column(name = "SID")
	private Long sId;
	@Column(name = "SERVICECODE")
	private String serviceCode;
	@Column(name = "SERVICENAME")
	private String serviceName;
	@Column(name = "SERVICEDESC")
	private String serviceDesc;
	@Column(name = "STATUS")
	private String status;
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
	@Column(name = "AUTH_STATUS")
	private String authStatus;
	@Column(name = "REF_NUM")
	private String refNum;

	public Long getsId() {
		return this.sId;
	}

	public void setsId(Long sId) {
		this.sId = sId;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return this.serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String toString() {
		return "Service [sId=" + sId + ", serviceCode=" + serviceCode + 
				", serviceName=" + serviceName + ", serviceDesc=" + 
				serviceDesc + ", status=" + status + "]";
	}
}