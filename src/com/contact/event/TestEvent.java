package com.contact.event;

public class TestEvent extends Event{
	
	private int data = 0;
	
	public TestEvent()
	{
	}
	
	public TestEvent(int data)
	{
		this.data = data;
	}
	
	public int getData()
	{
		return data;
	}
}
