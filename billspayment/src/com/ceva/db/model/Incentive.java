package com.ceva.db.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INCENTIVES_MAP")
public class Incentive {

	@Id
	@Column(name = "SEQID")
	private long Id;
	@Column(name = "PID")
	private long pid;
	@Column(name = "INCTVID")
	private long incentiveId;
	@Column(name = "STOREID")
	private String storeId;
	@Column(name = "FROMVAL")
	private long fromVal;
	@Column(name = "TOVAL")
	private long toVal;
	@Column(name = "INCTVAL")
	private BigDecimal incVal = new BigDecimal(0);
	@Column(name = "INCTTYPE")
	private String incType;
	@Column(name = "STATUS")
	private String status;

	public Incentive() {
		super();
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public long getFromVal() {
		return fromVal;
	}

	public void setFromVal(long fromVal) {
		this.fromVal = fromVal;
	}

	public long getToVal() {
		return toVal;
	}

	public void setToVal(long toVal) {
		this.toVal = toVal;
	}

	public BigDecimal getIncVal() {
		return incVal;
	}

	public void setIncVal(BigDecimal incVal) {
		this.incVal = incVal;
	}

	public String getIncType() {
		return incType;
	}

	public void setIncType(String incType) {
		this.incType = incType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getIncentiveId() {
		return incentiveId;
	}

	public void setIncentiveId(long incentiveId) {
		this.incentiveId = incentiveId;
	}

	@Override
	public String toString() {
		return "Incentive [Id=" + Id + ", pid=" + pid + ", incentiveId="
				+ incentiveId + ", storeID="
				+ storeId + ", fromVal=" + fromVal + ", toVal=" + toVal
				+ ", incVal=" + incVal + ", incType=" + incType + ", status="
				+ status + "]";
	}

}
