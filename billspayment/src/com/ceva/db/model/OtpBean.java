package com.ceva.db.model;

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
@Table(name = "ENTITY_OTP")
public class OtpBean {
	@Id
	@SequenceGenerator(name="OTP_ID", sequenceName="OTP_ID", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OTP_ID")
	private Long Id;
	private String mobileNo;
	private String otp;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	private String status;
	private String txnCode;
	private String narration;
	@Temporal(TemporalType.TIMESTAMP)
	private Date validationTime;

	private long retryCount;


	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTxnCode() {
		return txnCode;
	}

	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Date getValidationTime() {
		return validationTime;
	}

	public void setValidationTime(Date validationTime) {
		this.validationTime = validationTime;
	}

	public long getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(long retryCount) {
		this.retryCount = retryCount;
	}

	@Override
	public String toString() {
		return "OtpBean [Id=" + Id + ", mobileNo=" + mobileNo
				+ ", otp="
				+ otp + ", dateTime=" + dateTime + ", status=" + status
				+ ", txnCode=" + txnCode + ", narration=" + narration
				+ ", validationTime=" + validationTime + "]";
	}

}
