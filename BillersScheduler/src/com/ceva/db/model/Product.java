package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "PRODUCT")
public class Product {
	@Id
	@Column(name="ID")
	private Long Id;
	@Column(name="PRODUCT_NAME")
	private String prodCode;
	@Column(name="PRDDESC")
	private String prodDesc;
	@Column(name="TXN_LIMIT")
	private double txnLmit;
	@Column(name="FREQUENCY")
    private double frequency;
	@Column(name="DAILY_TXN_LIMIT")
	private double dailyTxnLimit;
	@Column(name="WEEK_TXN_LIMIT")
    private double weekTxnLimit;
	@Column(name="MONTH_TXN_LIMIT")
    private double monthTxnLimit;
	@Column(name="TXN_COUNT_LIMIT")
    private double txnCountLimit;
	@Column(name="REF_NUM")
    private String refNum;
	@Column(name="AUTH_FLAG")
    private String authFlag;
	@Column(name="STATUS")
    private String status;

	@Column(name="MAKER")
	private String makerId;
	@Column(name="CHECKER")
	private String authId;
	@Column(name="MAKER_DTTM")
    @Temporal(TemporalType.DATE)
	private Date makerDttm;
	@Column(name="CHECK_DTTM")
	@Temporal(TemporalType.DATE)
	private Date authDttm;


	public Product() {
		super();
	}

	public Product(Long id, String prodCode, String prdDesc, String makerId,
			String authId, Date makerDttm, Date authDttm) {
		super();
		Id = id;
		this.prodCode = prodCode;
		this.prodDesc = prdDesc;
		this.makerId = makerId;
		this.authId = authId;
		this.makerDttm = makerDttm;
		this.authDttm = authDttm;
	}

	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getProdDesc() {
		return prodDesc;
	}
	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
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

	public double getTxnLmit() {
		return txnLmit;
	}

	public void setTxnLmit(double txnLmit) {
		this.txnLmit = txnLmit;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getDailyTxnLimit() {
		return dailyTxnLimit;
	}

	public void setDailyTxnLimit(double dailyTxnLimit) {
		this.dailyTxnLimit = dailyTxnLimit;
	}

	public double getWeekTxnLimit() {
		return weekTxnLimit;
	}

	public void setWeekTxnLimit(double weekTxnLimit) {
		this.weekTxnLimit = weekTxnLimit;
	}

	public double getMonthTxnLimit() {
		return monthTxnLimit;
	}

	public void setMonthTxnLimit(double monthTxnLimit) {
		this.monthTxnLimit = monthTxnLimit;
	}

	public double getTxnCountLimit() {
		return txnCountLimit;
	}

	public void setTxnCountLimit(double txnCountLimit) {
		this.txnCountLimit = txnCountLimit;
	}

	public String getRefNum() {
		return refNum;
	}

	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}

	public String getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}

	@Override
	public String toString() {
		return "Product [Id=" + Id + ", prodCode=" + prodCode + ", prodDesc="
				+ prodDesc + ", makerId=" + makerId + ", authId=" + authId
				+ ", makerDttm=" + makerDttm + ", authDttm=" + authDttm
				+ ", txnLmit=" + txnLmit + ", frequency=" + frequency
				+ ", dailyTxnLimit=" + dailyTxnLimit + ", weekTxnLimit="
				+ weekTxnLimit + ", monthTxnLimit=" + monthTxnLimit
				+ ", txnCountLimit=" + txnCountLimit + ", refNum=" + refNum
				+ ", authFlag=" + authFlag + ", status=" + status + "]";
	}

}
