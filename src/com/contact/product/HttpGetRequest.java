package com.contact.product;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest {
	
	protected int responseCode = -1;
	protected String response = null;
	
	public HttpGetRequest()
	{
	}
	
	public String execute(String requestUrl) throws Exception
	{
		URL url = new URL(requestUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		response = responseBuffer.toString();
		return response;
	}
	
	public int getResponseCode()
	{
		return responseCode;
	}
	
	public String getResponse()
	{
		return response;
	}
}
