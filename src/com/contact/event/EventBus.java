package com.contact.event;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {
	
	private static EventBus instance = null;
	
	protected EventListenerMap listeners = new EventListenerMap();
	protected ExecutorService pool = Executors.newFixedThreadPool(8);
	
	private EventBus()
	{
	}
	
	public static EventBus getInstance()
	{
		if(instance == null)
		{
			instance = new EventBus();
		}
		return instance;
	}
	
	public <T extends Event> void addListener(Class<T> eventType, EventListener<T> listener)
	{
		listeners.put(eventType, listener);
	}
	
	public <T extends Event> boolean removeListener(Class<T> eventType, EventListener<T> listener)
	{
		return listeners.remove(eventType, listener);
	}
	
	public <T extends Event> void broadcastEvent(Class<T> eventType, T event)
	{
		List<EventListener<T>> topicListeners = listeners.getListeners(eventType);
		for(EventListener<T> listener : topicListeners)
		{
			pool.execute(new Runnable()
					{
						public void run()
						{
							listener.handle(event);
						}
					});
		}
	}
}
