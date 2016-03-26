package com.contact.person;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.contact.event.EventListener;
import com.contact.event.FriendAddedEvent;

public class FriendAddedEventListener implements EventListener<FriendAddedEvent> {
	private static Logger logger = Logger.getGlobal();

	@Override
	public void handle(FriendAddedEvent event) {
		PersonManager manager = PersonManager.getInstance();
		List<Person> people = manager.read(event.getPerson().getUserId());
		Person addedPerson = event.getPerson();
		
		logger.info("FriendAddedEvent for: " + addedPerson.getName());
		
		// Find every contact the user has that has the same name as the added friend,
		// and update those contacts (rudimentary first pass matching logic)
		// TODO: Better matching logic
		for (Person person : people) {
			if (person.getName() == addedPerson.getName()) {
				Map<String, Attribute> attributes = person.getAttributes();
				
				for(String key : addedPerson.getAttributes().keySet()) {
					if(attributes.containsKey(key)) {
						String newValue = addedPerson.getAttribute(key).getValue();
						attributes.get(key).setValue(newValue);
					} else {
						person.setAttribute(addedPerson.getAttribute(key));
					}
				}
				manager.update(person);
				return;
			}
		}
		
		manager.create(addedPerson);
	}

}
