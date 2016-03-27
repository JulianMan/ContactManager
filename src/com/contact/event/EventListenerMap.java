package com.contact.event;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class EventListenerMap {
	private Multimap<Class<? extends Event>, EventListener<? extends Event>> 
		listenersMap = HashMultimap.create();
	
	public EventListenerMap()
	{
	}
	
	public <E extends Event> void put(Class<E> c, EventListener<E> listener)
	{
		listenersMap.put(c, listener);
	}
	
	public <E extends Event> boolean remove(Class<E> c, EventListener<E> listener)
	{
		return listenersMap.remove(c, listener);
	}
	
	public <E extends Event> List<EventListener<E>> getListeners(Class<E> c)
	{
		List<EventListener<E>> listeners = new ArrayList<>();
		for(EventListener<? extends Event> listener : listenersMap.get(c))
		{
			// TODO: I think this is broken. It will cast events to type E
			listeners.add((EventListener<E>) listener);
		}
		return listeners;
	}
}
