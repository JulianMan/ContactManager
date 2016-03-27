package com.contact.product;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	protected AmazonProductSearch productSearch = new AmazonProductSearch();
	
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
		Map<String, String> queryPairs = splitQuery(query);
		
		String search = queryPairs.get("search");
		List<Product> products = new ArrayList<>();
		if("all".equals(queryPairs.get("store")) 
			|| "amazon".equals(queryPairs.get("store")))
		{
			products.addAll(productSearch.search(search));
		}
		else
		{
			// Add more searches when additional platforms are supported
		}
		
		response.getWriter().append(gson.toJson(products));
	}
	
	protected static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
	    Map<String, String> queryPairs = new HashMap<String, String>();
	    if(query != null)
	    {
		    String[] pairs = query.split("&");
		    for (String pair : pairs) {
		        int idx = pair.indexOf("=");
		        queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		    }
	    }
	    return queryPairs;
	}
}
