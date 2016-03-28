package com.contact.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.contact.product.AmazonProduct;
import com.contact.product.AmazonProductSearch;

public class ProductSearchTest {
	AmazonProductSearch productSearch = new AmazonProductSearch();
	List<AmazonProduct> products;
	
	public ProductSearchTest()
	{
		Properties props = new Properties();
		InputStream input = null;
		try
		{
			input = new FileInputStream("resources/AWSCredentials.properties");
			props.load(input);
			productSearch.start(props);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearch1()
	{
		products = productSearch.search("game+of+thrones");
		Assert.assertFalse(products.isEmpty());
	}
	
	@Test
	public void testSearch2()
	{
		products = productSearch.search("game of thrones");
		Assert.assertFalse(products.isEmpty());
	}
	
	@Test
	public void testSearch3()
	{
		products = productSearch.search("STEAK");
		Assert.assertFalse(products.isEmpty());
	}
	
	@Test
	public void testUrls()
	{
		products = productSearch.search("Steak");
		for(AmazonProduct prod : products)
		{
			if(!webContentExists(prod.getUrl()))
			{
				Assert.fail("Invalid URL: " + prod.getUrl());
			}
		}
		// Should pass by default, if no fail has been reached
	}
	
	@Test
	public void testImages()
	{
		products = productSearch.search("Steak");
		for(AmazonProduct prod : products)
		{
			if(!webContentExists(prod.getImageUrl()))
			{
				Assert.fail("Invalid URL: " + prod.getImageUrl());
			}
		}
		// Should pass by default, if no fail has been reached
	}
	
	/**
	 * Try to access web content
	 * @param URLName URL of web content to access, in String form
	 * @return true if HTTP_OK is returned. false if an error code is returned
	 */
	protected static boolean webContentExists(String URLName){
	    try {
	        HttpURLConnection.setFollowRedirects(true);
	        HttpURLConnection con =
	           (HttpURLConnection) new URL(URLName).openConnection();
	        con.setRequestProperty("User-Agent", "Mozilla/5.0");// Amazon returns a 503 otherwise
	        con.setInstanceFollowRedirects(true);
	        con.setRequestMethod("GET");
	        int responseCode = con.getResponseCode();
	        return responseCode == HttpURLConnection.HTTP_OK;
	      }
	      catch (Exception e) {
	         e.printStackTrace();
	         return false;
	      }
	    }
	
	@Test
	public void testSearchEmptyString()
	{
		products = productSearch.search("");
		Assert.assertTrue(products.isEmpty());
	}
	
	@Test
	public void testBadAWSCredentials()
	{
		productSearch = new AmazonProductSearch();
		Properties badCreds = new Properties();
		badCreds.setProperty("AccessKey", "ThisIsNotAKey");
		badCreds.setProperty("SecretKey", "UnfortunatelyThisTooIsNotAKey");
		badCreds.setProperty("AssociateTag", "ThisIsNotATag");
		productSearch.start(badCreds);
		// Any exceptions should be caught, and an empty list returned
		products = productSearch.search("stake");
		Assert.assertTrue(products.isEmpty());
	}
}
