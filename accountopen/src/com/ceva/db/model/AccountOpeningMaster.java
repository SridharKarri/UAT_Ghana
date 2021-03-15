package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ACCOUNT_OPENING_MASTER")
public class AccountOpeningMaster{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="ACCOUNT_SERVICE")
	private String accountServiceName;

	@Column(name="JSON_DATA")
	private String jsonData;

	private String status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountServiceName() {
		return accountServiceName;
	}

	public void setAccountServiceName(String accountServiceName) {
		this.accountServiceName = accountServiceName;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AccountOpeningMaster [id=" + id + ", accountServiceName="
				+ accountServiceName + ", jsonData=" + jsonData + ", status=" + status+"]";
	}
}