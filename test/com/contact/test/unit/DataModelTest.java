package com.contact.test.unit;

import org.junit.Test;

import java.util.List;

import org.junit.Assert;

import com.contact.datalayer.DataModel;
import com.contact.person.Person;

public class DataModelTest {
	
	@Test
	public void testCreatePerson()
	{
		Person person = new Person();
		person.setName("Julian Man");
		person.setUserId(1);
		
		boolean success = DataModel.getInstance().create(person);
		Assert.assertTrue(success);
	}
	
	@Test
	public void testReadPerson()
	{
		Person person1 = new Person();
		person1.setName("Julian Man");
		person1.setUserId(1);
		
		Person person2 = new Person();
		person2.setName("John Doe");
		person2.setUserId(1);
		
		DataModel.getInstance().create(person1);
		DataModel.getInstance().create(person2);
		
		List<Person> persons = DataModel.getInstance().read(Person.class, 1);
		Assert.assertTrue(persons.contains(person2));
	}
	
	@Test
	public void testDeletePerson()
	{
		Person person1 = new Person();
		person1.setName("Julian Man");
		person1.setUserId(1);
		
		Person person2 = new Person();
		person2.setName("John Doe");
		person2.setUserId(1);
		
		DataModel.getInstance().create(person1);
		DataModel.getInstance().create(person2);
		DataModel.getInstance().delete(person1);
		
		List<Person> persons = DataModel.getInstance().read(Person.class, 1);
		Assert.assertTrue(persons.contains(person2) && !persons.contains(person1));
	}
	
//	@Test
//	public void storePersonAttributes()
//	{
//		Person person = new Person();
//		person.setFirstName("Julian");
//		person.setLastName("Man");
//		person.setUserId(0);
//		person.getAttributes().put("Intelligence", "Ridiculously high");
//		person.getAttributes().put("Modesty", "Also ridiculously high");
//		
//		DataModel.getInstance().create(person);
//		String json = DataModel.getInstance().read(ITables.PERSON_TABLE, 0);
//		List<Person> persons = Person.fromJson(json);
//		Assert.assertTrue(persons.contains(person));
//	}
}
