package com.contact.datalayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.contact.person.Attribute;
import com.contact.person.Person;
import com.mysql.jdbc.Statement;

public class PersonTableManager extends TableManager<Person> {
	
	private static final String INSERT_PERSON_QUERY = "insert into person "
			+ "(user_id,name) values "
			+ "(?,?)";
	private static final String SELECT_PERSON_QUERY = 
			"select person.person_id as person_id, user_id, person.name as name, "
			+ "person_attributes.name as attribute_name, person_attributes.value as attribute_value, person_attributes.id as attribute_id  "
			+ "from person left join person_attributes "
			+ "on person.person_id = person_attributes.person_id "
			+ "where user_id = ?";
	private static final String SELECT_PERSON_QUERY_EXTENSION = " and person.person_id = ?";
	private static final String UPDATE_PERSON_QUERY = "update person set "
			+ "name = ? where "
			+ "user_id = ? and person_id = ?";
	private static final String DELETE_PERSON_QUERY = "DELETE person, person_attributes "
			+ "FROM person  INNER JOIN person_attributes  "
			+ "WHERE person.person_id = person_attributes.person_id and "
			+ "user_id = ? and person.person_id = ?";
	

	private static final String INSERT_ATTRIBUTE_QUERY = "insert into person_attributes "
			+ "(person_id, name, value) values(?, ?, ?)";

	private static final String UPDATE_ATTRIBUTE_QUERY = "update person_attributes "
			+ "set name = ?, value = ? "
			+ "where person_id = ? and id = ?";
	
	public PersonTableManager(Connection connection)
	{
		super(connection);
	}

	@Override
	public boolean create(Person t) {
		try
		{
			// Populate default values whenever a person is added
			if(!t.getAttributes().containsKey("Gift Ideas")) {
				t.setAttribute(new Attribute("Gift Ideas", ""));
			}
			if(!t.getAttributes().containsKey("Interests")) {
				t.setAttribute(new Attribute("Interests", ""));
			}
			if(!t.getAttributes().containsKey("Birthday")) {
				t.setAttribute(new Attribute("Birthday", ""));
			}
			if(!t.getAttributes().containsKey("Phone")) {
				t.setAttribute(new Attribute("Phone", ""));
			}
			if(!t.getAttributes().containsKey("Email")) {
				t.setAttribute(new Attribute("Email", ""));
			}
			
			PreparedStatement person_statement = connection.prepareStatement(INSERT_PERSON_QUERY, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			person_statement.setInt(idx++, t.getUserId());
			person_statement.setString(idx++, t.getName());
			
			person_statement.execute();
			
			ResultSet rs = person_statement.getGeneratedKeys();
		    rs.next();
		    int person_id = rs.getInt(1);
			
			Map<String, Attribute> attributes = t.getAttributes();
			for (String key : attributes.keySet()) {
				insertAttribute(person_id, attributes.get(key));
			}
			
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Person> read(int userId) {
		Map<Integer, Person> people = new HashMap<Integer, Person>();
		try {
			PreparedStatement stmt = connection.prepareStatement(SELECT_PERSON_QUERY);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				Person person = personFromResultSet(rs);
				
				if(people.containsKey(person.getPersonId())){
					Person updatePerson = people.get(person.getPersonId());
					Map<String, Attribute> attributes = person.getAttributes();
					
					for (String key : attributes.keySet()) {
						updatePerson.setAttribute(attributes.get(key));;
					}
				} else {
					people.put(person.getPersonId(), person);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Person>(people.values());
	}
	
	public Person read(int userId, int personId) {
		Person person = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(SELECT_PERSON_QUERY + SELECT_PERSON_QUERY_EXTENSION);
			stmt.setInt(1, userId);
			stmt.setInt(2, personId);
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			person = personFromResultSet(rs);
			
			while(rs.next())
			{
				Person tempPerson = personFromResultSet(rs);
				
				Map<String, Attribute> attributes = tempPerson.getAttributes();
					
				for (String key : attributes.keySet()) {
					person.setAttribute(attributes.get(key));;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return person;
	}

	@Override
	public boolean update(Person t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(UPDATE_PERSON_QUERY);
			int idx = 1;
			pstmt.setString(idx++,t.getName());
			pstmt.setInt(idx++, t.getUserId());
			pstmt.setInt(idx++, t.getPersonId());
			pstmt.execute();
			
			Map<String, Attribute> attributes = t.getAttributes();
			for (String key : attributes.keySet()) {
				Attribute attribute = attributes.get(key);
				if(attribute.getId() == -1) {
					insertAttribute(t.getPersonId(), attribute);
				} else {
					updateAttribute(t.getPersonId(), attribute);
				}
			}
			
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(Person t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(DELETE_PERSON_QUERY);
			int idx = 1;
			pstmt.setInt(idx++, t.getUserId());
			pstmt.setInt(idx++, t.getPersonId());
			return pstmt.execute();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean insertAttribute(int personId, Attribute attribute) throws SQLException {
		PreparedStatement attribute_statement = connection.prepareStatement(INSERT_ATTRIBUTE_QUERY);
		int idx = 1;
		attribute_statement.setInt(idx++, personId);
		attribute_statement.setString(idx++, attribute.getName());
		attribute_statement.setString(idx++, attribute.getValue());
		
		return attribute_statement.execute();
	}
	
	private boolean updateAttribute(int personId, Attribute attribute) throws SQLException {
		PreparedStatement attribute_statement = connection.prepareStatement(UPDATE_ATTRIBUTE_QUERY);
		int idx = 1;
		attribute_statement.setString(idx++, attribute.getName());
		attribute_statement.setString(idx++, attribute.getValue());
		attribute_statement.setInt(idx++, personId);
		attribute_statement.setInt(idx++, attribute.getId());
		
		return attribute_statement.execute();
	}
	
	protected Person personFromResultSet(ResultSet rs) throws SQLException
	{
		Person person = new Person();
		person.setUserId(rs.getInt("user_id"));
		person.setPersonId(rs.getInt("person_id"));
		person.setName(rs.getString("name"));
		
		String attributeName = rs.getString("attribute_name");
		if(attributeName != null) {
			person.setAttribute(new Attribute(attributeName, 
					  						  rs.getString("attribute_value"),
					  						  rs.getInt("attribute_id")));
		}

		return person;
	}
}