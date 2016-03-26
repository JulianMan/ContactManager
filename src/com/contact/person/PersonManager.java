package com.contact.person;

import java.util.List;

import com.contact.data.Person;
import com.contact.event.FriendAddedEvent;
import com.contact.event.PersonCreatedEvent;
import com.contact.manager.Manager;

public class PersonManager extends Manager {
	
	private static PersonManager instance = null;
	
	private PersonManager()
	{
		eventBus.addListener(FriendAddedEvent.class, new FriendAddedEventListener());
	}
	
	public static PersonManager getInstance()
	{
		if(instance == null)
		{
			instance = new PersonManager();
		}
		return instance;
	}
	
	public boolean create(Person person)
	{
		boolean success = dataModel.create(person);
		if(success)
		{
			eventBus.broadcastEvent(PersonCreatedEvent.class, new PersonCreatedEvent(person));
		}
		return success;
	}
	
	public List<Person> read(int userId)
	{
		return dataModel.read(Person.class, userId);
	}
	
	public Person read(int userId, int personId)
	{
		return dataModel.read(Person.class, userId, personId);
	}
	
	public boolean update(Person person)
	{
		boolean success = dataModel.update(person);
		logger.info("Updated Person: " + person.toJson());
		if(success)
		{
			// TODO: broadcast event
		}
		return success;
	}
	
	public boolean delete(Person person)
	{
		boolean success = dataModel.delete(person);
		if(success)
		{
			// TODO: broadcast event
		}
		return success;
	}
}
