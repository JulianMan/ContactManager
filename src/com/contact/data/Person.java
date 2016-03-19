package com.contact.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Person {
	private String name;
	private int userId;
	private int personId;
	
	private Map<String,String> attributes = new HashMap<>();
	
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
	
	public Map<String,String> getAttributes()
	{
		return attributes;
	}
	
	public void setAttributes(Map<String,String> attributes)
	{
		this.attributes = attributes;
	}
	
	public String getAttribute(String key)
	{
		return this.attributes.get(key);
	}
	
	public void setAttribute(String key, String value)
	{
		this.attributes.put(key, value);
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static List<Person> fromJson(String json)
	{
		Gson gson = new Gson();
		List<Person> persons = gson.fromJson(json, new TypeToken<ArrayList<Person>>(){}.getType());
		return persons;
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
