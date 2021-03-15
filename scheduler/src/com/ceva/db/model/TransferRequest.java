package com.ceva.db.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="FINACLE_TXN")
public class TransferRequest {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;

	@NotNull(message="This Field is Required.")
	@Length(min=10, max =13, message="This Field Length Should be 10 -13 Numeric Digits.")
	private String accountNumber;

	@NotNull(message="This Field is Required.")
   // @Range(min=10, max=20000, message="Range Should Between 10 to 20000")
	private BigDecimal amount =new BigDecimal(0);

	@NotNull(message="This Field is Required.")
    private String dateTime;

	@NotNull(message="This Field is Required.")
	@Length(min=10, max =13, message="This Field Length Should be 10 -13 Numeric Digits.")
    private String beneficiaryAccount;

	@NotNull(message="This Field is Required.")
    private String beneficiaryName;

	@NotNull(message="This Field is Required.")
    private String bankCode;

    private String referenceCode;

    @NotNull(message="This Field is Required.")
    private String narration;

    @NotNull(message="This Field is Required.")
    private String makerId;

    @NotNull(message="This Field is Required.")
    private String channel;

    private String status;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getBeneficiaryAccount() {
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(String beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TransferRequest [id="+id+", accountNumber=" + accountNumber + ", amount="
				+ amount + ", dateTime=" + dateTime + ", beneficiaryAccount="
				+ beneficiaryAccount + ", beneficiaryName=" + beneficiaryName
				+ ", bankCode=" + bankCode + ", referenceCode=" + referenceCode
				+ ", narration=" + narration + ", makerId=" + makerId
				+ ", channel=" + channel
				+ ", status=" + status+ "]";
	}


}
