package com.contact.person;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Person {
	private String name;
	private int userId;
	private int personId;
	
	private Map<String,Attribute> attributes = new HashMap<>();
	
	public Person()
	{
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
	public Map<String,Attribute> getAttributes()
	{
		return attributes;
	}
	
	public void setAttributes(Map<String,Attribute> attributes)
	{
		this.attributes = attributes;
	}
	
	public Attribute getAttribute(String key)
	{
		return this.attributes.get(key);
	}
	
	public void setAttribute(Attribute attribute)
	{
		this.attributes.put(attribute.getName(), attribute);
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static Person fromJson(String json)
	{
		Gson gson = new Gson();
		Person person = gson.fromJson(json, new TypeToken<Person>(){}.getType());
		return person;
	}
	
	public boolean equals(Object other)
	{
		if(other instanceof Person)
		{
			Person otherPerson = (Person) other;
			return this.userId == otherPerson.userId
					&& (this.personId == otherPerson.personId);
		}
		return false;
	}
}
