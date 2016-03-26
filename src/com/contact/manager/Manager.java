package com.contact.manager;

import java.util.logging.Logger;

import com.contact.datalayer.DataModel;
import com.contact.event.EventBus;

public class Manager {
	protected DataModel dataModel = DataModel.getInstance();
	protected EventBus eventBus = EventBus.getInstance();
	protected Logger logger = Logger.getGlobal();
}
