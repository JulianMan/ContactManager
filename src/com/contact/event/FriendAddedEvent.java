package com.contact.event;

import com.contact.data.Person;

public class FriendAddedEvent extends Event {
	private Person person;

	public FriendAddedEvent(Person person) {
		super();
		this.person = person;
	}

	public String getName() {
		return "FriendAddedEvent";
	}

	public Person getPerson() {
		return person;
	}
}
