package com.ceva.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountImage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String mandate;
	private String idImage;
	private String maker;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMandate() {
		return mandate;
	}

	public void setMandate(String mandate) {
		this.mandate = mandate;
	}

	public String getIdImage() {
		return idImage;
	}

	public void setIdImage(String idImage) {
		this.idImage = idImage;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	@Override
	public String toString() {
		return "AccountImage [id=" + id + "]";
	}

}
