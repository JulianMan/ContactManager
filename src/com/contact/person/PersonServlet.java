package com.contact.person;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.contact.base.BaseServlet;
import com.contact.base.BaseManager;

/**
 * Servlet implementation class PersonServerlet
 */
@WebServlet("/PersonServlet/*")
public class PersonServlet extends BaseServlet<Person> {
	private static final long serialVersionUID = 1L;
	private PersonManager personManager = PersonManager.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonServlet() {
        super();
    }

	@Override
	protected Class<Person> getTemplateClass() {
		return Person.class;
	}

	@Override
	protected BaseManager<Person> getManager() {
		return personManager;
	}


}
