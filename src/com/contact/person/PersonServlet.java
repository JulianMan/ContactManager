package com.contact.person;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contact.utils.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Servlet implementation class PersonServerlet
 */
@WebServlet("/PersonServlet/*")
public class PersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PersonManager personManager = PersonManager.getInstance();
	private Gson gson = new Gson();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = RequestHandler.getUserId(request);
		
		String requestedPerson = request.getPathInfo().split("/")[1];
		
		if(requestedPerson.equals("all")) {
			List<Person> people = personManager.read(userId);
			response.getWriter().append(gson.toJson(people));
		} else {
			int personId = Integer.parseInt(requestedPerson);
			Person person = personManager.read(userId, personId);
			if(person == null) {
				response.sendError(404, "Error, could not find person with the ID: " + personId);
			} else {
				response.getWriter().append(gson.toJson( person));
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = RequestHandler.getUserId(request);
		
		String requestedPerson = request.getPathInfo().split("/")[1];
		int personId;
		try {
			personId = Integer.parseInt(requestedPerson);
		} catch (Exception e) {
			response.sendError(400, "Error, must provide a valid URL in the form '/contactmanager/PersonServlet/{person_id}' for POST requests");
			return;
		}

		
		Person person;
		try {
			String json = RequestHandler.extractJson(request);
			person = Person.fromJson(json);
		} catch (JsonSyntaxException e) {
			response.sendError(400, "Error parsing supplied json.");
			return;
		}
		
		if(!personManager.update(person)) {
			response.sendError(404, "Error. Unable to update person");
		}

	}

}
