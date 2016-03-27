package com.contact.event;

import com.contact.time.CalendarEntry;

public class CalendarEntryTriggeredEvent extends Event {
	private CalendarEntry calendarEntry;
	
	public CalendarEntryTriggeredEvent(CalendarEntry calendarEntry) {
		this.setCalendarEntry(calendarEntry);
	}
	public String getName() {
		return "CalendarEntryTriggeredEvent";
	}
	public CalendarEntry getCalendarEntry() {
		return calendarEntry;
	}
	public void setCalendarEntry(CalendarEntry calendarEntry) {
		this.calendarEntry = calendarEntry;
	}
}
