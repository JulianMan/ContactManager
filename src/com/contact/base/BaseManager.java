package com.contact.base;

import java.util.List;
import java.util.logging.Logger;

import com.contact.datalayer.DataModel;
import com.contact.event.EventBus;

public abstract class BaseManager<T> {
	protected DataModel dataModel = DataModel.getInstance();
	protected EventBus eventBus = EventBus.getInstance();
	protected Logger logger = Logger.getGlobal();
	
	public abstract boolean create(T obj);
	
	public abstract List<T> read(int userId);
	
	public abstract T read(int userId, int resourceId);
	
	public abstract boolean update(T obj);
	
	public abstract boolean delete(T obj);
}
