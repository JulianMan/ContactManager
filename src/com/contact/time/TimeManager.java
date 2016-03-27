package com.contact.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.contact.base.BaseManager;
import com.contact.event.CalendarEntryTriggeredEvent;

public class TimeManager extends BaseManager<CalendarEntry> {
	
	private static TimeManager instance = null;
	private Map<Integer, ScheduledFuture<?>> futures = new HashMap<Integer, ScheduledFuture<?>>();
		
	private TimeManager()
	{
		eventBus.addListener(CalendarEntryTriggeredEvent.class, new CalendarEntryTriggeredEventListener());
		
		// TODO: Support all users
		List<CalendarEntry> entries = read(1);
		entries.forEach(entry -> scheduleCalendarEntry(entry));
	}
	
	public static TimeManager getInstance()
	{
		if(instance == null)
		{
			instance = new TimeManager();
		}
		return instance;
	}
	
	public boolean create(CalendarEntry ce)
	{
		boolean success = dataModel.create(ce);
		scheduleCalendarEntry(ce);
		return success;
	}
	
	public List<CalendarEntry> read(int userId)
	{
		return dataModel.read(CalendarEntry.class, userId);
	}
	
	public CalendarEntry read(int userId, int resourceId)
	{
		return dataModel.read(CalendarEntry.class, userId, resourceId);
	}
	
	public boolean update(CalendarEntry ce)
	{
		boolean success = dataModel.update(ce);
		scheduleCalendarEntry(ce);
		return success;
	}
	
	public boolean delete(CalendarEntry ce)
	{
		boolean success = dataModel.delete(ce);
		futures.get(ce.getEntryId()).cancel(false);
		futures.remove(ce.getEntryId());
		return success;
	}
	
	private void scheduleCalendarEntry(CalendarEntry ce) {
        if(futures.containsKey(ce.getEntryId())) {
        	futures.get(ce.getEntryId()).cancel(false);
        } 
		
		if(ce.isNotified()) {
			return;
		}
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime entryTime = ce.getTime();

        Duration duration = Duration.between(now, entryTime);
        long initalDelay = duration.getSeconds();
        
        if(initalDelay < 0 && ce.getRecurrence() > 0) {
    		while(initalDelay + ce.getRecurrence() < 0) {
    			entryTime = entryTime.plusSeconds(ce.getRecurrence());
    			ce.setTime(entryTime);
    			duration = Duration.between(now, entryTime);
    	        initalDelay = duration.getSeconds();
    		}
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> future = scheduler.schedule(ce, initalDelay, TimeUnit.SECONDS);

        futures.put(ce.getEntryId(), future);
	}
}
