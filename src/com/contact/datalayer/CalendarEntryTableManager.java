package com.contact.datalayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.contact.person.Attribute;
import com.contact.person.Person;
import com.contact.time.CalendarEntry;
import com.mysql.jdbc.Statement;

public class CalendarEntryTableManager extends TableManager<CalendarEntry>{
	
	private static final String CREATE_QUERY = "insert into calendar_entry "
			+ "(user_id,title,time,recurrence,notified,message) values "
			+ "(?,?,?,?,?,?)";
	private static final String CREATE_RELATED_PEOPLE_QUERY = "insert into calendar_entry_people "
			+ "(entry_id,person_id) values "
			+ "(?,?)";	
	private static final String READ_QUERY = "select calendar_entry.entry_id as entry_id, "
			+ "person_id, user_id, title, time, recurrence, notified, message "
			+ "from calendar_entry "
			+ "left join calendar_entry_people on calendar_entry_people.entry_id = calendar_entry.entry_id "
			+ "where user_id = ?";
	private static final String READ_QUERY_EXTENSION = " and calendar_entry.entry_id = ?";
	private static final String UPDATE_QUERY = "update calendar_entry set "
			+ "title = ?, time = ?, recurrence = ?, notified = ?, message = ? where "
			+ "user_id = ? and entry_id = ?";
	private static final String DELETE_QUERY = "delete from calendar_entry where "
			+ "user_id = ? and entry_id = ?";
	private static final String DELETE_RELATED_PEOPLE_QUERY = "delete from calendar_entry_people where "
			+ "entry_id = ?";
	
	public CalendarEntryTableManager(Connection connection) {
		super(connection);
	}

	@Override
	public boolean create(CalendarEntry t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
			int idx = 1;
			pstmt.setInt(idx++, t.getUserId());
			pstmt.setString(idx++, t.getTitle());
			pstmt.setTimestamp(idx++, Timestamp.valueOf(t.getTime()));
			pstmt.setLong(idx++, t.getRecurrence());
			pstmt.setBoolean(idx++, t.isNotified());
			pstmt.setString(idx++, t.getMessage());
			
			pstmt.execute();
			
			ResultSet rs = pstmt.getGeneratedKeys();
		    rs.next();
		    int entryId = rs.getInt(1);
		    
			for (Integer personId : t.getRelatedPeople()) {
				pstmt = connection.prepareStatement(CREATE_RELATED_PEOPLE_QUERY);
				idx = 1;
				pstmt.setInt(idx++, entryId);
				pstmt.setInt(idx++, personId);
				pstmt.execute();
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
	public List<CalendarEntry> read(int key) {
		Map<Integer, CalendarEntry> entries = new HashMap<Integer, CalendarEntry>();

		try
		{
			PreparedStatement pstmt = connection.prepareStatement(READ_QUERY);
			pstmt.setInt(1, key);

			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				CalendarEntry entry = calendarEntryFromResultSet(rs);
				
				if(entries.containsKey(entry.getEntryId())){
					CalendarEntry updateEntry = entries.get(entry.getEntryId());
					List<Integer> people = entry.getRelatedPeople();
					updateEntry.getRelatedPeople().addAll(people);

				} else {
					entries.put(entry.getEntryId(), entry);
				}
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<CalendarEntry>(entries.values());
	}
	
	@Override
	public CalendarEntry read(int userId, int resourceId) {
		CalendarEntry calendarEntry = null;
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(READ_QUERY + READ_QUERY_EXTENSION);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, resourceId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			calendarEntry = calendarEntryFromResultSet(rs);
			while(rs.next())
			{
				CalendarEntry nextEntry = calendarEntryFromResultSet(rs);
				List<Integer> nextPeople = nextEntry.getRelatedPeople();
				calendarEntry.getRelatedPeople().addAll(nextPeople);
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return calendarEntry;
	}
	
	protected CalendarEntry calendarEntryFromResultSet(ResultSet rs) throws SQLException
	{
		CalendarEntry calendarEntry = new CalendarEntry();
		calendarEntry.setEntryId(rs.getInt("entry_id"));
		calendarEntry.setUserId(rs.getInt("user_id"));
		calendarEntry.setTitle(rs.getString("title"));
		calendarEntry.setTime(rs.getTimestamp("time").toLocalDateTime());
		calendarEntry.setRecurrence(rs.getLong("recurrence"));
		calendarEntry.setNotified(rs.getBoolean("notified"));
		calendarEntry.setMessage(rs.getString("message"));
		int personId = rs.getInt("person_id");
		if(personId != 0) {
			calendarEntry.setRelatedPeople(new ArrayList<Integer>(Arrays.asList(personId)));
		}
		return calendarEntry;
	}

	@Override
	public boolean update(CalendarEntry t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(UPDATE_QUERY);
			int idx = 1;
			pstmt.setString(idx++,t.getTitle());
			pstmt.setTimestamp(idx++, Timestamp.valueOf(t.getTime()));
			pstmt.setLong(idx++, t.getRecurrence());
			pstmt.setBoolean(idx++,	t.isNotified());
			pstmt.setString(idx++, t.getMessage());
			pstmt.setInt(idx++, t.getUserId());
			pstmt.setInt(idx++, t.getEntryId());
			pstmt.execute();
			
			deleteRelatedPeople(t.getEntryId());
			for (Integer personId : t.getRelatedPeople()) {
				createRelatedPerson(t.getEntryId(), personId);
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
	public boolean delete(CalendarEntry t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(DELETE_QUERY);
			int idx = 1;
			pstmt.setInt(idx++, t.getUserId());
			pstmt.setInt(idx++, t.getEntryId());
			pstmt.execute();
			
			deleteRelatedPeople(t.getEntryId());
			
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private void createRelatedPerson(int entryId, int personId) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(CREATE_RELATED_PEOPLE_QUERY);
		int idx = 1;
		pstmt.setInt(idx++, entryId);
		pstmt.setInt(idx++, personId);
		pstmt.execute();
	}
	
	private void deleteRelatedPeople(int entryId) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement(DELETE_RELATED_PEOPLE_QUERY);
		int idx = 1;
		pstmt.setInt(idx++, entryId);
		pstmt.execute();
	}

}
