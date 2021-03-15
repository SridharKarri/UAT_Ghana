package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;





@Entity
@Table(name = "billerCategory")
public class BillerCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAT_ID")
	@SequenceGenerator(name = "CAT_ID", sequenceName = "CAT_ID", allocationSize = 50)
	private long id;
	private String catId;
	private String name;
	private String description;

	public BillerCategory() {}

	public BillerCategory(String catId, String name) {
		this.catId = catId;
		this.name = name;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCatId() {
		return this.catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}


	public String toString() {
		return "BillerCategory [id=" + id + ", name=" + name + ", catId=" + catId + ", description=" + description + "]";
	}
}