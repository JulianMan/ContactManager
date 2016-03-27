package com.contact.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.contact.product.AmazonProduct;
import com.contact.product.ProductSearch;

public class ProductSearchTest {
	ProductSearch productSearch = new ProductSearch();
	List<AmazonProduct> products;
	
	public ProductSearchTest()
	{
		productSearch.start();
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
	public void testSearchEmptyString()
	{
		products = productSearch.search("");
		Assert.assertTrue(products.isEmpty());
	}
}
