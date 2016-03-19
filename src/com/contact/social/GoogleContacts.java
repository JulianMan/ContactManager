package com.contact.social;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
/**
 * Servlet implementation class GoogleContacts
 */
@WebServlet("/GoogleContacts")
public class GoogleContacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleContacts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: Actual userId
        People service = GoogleContactManager.getPeopleService("1");
        
        PrintWriter responseWriter = response.getWriter();
        
        responseWriter.append(service.people().get("people/me").execute().toString());

        // Request 10 connections.
        ListConnectionsResponse listConnectionsResponse = service.people().connections()
                .list("people/me")
                .execute();

        // Print display name of connections if available.
        List<Person> connections = listConnectionsResponse.getConnections();
        if (connections != null && connections.size() > 0) {
            for (Person person : connections) {
                List<Name> names = person.getNames();
                if (names != null && names.size() > 0) {
                	responseWriter.append("Name: " + person.getNames().get(0)
                            .getDisplayName());
                } else {
                	responseWriter.append("No names available for connection.");
                }
            }
        } else {
        	responseWriter.append("No connections found.");
        }

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
