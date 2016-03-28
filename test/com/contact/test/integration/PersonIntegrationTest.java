package com.contact.test.integration;


import java.io.IOException;
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

import com.contact.datalayer.DataModel;
import com.contact.datalayer.PersonTableManager;
import com.contact.person.Attribute;
import com.contact.person.Person;
import com.contact.person.PersonServlet;
import com.google.gson.Gson;

public class PersonIntegrationTest {
	private DataModel dm = DataModel.getInstance();
	private PersonTableManager tableManager = new PersonTableManager(dm.getConnection());
	
	private class PersonServletTester extends PersonServlet {
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
		List<Person> people = tableManager.read(1);
		people.stream()
			.filter(person -> person.getName().startsWith("test_"))
			.forEach(person -> tableManager.delete(person));
	}
	
	@After
	public void cleanUpDatabse() {
		List<Person> people = tableManager.read(1);
		people.stream()
			.filter(person -> person.getName().startsWith("test_"))
			.forEach(person -> tableManager.delete(person));
	}
	
	@Test
	public void testPersonCreate() {
		PersonServletTester servlet = new PersonServletTester();
		Gson gson = new Gson();
		
		Person p1 = new Person();
		Attribute attribute = new Attribute("name1","value1");
		p1.setName("test_1");
		p1.setUserId(1);
		p1.setAttribute(attribute);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		request.setContent(gson.toJson(p1).getBytes());
		request.setPathInfo("/");
		
		try {
			servlet.publicDoPost(request, response);
		} catch (Exception e) {
			Assert.fail("PersonServlet threw an exception while testing: " + e.getMessage());
		}
		
		List<Person> retrievedpeople = tableManager.read(1)
												   .stream()
												   .filter(person -> person.getName().equals("test_1"))
												   .collect(Collectors.toList());
		
		Assert.assertTrue(retrievedpeople.size() == 1);
		
		Person retrievedPerson = retrievedpeople.get(0);
		
		Assert.assertTrue(retrievedPerson.getName().equals("test_1"));
		Assert.assertTrue(retrievedPerson.getUserId() == 1);
		Assert.assertTrue(retrievedPerson.getAttribute("name1").getName().equals(attribute.getName()));
		Assert.assertTrue(retrievedPerson.getAttribute("name1").getValue().equals(attribute.getValue()));
	}
	
	@Test
	public void testPersonRead() {
		PersonServletTester servlet = new PersonServletTester();
		Gson gson = new Gson();
		
		Person p1 = new Person();
		Attribute attribute1 = new Attribute("name1","value1");
		p1.setName("test_1");
		p1.setUserId(1);
		p1.setAttribute(attribute1);
		
		tableManager.create(p1);
		
		List<Person> retrievedpeople = tableManager.read(1)
				   .stream()
				   .filter(person -> person.getName().equals("test_1"))
				   .collect(Collectors.toList());

		Assert.assertTrue(retrievedpeople.size() == 1);

		Person retrievedPerson = retrievedpeople.get(0);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		request.setPathInfo("/" + retrievedPerson.getPersonId());

		Person responsePerson = null;
		try {
			servlet.publicDoGet(request, response);
			responsePerson = gson.fromJson(response.getContentAsString(), Person.class);
		} catch (Exception e) {
			Assert.fail("PersonServlet threw an exception while testing: " + e.getMessage());
		}
		
		Assert.assertTrue(responsePerson.getName().equals("test_1"));
		Assert.assertTrue(responsePerson.getUserId() == 1);
		Assert.assertTrue(responsePerson.getPersonId() == retrievedPerson.getPersonId());
		Assert.assertTrue(responsePerson.getAttribute("name1").getName().equals(attribute1.getName()));
		Assert.assertTrue(responsePerson.getAttribute("name1").getValue().equals(attribute1.getValue()));
	}
	
	@Test
	public void testPersonUpdate() {
		PersonServletTester servlet = new PersonServletTester();
		Gson gson = new Gson();
		
		Person p1 = new Person();
		Attribute attribute1 = new Attribute("name1","value1");
		p1.setName("test_1");
		p1.setUserId(1);
		p1.setAttribute(attribute1);
		
		tableManager.create(p1);
		
		List<Person> retrievedpeople = tableManager.read(1)
				   .stream()
				   .filter(person -> person.getName().equals("test_1"))
				   .collect(Collectors.toList());

		Assert.assertTrue(retrievedpeople.size() == 1);

		Person updatedPerson = retrievedpeople.get(0);
		Attribute attribute2 = new Attribute("name2","value2");
		attribute1 = updatedPerson.getAttribute("name1");
		attribute1.setValue("newvalue1");
		updatedPerson.setAttribute(attribute2);
		updatedPerson.setAttribute(attribute1);
		
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		request.setContent(gson.toJson(updatedPerson).getBytes());
		request.setPathInfo("/" + updatedPerson.getPersonId());
		
		try {
			servlet.publicDoPost(request, response);
		} catch (Exception e) {
			Assert.fail("PersonServlet threw an exception while testing: " + e.getMessage());
		}
		
		Person retrievedPerson = tableManager.read(1, updatedPerson.getPersonId());
		
		Assert.assertTrue(retrievedPerson.getName().equals("test_1"));
		Assert.assertTrue(retrievedPerson.getUserId() == 1);
		Assert.assertTrue(retrievedPerson.getPersonId() == updatedPerson.getPersonId());
		Assert.assertTrue(retrievedPerson.getAttribute("name1").getName().equals(attribute1.getName()));
		Assert.assertTrue(retrievedPerson.getAttribute("name1").getValue().equals(attribute1.getValue()));
		Assert.assertTrue(retrievedPerson.getAttribute("name2").getName().equals(attribute2.getName()));
		Assert.assertTrue(retrievedPerson.getAttribute("name2").getValue().equals(attribute2.getValue()));
	}

}