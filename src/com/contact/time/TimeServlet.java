package com.contact.time;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.contact.base.BaseServlet;
import com.contact.base.BaseManager;

/**
 * Servlet implementation class TimeServlet
 */
@WebServlet("/TimeServlet/*")
public class TimeServlet extends BaseServlet<CalendarEntry> {
	private static final long serialVersionUID = 1L;
	private TimeManager timeManager = TimeManager.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected Class<CalendarEntry> getTemplateClass() {
		return CalendarEntry.class;
	}

	@Override
	protected BaseManager<CalendarEntry> getManager() {
		return timeManager;
	}



}
