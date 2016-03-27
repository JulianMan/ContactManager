package com.contact.datalayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.logging.Logger;

import com.contact.person.Person;
import com.contact.time.CalendarEntry;

public class DataModel {
	private static DataModel instance = null;
	private Connection connection;
	private Logger logger = Logger.getGlobal();
	
	private PersonTableManager personTableManager;
	private CalendarEntryTableManager calendarEntryTableManager;
	
	private DataModel()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/contactmanager","contactmanager", "pass");
			personTableManager = new PersonTableManager(connection);
			calendarEntryTableManager = new CalendarEntryTableManager(connection);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static DataModel getInstance()
	{
		if(instance == null)
		{
			instance = new DataModel();
		}
		return instance;
	}
	
	public boolean create(Object o)
	{
		boolean success = false;
		if(o instanceof Person)
		{
			success = personTableManager.create((Person) o);
		}
		else if(o instanceof CalendarEntry)
		{
			success = calendarEntryTableManager.create((CalendarEntry) o);
		}
		else
		{
			badObjectType(o,"create");
		}
		return success;
	}
	// TODO: Remove magic strings, return "error" JSON
	@SuppressWarnings("unchecked")
	public <T> List<T> read(Class<T> type, int userId)
	{
		if(type == Person.class)
		{
			return (List<T>)personTableManager.read(userId);
		}
		else if(type == CalendarEntry.class)
		{
			return (List<T>)calendarEntryTableManager.read(userId);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T read(Class<T> type, int userId, int resourceId)
	{
		if(type == Person.class)
		{
			return (T)personTableManager.read(userId, resourceId);
		}
		else if(type == CalendarEntry.class)
		{
			return (T)calendarEntryTableManager.read(userId, resourceId);
		}
		return null;
	}
	
	public boolean update(Object o)
	{
		boolean success = false;
		if(o instanceof Person)
		{
			success = personTableManager.update((Person) o);
		}
		else if(o instanceof CalendarEntry)
		{
			success = calendarEntryTableManager.update((CalendarEntry) o);
		}
		else
		{
			badObjectType(o,"update");
		}
		return success;
	}
	
	public boolean delete(Object o)
	{
		boolean success = false;
		if(o instanceof Person)
		{
			success = personTableManager.delete((Person) o);
		}
		else if(o instanceof CalendarEntry)
		{
			success = calendarEntryTableManager.delete((CalendarEntry) o);
		}
		else
		{
			badObjectType(o,"delete");
		}
		return success;
	}
	
	private void badObjectType(Object o, String operation)
	{
		logger.severe("Bad object type " + o.getClass().getName()
				+ " for operation \"" + operation + "\"");
	}
}
