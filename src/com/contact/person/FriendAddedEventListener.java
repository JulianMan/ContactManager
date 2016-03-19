package com.contact.person;

import java.util.List;

import com.contact.data.Person;
import com.contact.event.EventListener;
import com.contact.event.FriendAddedEvent;

public class FriendAddedEventListener implements EventListener<FriendAddedEvent> {

	@Override
	public void handle(FriendAddedEvent event) {
		PersonManager manager = PersonManager.getInstance();
		List<Person> people = manager.read(event.getPerson().getUserId());
		Person addedPerson = event.getPerson();
		
		// Find every contact the user has that has the same name as the added friend,
		// and update those contacts (rudimentary first pass matching logic)
		// TODO: Better matching logic
		for (Person person : people) {
			if (person.getName() == addedPerson.getName()) {
				for(String key : addedPerson.getAttributes().keySet()) {
					person.setAttribute(key, addedPerson.getAttribute(key));
				}
				manager.update(person);
			}
		}
	}

}
