package com.contact.data;

public class AmazonProduct {
	
	protected String asin = "";
	protected String description = "";
	protected String url = "";
	
	public AmazonProduct()
	{
		
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString()
	{
		return "{asin=" + asin + ", desc=" + description + ",url=" + url + "}";
	}
	
}
