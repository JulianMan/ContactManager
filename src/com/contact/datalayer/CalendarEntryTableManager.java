package com.contact.datalayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.contact.time.CalendarEntry;

public class CalendarEntryTableManager extends TableManager<CalendarEntry>{
	
	private static final String CREATE_QUERY = "insert into calendar_entry "
			+ "(user_id,title,time,recurrence,notified,message) values "
			+ "(?,?,?,?,?,?)";	
	private static final String READ_QUERY = "select entry_id,user_id,"
			+ "title,time,recurrence,notified,message "
			+ "from calendar_entry where "
			+ "user_id = ?";
	private static final String READ_QUERY_EXTENSION = " and entry_id = ?";
	private static final String UPDATE_QUERY = "update calendar_entry set "
			+ "title = ?, time = ?, recurrence = ?, notified = ?, message = ? where "
			+ "user_id = ? and entry_id = ?";
	private static final String DELETE_QUERY = "delete from calendar_entry where "
			+ "user_id = ? and entry_id = ?";
	
	public CalendarEntryTableManager(Connection connection) {
		super(connection);
	}

	@Override
	public boolean create(CalendarEntry t) {
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(CREATE_QUERY);
			int idx = 1;
			pstmt.setInt(idx++, t.getUserId());
			pstmt.setString(idx++, t.getTitle());
			pstmt.setTimestamp(idx++, Timestamp.valueOf(t.getTime()));
			pstmt.setLong(idx++, t.getRecurrence());
			pstmt.setBoolean(idx++, t.isNotified());
			pstmt.setString(idx++, t.getMessage());
			pstmt.execute();
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
		List<CalendarEntry> calendarEntries = new ArrayList<>();
		try
		{
			PreparedStatement pstmt = connection.prepareStatement(READ_QUERY);
			pstmt.setInt(1, key);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				calendarEntries.add(calendarEntryFromResultSet(rs));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return calendarEntries;
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
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

}
