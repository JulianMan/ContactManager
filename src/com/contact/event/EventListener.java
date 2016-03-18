package com.contact.event;

public interface EventListener <E extends Event> {

	public abstract void handle(E event);
}
