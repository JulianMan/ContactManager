package com.contact.test.unit;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.contact.event.EventBus;
import com.contact.event.EventListener;

public class EventBusTest {
	@Rule
    public final ExpectedException exception = ExpectedException.none();
	
	long timeout = 500;
	
	@Test
	public void testReceiveEvent()
	{
		TestEventListener listener = new TestEventListener();
		EventBus.getInstance().addListener(TestEvent.class, listener);
		EventBus.getInstance().broadcastEvent(TestEvent.class, new TestEvent());
		
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start < timeout)
		{
		}
		
		EventBus.getInstance().removeListener(TestEvent.class, listener);
		Assert.assertTrue(listener.receivedEvent());
	}
	
	@Test
	public void testNullListener()
	{
		exception.expect(IllegalArgumentException.class);
		EventBus.getInstance().addListener(TestEvent.class, null);
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
	
	@Test
	public void testMultipleListeners()
	{
		TestEventListener listener1 = new TestEventListener();
		TestEventListener listener2 = new TestEventListener();
		EventBus.getInstance().addListener(TestEvent.class,listener1);
		EventBus.getInstance().addListener(TestEvent.class,listener2);
		
		EventBus.getInstance().broadcastEvent(TestEvent.class, new TestEvent());
		
		pause(timeout);
		
		EventBus.getInstance().removeListener(TestEvent.class, listener1);
		EventBus.getInstance().removeListener(TestEvent.class, listener2);
		Assert.assertTrue(listener1.receivedEvent() && listener2.receivedEvent());
	}
	
	// One listener throwing an exception should not stop another listener from receiving event
	@Test
	public void testListenerThrowingException()
	{
		EventListener<TestEvent> listener1 = new EventListener<TestEvent>(){
			// Throw a null pointer exception
			@SuppressWarnings("null")
			@Override
			public void handle(TestEvent event) {
				Object unsafe = null;
				System.out.println(unsafe.toString());
			}
		};		
		TestEventListener listener2 = new TestEventListener();
		
		EventBus.getInstance().addListener(TestEvent.class,listener1);
		EventBus.getInstance().addListener(TestEvent.class,listener2);
		
		EventBus.getInstance().broadcastEvent(TestEvent.class, new TestEvent());
		
		pause(timeout);
		
		Assert.assertTrue(listener2.receivedEvent());
	}
	
	public static class TestEventListener implements EventListener<TestEvent>
	{
		private boolean received = false;
		public void handle(TestEvent testEvent)
		{
			received = true;
		}
		
		public boolean receivedEvent()
		{
			return received;
		}
	}
	
	protected static void pause(long duration)
	{
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start < duration)
		{
		}
	}
}
