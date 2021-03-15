package com.ceva.bank.common.beans;

public class MinistmtResponse {

	private String amount;
	private String date;
	private String narration;
	private String time;
	private String transType;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	@Override
	public String toString() {
		return "MinistatementResponse [amount=" + amount + ", date=" + date
				+ ", narration=" + narration + ", time=" + time
				+ ", transType=" + transType + "]";
	}
	
}
