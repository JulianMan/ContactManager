package com.contact.test.performance;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.contact.datalayer.DataModel;
import com.contact.datalayer.PersonTableManager;
import com.contact.person.Attribute;
import com.contact.person.Person;
import com.contact.person.PersonServlet;

public class PersonPerformanceTest {
	private DataModel dm = DataModel.getInstance();
	private PersonTableManager tableManager = new PersonTableManager(dm.getConnection());
	
	private class PersonServletTester extends PersonServlet {
		private static final long serialVersionUID = 1L;
		public void publicDoGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			this.doGet(request, response);
		}
	}
	
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();
	
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
	
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test10People() {
		addPeople(10);
		getPeople();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test100People() {
		addPeople(100);
		getPeople();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test1000People() {
		addPeople(1000);
		getPeople();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test10000People() {
		addPeople(10000);
		getPeople();
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void test100000People() {
		addPeople(100000);
		getPeople();
	}
	
	private void getPeople() {
		PersonServletTester servlet = new PersonServletTester();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		request.setPathInfo("/");
		try {
			servlet.publicDoGet(request, response);
		} catch (Exception e) {
			Assert.fail("PersonServlet threw an exception while testing: " + e.getMessage());
		}
	}
	
	private void addPeople(int numPeople) {		
		Person person = new Person();
		Attribute attribute1 = new Attribute("name1","value1");
		person.setName("test_1");
		person.setUserId(1);
		person.setAttribute(attribute1);
		
		for (int i = 0; i < numPeople; i++) {
			Attribute attr = person.getAttribute("name1");
			attr.setValue("value" + i);
			tableManager.create(person);
		}
	}

}
