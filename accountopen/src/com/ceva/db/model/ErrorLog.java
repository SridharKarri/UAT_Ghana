package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "APPERRORLOG")
public class ErrorLog {

	@Id
	@GeneratedValue
	private long id;
	@Column(name="errorDescription")
	private String errorDescription;
	@Column(name="errorCode")
	private String errorCode;
	@Column(name="merchantId")
	private String merchantId;
	@Column(name="narration")
	private String narration;
	@Column(name="userId")
	private String userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.ALWAYS)
	@Column(name="errorTime", insertable=false,updatable=false)
	private Date errorTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(Date errorTime) {
		this.errorTime = errorTime;
	}

}
