package com.ceva.db.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SERVICE_PRODUCT_MAP")
public class ServiceProducts {

	@Id
	private long spId;
	private long pid;
	private long sid;
	private String makerId;
	private String authId;
	@Temporal(TemporalType.DATE)
	private Date makerDttm;
	@Temporal(TemporalType.DATE)
	private Date authDttm;
	private BigDecimal txnLimit = new BigDecimal(0);
	private String status;

	public long getSpId() {
		return spId;
	}

	public void setSpId(long spId) {
		this.spId = spId;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
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

	public BigDecimal getTxnLimit() {
		return txnLimit;
	}

	public void setTxnLimit(BigDecimal txnLimit) {
		this.txnLimit = txnLimit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ServiceProducts [spId=" + spId + ", pid=" + pid + ", sid="
				+ sid + ", makerId=" + makerId + ", authId=" + authId
				+ ", makerDttm=" + makerDttm + ", authDttm=" + authDttm
				+ ", txnLimit=" + txnLimit + ", status=" + status + "]";
	}

}
