package com.contact.test;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.contact.event.Event;
import com.contact.event.EventListener;
import com.contact.event.EventListenerMap;
import com.contact.event.PersonCreatedEvent;
import com.contact.event.TestEvent;

public class EventListenerMapTest {

	@Rule
    public final ExpectedException exception = ExpectedException.none();
	
	protected EventListenerMap listeners = new EventListenerMap();
	
	public EventListenerMapTest()
	{
	}
	
	@Test
	public void testAddListener()
	{
		TestEventListener listener = new TestEventListener();
		listeners.put(TestEvent.class, listener);
		Assert.assertTrue(listeners.getListeners(TestEvent.class).contains(listener));
	}
	
	@Test
	public void testAddMultipleListeners()
	{
		addThreeListeners();
		
		Collection<EventListener<TestEvent>> threeListeners = listeners.getListeners(TestEvent.class);
		Assert.assertTrue(threeListeners.size() == 3);
	}
	
	@Test
	public void testAddNullListener()
	{
		exception.expect(IllegalArgumentException.class);
		listeners.put(TestEvent.class, null);
	}
	
	@Test
	public void testRemoveListenerSuccess()
	{
		TestEventListener toRemove = new TestEventListener();
		listeners.put(TestEvent.class, toRemove);
		addThreeListeners();
		boolean success = listeners.remove(TestEvent.class, toRemove);
		Assert.assertTrue(success);
	}
	
	@Test
	public void testRemoveListenerNoLongerPresent()
	{
		TestEventListener toRemove = new TestEventListener();
		listeners.put(TestEvent.class, toRemove);
		addThreeListeners();
		listeners.remove(TestEvent.class, toRemove);
		Collection<EventListener<TestEvent>> remaining = listeners.getListeners(TestEvent.class);
		Assert.assertFalse(remaining.contains(toRemove));
	}
	
	@Test
	public void testRemoveListenerOthersRemain()
	{
		TestEventListener toRemove = new TestEventListener();
		listeners.put(TestEvent.class, toRemove);
		addThreeListeners();
		listeners.remove(TestEvent.class, toRemove);
		Collection<EventListener<TestEvent>> remaining = listeners.getListeners(TestEvent.class);
		Assert.assertTrue(remaining.size() == 3);
	}
	
	@Test
	public void testRemoveNonExistentListener()
	{
		addThreeListeners();
		boolean success = listeners.remove(TestEvent.class, new TestEventListener());
		Assert.assertFalse(success);
	}
	
	@Test
	public void testRemoveNullListener()
	{
		addThreeListeners();
		exception.expect(IllegalArgumentException.class);
		listeners.remove(TestEvent.class, null);
	}
	
	@Test
	public void testGetZeroListeners()
	{
		addThreeListeners(); // Adds three TestEventListeners
		Assert.assertTrue(listeners.getListeners(PersonCreatedEvent.class).isEmpty());
	}
	
	@Test
	public void testGetMultipleListenerTypes1()
	{
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent2.class, new ChildEvent2.Listener());
		listeners.put(ChildEvent2.class, new ChildEvent2.Listener());
		
		Assert.assertEquals(listeners.getListeners(ChildEvent1.class).size(), 3);
	}
	
	@Test
	public void testGetMultipleListenerTypes2()
	{
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent2.class, new ChildEvent2.Listener());
		listeners.put(ChildEvent2.class, new ChildEvent2.Listener());
		
		Assert.assertEquals(listeners.getListeners(ChildEvent2.class).size(), 2);
	}
	
	@Test
	public void testGetListenersWithInheritance1()
	{
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ParentEvent.class, new ParentEvent.Listener());
		listeners.put(ParentEvent.class, new ParentEvent.Listener());
		
		Assert.assertEquals(listeners.getListeners(ParentEvent.class).size(), 2);
	}
	
	@Test
	public void testGetListenersWithInheritance2()
	{
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ChildEvent1.class, new ChildEvent1.Listener());
		listeners.put(ParentEvent.class, new ParentEvent.Listener());
		listeners.put(ParentEvent.class, new ParentEvent.Listener());
		
		Assert.assertEquals(listeners.getListeners(ChildEvent1.class).size(), 3);
	}
	
	protected void addThreeListeners()
	{
		listeners.put(TestEvent.class, new TestEventListener());
		listeners.put(TestEvent.class, new TestEventListener());
		listeners.put(TestEvent.class, new TestEventListener());
	}
	
	public static class TestEventListener implements EventListener<TestEvent>
	{
		@Override
		public void handle(TestEvent event) {
		}
	}
	
	public static class ParentEvent extends TestEvent
	{
		public static class Listener implements EventListener<ParentEvent>
		{
			@Override
			public void handle(ParentEvent event) {
			}
		}
	}
	
	public static class ChildEvent1 extends ParentEvent
	{
		public static class Listener implements EventListener<ChildEvent1>
		{
			@Override
			public void handle(ChildEvent1 event) {
			}
		}
	}
	
	public static class ChildEvent2 extends ParentEvent
	{
		public static class Listener implements EventListener<ChildEvent2>
		{
			@Override
			public void handle(ChildEvent2 event) {
			}
		}
	}
}
