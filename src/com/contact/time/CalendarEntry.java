package com.contact.time;

import java.time.LocalDateTime;

public class CalendarEntry {
	
	private int entryId;
	private int userId;
	private String title = "";
	private LocalDateTime time = null;
	private Long recurrence = null;
	
	public CalendarEntry()
	{
	}

	public int getEntryId()
	{
		return entryId;
	}
	
	public void setEntryId(int entryId)
	{
		this.entryId = entryId;
	}
	
	public int getUserId() 
	{
		return userId;
	}

	public void setUserId(int userId) 
	{
		this.userId = userId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public LocalDateTime getTime() 
	{
		return time;
	}

	public void setTime(LocalDateTime time) 
	{
		this.time = time;
	}

	public Long getRecurrence()
	{
		return recurrence;
	}

	public void setRecurrence(Long recurrency) 
	{
		this.recurrence = recurrency;
	}

}
