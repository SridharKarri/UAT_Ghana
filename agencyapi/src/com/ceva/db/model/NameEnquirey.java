package com.ceva.db.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;

@XmlRootElement(name="Enquirey")
public class NameEnquirey {

	private String bankCode;

	@NotNull
	@Size(message="Beneficiary Account Should Not empty.")
	@Length(min=10, max=10, message="This Field Should be 10 Numeric Digits.")
	private String beneficiaryAccount;

	public NameEnquirey() {
		super();
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBeneficiaryAccount() {
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(String beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}

	@Override
	public String toString() {
		return "NameEnquirey [bankCode=" + bankCode + ", beneficiaryAccount="
				+ beneficiaryAccount + "]";
	}

}
