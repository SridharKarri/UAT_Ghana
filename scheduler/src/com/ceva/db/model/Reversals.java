package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TXN_REVERSALS")
public class Reversals {

	@Id
	@SequenceGenerator(name = "REV_ID", sequenceName = "REV_ID", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REV_ID")
	private Long id;
	private String refNumber;
	private String abReference;
	private String interfaceName;
	private String revRefNumber;
	private String status="P";
	@Column(name="RETRYCOUNT")
	private Long retryCount;
	private String message;

	public Reversals() {
		super();
	}

	public Reversals(String refNumber, String abReference, String interfaceName) {
		super();
		this.refNumber = refNumber;
		this.abReference = abReference;
		this.interfaceName = interfaceName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getAbReference() {
		return abReference;
	}

	public void setAbReference(String abReference) {
		this.abReference = abReference;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Long retryCount) {
		this.retryCount = retryCount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRevRefNumber() {
		return revRefNumber;
	}

	public void setRevRefNumber(String revRefNumber) {
		this.revRefNumber = revRefNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Reversals [id=").append(id).append(", refNumber=").append(refNumber).append(", abReference=")
				.append(abReference).append(", interfaceName=").append(interfaceName).append(", status=").append(status)
				.append(", retryCount=").append(retryCount).append(", message=").append(message).append("]");
		return builder.toString();
	}
}
