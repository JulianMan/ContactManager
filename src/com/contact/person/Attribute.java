package com.contact.person;

public class Attribute {
	private String attributeName;
	private String attributeValue;
	private int id;
	
	public Attribute(String attributeName, String attributeValue) {
		this(attributeName, attributeValue, -1);
	}

	public Attribute(String attributeName, String attributeValue, int id) {
		this.setName(attributeName);
		// TODO Auto-generated constructor stub
		this.setValue(attributeValue);
		this.setId(id);
	}

	public String getName() {
		return attributeName;
	}

	public void setName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getValue() {
		return attributeValue;
	}

	public void setValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
