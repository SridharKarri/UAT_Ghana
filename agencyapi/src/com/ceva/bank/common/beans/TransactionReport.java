package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Range;

@XmlRootElement(name="Report")
public class TransactionReport {

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private long channel;

	private String merchantId;

	@NotNull(message="This Field Is Required.")
	@Size(min=3, max=120, message="This field should be 3 - 120 Characters")
	private String userId;

	@NotNull(message="This Field Is Required.")
	@Size(min=5, max=10, message="This field should be 5 - 10 Characters")
	private String reportType;

	@NotNull(message="This Field Is Required.")
	private String fromDate;

	@NotNull(message="This Field Is Required.")
	private String toDate;

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		return "TransactionReport [channel=" + channel + ", merchantId="
				+ merchantId + ", userId=" + userId + ", reportType="
				+ reportType + "]";
	}

}
