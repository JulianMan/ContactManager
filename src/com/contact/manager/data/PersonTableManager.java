package com.contact.manager.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.contact.data.Person;

public class PersonTableManager extends TableManager<Person> {
	
	private static final String INSERT_QUERY = "insert into person "
			+ "(user_id,name) values "
			+ "(?,?,?)";
	private static final String SELECT_QUERY = 
			"select person_id, user_id, name from person where "
			+ "user_id = ?";
	private static final String UPDATE_QUERY = "update person set "
			+ "name = ? where "
			+ "user_id = ? and person_id = ?";
	private static final String DELETE_QUERY = "delete from person where "
			+ "user_id = ? and person_id = ?";
	
	public PersonTableManager(Connection connection)
	{
		super(connection);
	}

	@Override
	public boolean create(Person t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(INSERT_QUERY);
			int idx = 1;
			pstmt.setInt(idx++, t.getUserId());
			pstmt.setString(idx++, t.getName());
			return pstmt.execute();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Person> read(int key) {
		List<Person> people = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, key);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				people.add(personFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return people;
	}
	
	protected Person personFromResultSet(ResultSet rs) throws SQLException
	{
		Person person = new Person();
		person.setUserId(rs.getInt("user_id"));
		person.setPersonId(rs.getInt("person_id"));
		person.setName(rs.getString("name"));
		return person;
	}

	@Override
	public boolean update(Person t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(UPDATE_QUERY);
			int idx = 1;
			pstmt.setString(idx++,t.getName());
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

	@Override
	public boolean delete(Person t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(DELETE_QUERY);
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
}
