package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SUSPECTED_FRAUD")
public class SuspectedFraud {
	
	@Id
	@GeneratedValue
	private Integer id;
	private String accountOrCard;
	private String message;
	private String status;
	private String maker;
	private String dateTime;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getAccountOrCard() {
		return accountOrCard;
	}
	
	public void setAccountOrCard(String accountOrCard) {
		this.accountOrCard = accountOrCard;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMaker() {
		return maker;
	}
	
	public void setMaker(String maker) {
		this.maker = maker;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "SuspectedFraud [id=" + id + ", accountOrCard=" + accountOrCard + ", message=" + message + ", status="
				+ status + ", maker=" + maker + ", dateTime=" + dateTime + "]";
	}

}
