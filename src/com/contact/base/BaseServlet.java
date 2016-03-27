package com.contact.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contact.utils.RequestHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Servlet implementation class ContactManagerServlet
 */
public abstract class BaseServlet<T> extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = RequestHelper.getUserId(request);
		
		String [] splitPath = request.getPathInfo().split("/");
		
		if(splitPath.length == 0 || splitPath[1].equals("all")) {
			List<T> objs = getManager().read(userId);
			response.getWriter().append(gson.toJson(objs));
		} else {
			int resourceId = Integer.parseInt(splitPath[1]);
			T obj = getManager().read(userId, resourceId);
			if(obj == null) {
				response.sendError(404, "Error, could not find resource with the ID: " + resourceId);
			} else {
				response.getWriter().append(gson.toJson(obj));
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		T obj;
		try {
			String json = RequestHelper.extractJson(request);
			obj = gson.<T>fromJson(json, getTemplateClass());
		} catch (JsonSyntaxException e) {
			response.sendError(400, "Error parsing supplied json.");
			return;
		}
		
		String [] splitPath = request.getPathInfo().split("/");
		
		if(splitPath.length == 0) {
			boolean createSucceeded = getManager().create(obj);
			if(!createSucceeded) {
				response.sendError(404, "Error. Unable to create resource");
			}
		} else {
			boolean updateSucceeded = getManager().update(obj);
			if(!updateSucceeded) {
				response.sendError(404, "Error. Unable to update resource");
			}
		}

	}
	
	protected abstract Class<T> getTemplateClass();
	
	protected abstract BaseManager<T> getManager();

}
