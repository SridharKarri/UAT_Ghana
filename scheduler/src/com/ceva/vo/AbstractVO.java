package com.ceva.vo;

import java.io.File;

/**
 * @author ceva
 *
 */
public abstract class AbstractVO {

	protected String senderName;

	protected String receiverName;

	protected String sendDate;

	protected String quotes;

	protected String senderEmailAddress;

	protected String[] toAddress;

	protected String vmTemplate;

	protected File[] attachment;

	protected String[] toCC;

	protected String[] toBcc;

	protected String subject;

	/**
	 * @return the quotes
	 */
	public String getQuotes() {
		return quotes;
	}

	/**
	 * @param quotes
	 *            the quotes to set
	 */
	public void setQuotes(String quotes) {
		this.quotes = quotes;
	}

	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * @param receiverName
	 *            the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * @return the sendDate
	 */
	public String getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate
	 *            the sendDate to set
	 */
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName
	 *            the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @return the senderEmailAddress
	 */
	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	/**
	 * @param senderEmailAddress
	 *            the senderEmailAddress to set
	 */
	public void setSenderEmailAddress(String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;
	}

	/**
	 * @return the toAddress
	 */
	public String[] getToAddress() {
		return this.toAddress;
	}

	/**
	 * @param toAddress
	 *            the toAddress to set
	 */
	public void setToAddress(String[] toAddress) {
		this.toAddress = toAddress;
	}

	/**
	 * @return the vmTemplate
	 */
	public String getVmTemplate() {
		return vmTemplate;
	}

	/**
	 * @param vmTemplate
	 *            the vmTemplate to set
	 */
	public void setVmTemplate(String vmTemplate) {
		this.vmTemplate = vmTemplate;
	}

	/**
	 * @return the attachment
	 */
	public File[] getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment
	 *            the attachment to set
	 */
	public void setAttachment(File[] attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the toBcc
	 */
	public String[] getToBcc() {
		return toBcc;
	}

	/**
	 * @param toBcc
	 *            the toBcc to set
	 */
	public void setToBcc(String[] toBcc) {
		this.toBcc = toBcc;
	}

	/**
	 * @return the toCC
	 */
	public String[] getToCC() {
		return toCC;
	}

	/**
	 * @param toCC
	 *            the toCC to set
	 */
	public void setToCC(String[] toCC) {
		this.toCC = toCC;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
