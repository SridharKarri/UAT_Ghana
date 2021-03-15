package com.ceva.db.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "APP_WALLETTXNLOG")
public class walletTxnLog {

	@Id
	private String txnRef;
	private String resquest;
	private String response;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;

	public String getTxnRef() {
		return txnRef;
	}

	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}

	public String getResquest() {
		return resquest;
	}

	public void setResquest(String resquest) {
		this.resquest = resquest;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
