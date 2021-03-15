package com.ceva.response.beans;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {
	private String userId;
	private String status;
	private String lastLoggedIn;
	private String userName;
	private String email;
	private String mobileNo;
	private String store;
	private String agent;
	private String publicKey;
	private String iv;
	private String acountNumber;
	private String agentType;
	private String canOpenAccount;

	private List<String> txnCurrencies = new ArrayList<>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(String lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getAcountNumber() {
		return acountNumber;
	}

	public void setAcountNumber(String acountNumber) {
		this.acountNumber = acountNumber;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String isCanOpenAccount() {
		return canOpenAccount;
	}

	public void setCanOpenAccount(String canOpenAccount) {
		this.canOpenAccount = canOpenAccount;
	}

	public String getCanOpenAccount() {
		return canOpenAccount;
	}

	public List<String> getTxnCurrencies() {
		return txnCurrencies;
	}

	public void setTxnCurrencies(List<String> txnCurrencies) {
		this.txnCurrencies = txnCurrencies;
	}

	@Override
	public String toString() {
		return "LoginResponse [userId=" + userId + ", status=" + status + ", lastLoggedIn=" + lastLoggedIn
				+ ", userName=" + userName + ", email=" + email + ", mobileNo=" + mobileNo + ", store=" + store
				+ ", agent=" + agent + ", canOpenAccount=" + canOpenAccount + "]";
	}

}
