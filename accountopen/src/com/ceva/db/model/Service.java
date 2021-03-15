package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE_MASTER")
public class Service {

	@Id
	@Column(name = "SID")
	private Long sid;
	@Column(name = "SERVICECODE")
	private String serviceCode;
	@Column(name = "SERVICENAME")
	private String serviceName;
	@Column(name = "SERVICEDESC")
	private String serviceDesc;
	@Column(name = "STATUS")
	private String status;


/*	@OneToMany(mappedBy="service")
	private Set<Billers> billers;*/

	public Service() {
		super();
	}

	public Long getsId() {
		return sid;
	}

	public void setsId(Long sid) {
		this.sid = sid;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Service [sId=" + sid + ", serviceCode=" + serviceCode
				+ ", serviceName=" + serviceName + ", serviceDesc="
				+ serviceDesc + ", status=" + status +"]";
	}

}
