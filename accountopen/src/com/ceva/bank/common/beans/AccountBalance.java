package com.ceva.bank.common.beans;

import java.math.BigDecimal;

public class AccountBalance {
	private BigDecimal balance;
	private String currencyCode;
	private String bookBalance;

	public BigDecimal getBalance() {
		return balance;
	}
	

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}


	public String getCurrencyCode() {
		return currencyCode;
	}
	


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	


	public String getBookBalance() {
		return bookBalance;
	}
	


	public void setBookBalance(String bookBalance) {
		this.bookBalance = bookBalance;
	}


	@Override
	public String toString() {
		return "AccountBalance [balance=" + balance + ", currencyCode=" + currencyCode + ", bookBalance=" + bookBalance
				+ "]";
	}
	
	
}
