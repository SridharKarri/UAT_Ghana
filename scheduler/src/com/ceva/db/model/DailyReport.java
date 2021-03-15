package com.ceva.db.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "AGENT_DAILY_REPORT")
public class DailyReport implements Serializable {

	@Id
	@Column(name = "id")
	private Integer id;
	private String merchantId;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date generationDate;
	private String amount;
	private String fileLocation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(Date generationDate) {
		this.generationDate = generationDate;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "DailyReport [id=" + id + ", merchantId=" + merchantId
				+ ", status=" + status + ", generationDate=" + generationDate
				+ ", amount=" + amount + ", fileLocation="
				+ fileLocation + "]";
	}

}
