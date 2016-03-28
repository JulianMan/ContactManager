package com.contact.time;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contact.base.BaseServlet;
import com.contact.utils.RequestHelper;
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
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = RequestHelper.getUserId(request);
		
		String [] splitPath = request.getPathInfo().split("/");
		
		if(splitPath.length > 2 && splitPath[2].equals("person")) {
			int personId = Integer.parseInt(splitPath[3]);
			List<CalendarEntry> objs = getManager().read(userId);
			objs = objs.stream()
					.filter(entry -> entry.getRelatedPeople().contains(personId))
					.collect(Collectors.toList());
			response.getWriter().append(gson.toJson(objs));
		} else {
			super.doGet(request, response);
		}
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
