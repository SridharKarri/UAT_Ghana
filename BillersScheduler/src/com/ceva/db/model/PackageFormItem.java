package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BILLER_FORM_ITEMS")
public class PackageFormItem
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORM_ITEM_ID")
	@SequenceGenerator(name = "FORM_ITEM_ID", sequenceName = "FORM_ITEM_ID", allocationSize = 50)
	private long id;
	@Column(name = "FORM_ITEMS")
	private String formItems;
	@Column(name = "PACK_ID")
	private Long packId;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFormItems() {
		return this.formItems;
	}

	public void setFormItems(String formItems) {
		this.formItems = formItems;
	}

	public Long getPackId() {
		return this.packId;
	}

	public void setPackId(Long packId) {
		this.packId = packId;
	}
}
