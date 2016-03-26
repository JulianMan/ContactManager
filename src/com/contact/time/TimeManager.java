package com.contact.time;

import java.util.ArrayList;
import java.util.List;

import com.contact.datalayer.DataModel;
import com.contact.datalayer.ITables;
import com.contact.manager.Manager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TimeManager extends Manager {
	
	private static TimeManager instance = null;
		
	private TimeManager()
	{
	}
	
	public static TimeManager getInstance()
	{
		if(instance == null)
		{
			instance = new TimeManager();
		}
		return instance;
	}
	
	public boolean createCalendarEntry(CalendarEntry ce)
	{
		boolean success = dataModel.create(ce);
		return success;
	}
	
	public List<CalendarEntry> readCalendarEntry(int userId)
	{
		return dataModel.read(CalendarEntry.class, userId);
	}
	
	public boolean updateCalendarEntry(CalendarEntry ce)
	{
		boolean success = dataModel.update(ce);
		return success;
	}
	
	public boolean deleteCalendarEntry(CalendarEntry ce)
	{
		boolean success = dataModel.delete(ce);
		return success;
	}
}
