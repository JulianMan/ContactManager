package com.contact.test.integration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.contact.datalayer.CalendarEntryTableManager;
import com.contact.datalayer.DataModel;
import com.contact.time.CalendarEntry;
import com.contact.time.TimeServlet;
import com.google.gson.Gson;

public class TimeIntegrationTest {
	private DataModel dm = DataModel.getInstance();
	private CalendarEntryTableManager tableManager = new CalendarEntryTableManager(dm.getConnection());
	
	private class TimeServletTester extends TimeServlet {
		private static final long serialVersionUID = 1L;
		public void publicDoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			this.doGet(request, response);
		}
		public void publicDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			this.doPost(request, response);
		}
	}
	
	@Before
	public void ensureFreshDatabse() {
		List<CalendarEntry> entries = tableManager.read(1);
		entries.stream()
			.filter(CalendarEntry -> CalendarEntry.getTitle().startsWith("test_"))
			.forEach(CalendarEntry -> tableManager.delete(CalendarEntry));
	}
	
	@After
	public void cleanUpDatabse() {
		List<CalendarEntry> entries = tableManager.read(1);
		entries.stream()
			.filter(CalendarEntry -> CalendarEntry.getTitle().startsWith("test_"))
			.forEach(CalendarEntry -> tableManager.delete(CalendarEntry));
	}
	
	@Test
	public void testCalendarEntryCreate() {
		TimeServletTester servlet = new TimeServletTester();
		Gson gson = new Gson();
		
		CalendarEntry e1 = new CalendarEntry();
		LocalDateTime now = LocalDateTime.now();
		now= now.withNano(0);
		e1.setTitle("test_1");
		e1.setUserId(1);
		e1.setMessage("test_message");
		e1.setNotified(true);
		e1.setRecurrence((long)10000);
		e1.setRelatedPeople(new ArrayList<Integer>(Arrays.asList(1)));
		e1.setTime(now);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		request.setContent(gson.toJson(e1).getBytes());
		request.setPathInfo("/");
		
		try {
			servlet.publicDoPost(request, response);
		} catch (Exception e) {
			Assert.fail("CalendarEntryServlet threw an exception while testing: " + e.getMessage());
		}
		
		List<CalendarEntry> retrievedEntries = tableManager.read(1)
												   .stream()
												   .filter(CalendarEntry -> CalendarEntry.getTitle().equals("test_1"))
												   .collect(Collectors.toList());
		
		Assert.assertTrue(retrievedEntries.size() == 1);
		
		CalendarEntry retrievedCalendarEntry = retrievedEntries.get(0);
		
		Assert.assertEquals(retrievedCalendarEntry.getTitle(), "test_1");
		Assert.assertEquals(retrievedCalendarEntry.getUserId(),  1);
		Assert.assertEquals(retrievedCalendarEntry.getMessage(), "test_message");
		Assert.assertEquals(retrievedCalendarEntry.isNotified(), true);
		Assert.assertEquals(retrievedCalendarEntry.getRecurrence(), new Long(10000));
		Assert.assertEquals(retrievedCalendarEntry.getRelatedPeople().size(),  1);
		Assert.assertEquals(retrievedCalendarEntry.getRelatedPeople().get(0),  new Integer(1));
		Assert.assertEquals(retrievedCalendarEntry.getTime(), now);
	}
	
	@Test
	public void testCalendarEntryRead() {
		TimeServletTester servlet = new TimeServletTester();
		Gson gson = new Gson();
		
		CalendarEntry e1 = new CalendarEntry();
		LocalDateTime now = LocalDateTime.now();
		e1.setTitle("test_1");
		e1.setUserId(1);
		e1.setMessage("test_message");
		e1.setNotified(true);
		e1.setRecurrence((long)10000);
		e1.setRelatedPeople(new ArrayList<Integer>(Arrays.asList(1)));
		e1.setTime(now);
		
		tableManager.create(e1);
		
		List<CalendarEntry> retrievedentries = tableManager.read(1)
				   .stream()
				   .filter(CalendarEntry -> CalendarEntry.getTitle().equals("test_1"))
				   .collect(Collectors.toList());

		Assert.assertTrue(retrievedentries.size() == 1);

		CalendarEntry retrievedCalendarEntry = retrievedentries.get(0);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		request.setPathInfo("/" + retrievedCalendarEntry.getEntryId());

		CalendarEntry responseCalendarEntry = null;
		try {
			servlet.publicDoGet(request, response);
			responseCalendarEntry = gson.fromJson(response.getContentAsString(), CalendarEntry.class);
		} catch (Exception e) {
			Assert.fail("CalendarEntryServlet threw an exception while testing: " + e.getMessage());
		}
		
		Assert.assertEquals(responseCalendarEntry, retrievedCalendarEntry);
	}
	
	@Test
	public void testCalendarEntryUpdate() {
		TimeServletTester servlet = new TimeServletTester();
		Gson gson = new Gson();
		
		CalendarEntry e1 = new CalendarEntry();
		LocalDateTime now = LocalDateTime.now();
		e1.setTitle("test_1");
		e1.setUserId(1);
		e1.setMessage("test_message");
		e1.setNotified(true);
		e1.setRecurrence((long)10000);
		e1.setRelatedPeople(new ArrayList<Integer>(Arrays.asList(1)));
		e1.setTime(now);
		
		tableManager.create(e1);
		
		List<CalendarEntry> retrievedentries = tableManager.read(1)
				   .stream()
				   .filter(CalendarEntry -> CalendarEntry.getTitle().equals("test_1"))
				   .collect(Collectors.toList());

		Assert.assertTrue(retrievedentries.size() == 1);

		CalendarEntry updatedCalendarEntry = retrievedentries.get(0);
		updatedCalendarEntry.setTitle("test_2");
		updatedCalendarEntry.setMessage("test_message2");
		updatedCalendarEntry.setNotified(false);
		updatedCalendarEntry.setRecurrence((long)20000);
		updatedCalendarEntry.setRelatedPeople(new ArrayList<Integer>());
		updatedCalendarEntry.setTime(now.minusDays(1));
		
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		request.setContent(gson.toJson(updatedCalendarEntry).getBytes());
		request.setPathInfo("/" + updatedCalendarEntry.getEntryId());
		
		try {
			servlet.publicDoPost(request, response);
		} catch (Exception e) {
			Assert.fail("CalendarEntryServlet threw an exception while testing: " + e.getMessage());
		}
		
		CalendarEntry retrievedCalendarEntry = tableManager.read(1, updatedCalendarEntry.getEntryId());
		
		Assert.assertEquals(retrievedCalendarEntry, retrievedCalendarEntry);
	}
}
