package com.contact.product;

public class AmazonProduct extends Product {
	
	protected String asin = "";
	
	public AmazonProduct()
	{
		
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}
	
	public String toString()
	{
		return "{asin=" + asin + ", name=" + name + ",url=" + url + "}";
	}
	
}
