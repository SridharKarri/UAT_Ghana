package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NIBBS_FINANCIAL_INSTITUTION")
public class BanksAndWallets {

	@Id
	private Long id;
	@Column(name="INSTITUTION_CODE")
	private String instCode;
	@Column(name="INSTITUTION_NAME")
	private String instName;
	@Column(name="STATUS")
	private String status;
	@Column(name="CATEGORY")
	private String category;
	@Column(name="BANK_CODE")
	private String bankCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getInstCode() {
		return instCode;
	}
	
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "BanksAndWallets [id=" + id + ", instName=" + instName
				+ ", category=" + category + ", bankCode=" + bankCode + "]";
	}

}
