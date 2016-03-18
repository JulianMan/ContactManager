package com.contact.manager.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import com.contact.data.CalendarEntry;
import com.contact.data.Person;
import com.google.gson.Gson;

public class DataModel {
	private static DataModel instance = null;
	private Connection connection;
	
	private PersonTableManager personTableManager;
	private CalendarEntryTableManager calendarEntryTableManager;
	
	private DataModel()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("");
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
	public String read(String table, int key)
	{
		Gson gson = new Gson();
		if("person".equals(table))
		{
			List<Person> people = personTableManager.read(key);
			return gson.toJson(people);
		}
		else if("calendar_entry".equals(table))
		{
			List<CalendarEntry> calendarEntries = calendarEntryTableManager.read(key);
			return gson.toJson(calendarEntries);
		}
		return "";
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
		System.out.println("Bad object type " + o.getClass().getName()
				+ " for operation \"" + operation + "\"");
	}
}
