package com.ceva.db.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	@Column(name="MAKERID")
	private String makerId;
	@Column(name="CHECKERID")
	private String authId;
	@Column(name="MAKERDTTM")
    @Temporal(TemporalType.DATE)
	private Date makerDttm;
	@Column(name="CHECKERDTTM")
	@Temporal(TemporalType.DATE)
	private Date authDttm;
	@Column(name = "AUTH_STATUS")
	private String authStatus;
	@Column(name="REF_NUM")
    private String refNum;

	@Column(name="LABEL")
    private String label;
	@Column(name="SERVICETYPE")
	private String serviceType;

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


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
	public String toString() {
		return "Service [sId=" + sid + ", serviceCode=" + serviceCode
				+ ", serviceName=" + serviceName + ", serviceDesc="
				+ serviceDesc + ", status=" + status + ", label=" + label + ", serviceType="+serviceType+"]";
	}

}
