package com.contact.time;

import java.time.LocalDateTime;

import com.contact.event.CalendarEntryTriggeredEvent;
import com.contact.event.EventBus;

public class CalendarEntry implements Runnable {
	
	private int entryId = -1;
	private int userId = -1;
	private String title = null;
	private LocalDateTime time = null;
	private Long recurrence = null;
	private boolean notified = false;
	private String message = null;
	
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
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + entryId;
		result = prime * result + ((recurrence == null) ? 0 : recurrence.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalendarEntry other = (CalendarEntry) obj;
		if (entryId != other.entryId)
			return false;
		if (recurrence == null) {
			if (other.recurrence != null)
				return false;
		} else if (!recurrence.equals(other.recurrence))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public void run() {
		CalendarEntryTriggeredEvent event = new CalendarEntryTriggeredEvent(this);
		EventBus.getInstance().broadcastEvent(CalendarEntryTriggeredEvent.class, event);
	}

	public boolean isNotified() {
		return notified;
	}

	public void setNotified(boolean notified) {
		this.notified = notified;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
