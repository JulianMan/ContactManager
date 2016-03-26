package com.contact.manager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.contact.datalayer.DataModel;
import com.contact.event.EventBus;
import com.contact.person.PersonManager;
import com.contact.time.TimeManager;

/**
 * Application Lifecycle Listener implementation class StartupClass
 *
 */
@WebListener
public class StartupClass implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public StartupClass() {
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // Do Nothing
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
    	// Initialize everything so event listeners will be registered
    	EventBus.getInstance();
        DataModel.getInstance();
        PersonManager.getInstance();
        TimeManager.getInstance();
         
    }
	
}
