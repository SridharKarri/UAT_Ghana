package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_RESPONSE_CODES")
public class TransactionResponse {

	@Id
	private Long id;
	private String posCode;
	private String bankCode;
	private String bankId;
	private String shortDescription;
	private String fullDescription;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPosCode() {
		return posCode;
	}
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getFullDescription() {
		return fullDescription;
	}
	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	@Override
	public String toString() {
		return "TransactionResponse [id=" + id + ", posCode=" + posCode
				+ ", bankCode=" + bankCode + ", bankId=" + bankId
				+ ", shortDescription=" + shortDescription
				+ ", fullDescription=" + fullDescription + "]";
	}

}
