package com.contact.person;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contact.data.Person;
import com.contact.utils.RequestHandler;
import com.google.gson.Gson;

/**
 * Servlet implementation class PersonServerlet
 */
@WebServlet("/PersonServerlet")
public class PersonServerlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PersonManager personManager = PersonManager.getInstance();
	private Gson gson = new Gson();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonServerlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = RequestHandler.getUserId(request);
		List<Person> people = personManager.read(userId);
		response.getWriter().append(gson.toJson(people));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: Implement
	}

}
