package com.ceva.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "STATE_CITY")
public class States {

	@Id
	@GeneratedValue
	@Column(name="STATE_CODE")
	private String stateCode;
	@Column(name="STATE_NAME")
	private String stateName;
	
	@Transient
	private String localGovernment;
	@Column(name="CITYCODE")
	private String cityCode;
	@Column(name="CITYNAME")
	private String cityName;
	
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getLocalGovernment() {
		return localGovernment;
	}
	public void setLocalGovernment(String localGovernment) {
		this.localGovernment = localGovernment;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	@Override
	public String toString() {
		return "States [ stateName=" + stateName
				+ ", localGovernment=" + localGovernment + ", stateCode="
				+ stateCode + "]";
	}
	
}
