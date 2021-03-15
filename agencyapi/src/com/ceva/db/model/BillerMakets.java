package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BILLER_MARKETS")
public class BillerMakets {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "BILLERID")
	private String billerId;

	@Column(name = "MARKETNAME")
	private String marketName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	@Override
	public String toString() {
		return "BillerMakets [id=" + id + ", status=" + status + ", billerId=" + billerId + ", marketName=" + marketName
				+ "]";
	}

}