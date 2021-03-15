package com.ceva.db.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BILLER_PACKAGES")
public class BillerPackages {

	@Id
	@GeneratedValue
	private long id;
	
	private String packId;

	@Column(name="DISPLAYNAME")
	private String displayName;

	@Column(name="PACKDESC")
	private String packDescription;

	@Column(name="CHARGE")
	private BigDecimal charge;

	@Column(name="STATUS")
	private String status;

	@Column(name="BILLERID")
	private String billerId;

	@Column(name="PACKNAME")
	private String packName;
	
	@Column(name="PAYMENTCODE")
	private String paymentCode;
	
	@Column(name="QTBILLERID")
	private String qtbillerId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPackId() {
		return packId;
	}

	public void setPackId(String packId) {
		this.packId = packId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPackDescription() {
		return packDescription;
	}

	public void setPackDescription(String packDescription) {
		this.packDescription = packDescription;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public void setCharge(BigDecimal charge) {
		this.charge = charge;
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

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
	

	public String getQtbillerId() {
		return qtbillerId;
	}

	public void setQtbillerId(String qtbillerId) {
		this.qtbillerId = qtbillerId;
	}

	@Override
	public String toString() {
		return "BillerPackages [id=" + id + ", packId=" + packId
				+ ", displayName=" + displayName + ", packDescription="
				+ packDescription + ", charge=" + charge + ", status=" + status
				+ ", billerId=" + billerId + ", packName=" + packName
				+ ", paymentCode=" + paymentCode + ", qtbillerId=" + qtbillerId
				+ "]";
	}
}
