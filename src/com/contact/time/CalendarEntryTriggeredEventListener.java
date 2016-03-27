package com.contact.time;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import com.contact.communication.Messager;
import com.contact.communication.TwillioMessager;
import com.contact.event.CalendarEntryTriggeredEvent;
import com.contact.event.EventListener;

public class CalendarEntryTriggeredEventListener implements EventListener<CalendarEntryTriggeredEvent> {
	Logger logger = Logger.getGlobal();
	Messager messager = new TwillioMessager();
	@Override
	public void handle(CalendarEntryTriggeredEvent event) {
		CalendarEntry entry = event.getCalendarEntry();
		logger.info("Handling CalendarEntryTriggeredEvent called " + entry.getTitle() + " with message " + entry.getMessage() );
		
		boolean messageSuccess = messager.message(entry.getUserId(), entry.getMessage());
		
		if(entry.getRecurrence() > 0) {
			LocalDateTime dateTime = entry.getTime();
			dateTime = dateTime.plusSeconds(entry.getRecurrence());
			entry.setTime(dateTime);
		} else if(messageSuccess) {
			entry.setNotified(true);
		}

		TimeManager.getInstance().update(entry);	
	}

}
