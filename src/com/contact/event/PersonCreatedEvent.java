package com.contact.event;

import com.contact.data.Person;

public class PersonCreatedEvent extends Event {
	
	private Person person;
	
	public PersonCreatedEvent(Person person)
	{
		this.person = person;
	}
	
	public Person getPerson()
	{
		return person;
	}
}
