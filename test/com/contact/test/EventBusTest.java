package com.contact.test;

import org.junit.Assert;
import org.junit.Test;

import com.contact.event.EventBus;
import com.contact.event.EventListener;
import com.contact.event.TestEvent;

public class EventBusTest {
	
	boolean received = false;
	long timeout = 500;
	
	@Test
	public void testReceiveEvent()
	{
		EventBus.getInstance().addListener(TestEvent.class, new TestEventListener());
		EventBus.getInstance().broadcastEvent(TestEvent.class, new TestEvent());
		
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start < timeout)
		{
		}
		
		Assert.assertTrue(received);
	}
	
	@Test
	public void testRemoveListener1()
	{
		TestEventListener listener = new TestEventListener();
		EventBus.getInstance().addListener(TestEvent.class, listener);
		boolean success = EventBus.getInstance().removeListener(TestEvent.class, listener);
		Assert.assertTrue(success);
	}
	
	@Test
	public void testRemoveListener2()
	{
		TestEventListener listener = new TestEventListener();
		boolean success = EventBus.getInstance().removeListener(TestEvent.class, listener);
		Assert.assertFalse(success);
	}
	
	public class TestEventListener implements EventListener<TestEvent>
	{
		public void handle(TestEvent testEvent)
		{
			received = true;
		}
	}
}
