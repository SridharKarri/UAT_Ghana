package com.ceva.bank.common.beans;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProductFormItems")
public class ProductFormItems {
	@XmlElement(name = "Id")
	private String id;
	@XmlElement(name = "Value")
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductFormItems [Id=");
		builder.append(id);
		builder.append(", Value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

}
