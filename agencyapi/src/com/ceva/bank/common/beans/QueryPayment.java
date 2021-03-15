package com.ceva.bank.common.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@XmlRootElement(name = "QueryPayment")
public class QueryPayment {

	@Range(min=1, max=5, message="This field should be 1 - 5 Numbers")
	private String channel;

	@Size(message = "Reference Numner Should Not empty.")
	@Length(min = 1, max = 120, message = "This Field Should between 1-120 Characters.")
	private String refNumber;

	@Size(message = "This Field is Required.")
	@Length(min = 1, max = 120, message = "This Field Should between 1-120 Characters.")
	private String makerId;

	@Size(message = "This Field is Required.")
	@Length(min = 1, max = 120, message = "This Field Should between 1-120 Characters.")
	private String merchantId;

	private String serviceId;

	@NotNull(message="This Field Is Required.")
	@Size(min=10, max=10, message="This field should be 10 Numeric Digits")
	private String mobileNumber;

	private String terminalId;

	private String requestReference;

	public QueryPayment() {
		super();
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getRequestReference() {
		return requestReference;
	}


	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public void setRequestReference(String requestReference) {
		this.requestReference = requestReference;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "QueryPayment [channel=" + channel + ", refNumber=" + refNumber
				+ ", makerId=" + makerId + ", merchantId=" + merchantId
				+ ", serviceId=" + serviceId + ", mobileNumber=" + mobileNumber
				+ ", terminalId=" + terminalId + ", requestReference="
				+ requestReference + "]";
	}



}
