package com.ceva.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "STORE_ACCOUNTS")
public class StoreAccount implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="STORE_ACCOUNT_ID", sequenceName="STORE_ACCOUNT_ID", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STORE_ACCOUNT_ID")
	private Long id;
	
	@Id
	@Column(name = "STORE_ID")
	private String storeId;

	@Column(name = "ACCNUM")
	private String accountNumber;
	@Column(name = "ACCNAME")
	private String accountName;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "STATUS")
	private String status;

	public StoreAccount() {
		super();
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
