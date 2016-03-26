package com.contact.datalayer;

import java.sql.Connection;
import java.util.List;

public abstract class TableManager<T>{
	protected Connection connection;
	
	public TableManager(Connection connection)
	{
		this.connection = connection;
	}
	
	
	public abstract boolean create(T t);
	
	public abstract List<T> read(int userId);
	
	public abstract T read(int userId, int resourceId);
	
	public abstract boolean update(T t);
	
	public abstract boolean delete(T t);
}
