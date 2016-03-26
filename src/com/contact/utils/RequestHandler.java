package com.contact.utils;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class RequestHandler {
	public static int getUserId(HttpServletRequest request) {
		// TODO: Implement
		return 1;
	}
	
	public static String extractJson(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    try {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append('\n');
	        }
	    } finally {
	        reader.close();
	    }
	    return sb.toString();
	}
}
