package com.contact.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/ProductSearch")
public class ProductSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected Gson gson = new Gson();
	protected ProductSearch productSearch = new ProductSearch();
	
	public ProductSearchServlet() {
        super();
        boolean success = productSearch.start();
        if(!success)
        {
        	System.out.println("Error loading ProductSearch");
        }
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getQueryString();
		
		List<AmazonProduct> products = productSearch.search(query);
		for(AmazonProduct prod : products)
			System.out.println(prod.toString());
		
		response.getWriter().append(gson.toJson(products));
	}
}
