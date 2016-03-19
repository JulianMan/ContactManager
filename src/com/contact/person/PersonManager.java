package com.contact.person;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.contact.data.Person;
import com.contact.event.EventBus;
import com.contact.event.FriendAddedEvent;
import com.contact.event.PersonCreatedEvent;
import com.contact.manager.Manager;
import com.contact.manager.data.DataModel;
import com.contact.manager.data.ITables;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		String json = dataModel.read(ITables.PERSON_TABLE, userId);
		Gson gson = new Gson();
		List<Person> persons = gson.fromJson(json, new TypeToken<ArrayList<Person>>(){}.getType());
		return persons;
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
