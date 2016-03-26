package com.contact.product;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.contact.data.AmazonProduct;

public class ProductSearch {
	
	protected static final String PROVIDER = "http://webservices.amazon.com/onca/xml?";
	protected static final String SIGNING_HEADER = "GET\n" +
												   "webservices.amazon.com\n" +
												   "/onca/xml\n";
	protected String accessKey = "";
	protected String secretKey = "";
	protected String associateTag = "";
	protected static final String HMAC_SHA256 = "HmacSHA256";
	protected static final String UTF8 = "UTF-8";
	
	public static void main(String[] args)
	{
		ProductSearch ps = new ProductSearch();
		ps.start();
		ps.test();
	}
	
	public void test()
	{
		search("chess");		
	}
	
	public void start()
	{
		Properties prop = new Properties();
		InputStream input = null;
		try
		{
			input = new FileInputStream("resources/AWSCredentials.properties");
			prop.load(input);
			accessKey = prop.getProperty("AccessKey");
			secretKey = prop.getProperty("SecretKey");
			associateTag = prop.getProperty("AssociateTag");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<AmazonProduct> search(String interest)
	{
		List<String> searchParams = getSearchParameters(interest);
		String query = PROVIDER + buildQuery(searchParams);
		System.out.println(query);
		System.out.println();
		try
		{
			String xmlResult = executeHttpRequest(query);
			System.out.println(xmlResult);
			Document doc = parseXml(xmlResult);
			NodeList items = doc.getElementsByTagName("Item");
			List<AmazonProduct> products = extractAmazonProducts(items);
			return products;
//			System.out.println(xmlResult);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	protected List<String> getSearchParameters(String interest)
	{
		List<String> searchParams = new ArrayList<>();
		searchParams.add("Service=AWSECommerceService");
		searchParams.add("Operation=ItemSearch");
		searchParams.add("AWSAccessKeyId=" + accessKey);
		searchParams.add("AssociateTag=" + associateTag);
		searchParams.add("Keywords=" + urlEncode(interest.replaceAll(" ", "+")));
		searchParams.add("Timestamp=" + urlEncode(getCurrentTime()));
		searchParams.add("SearchIndex=All");
		Collections.sort(searchParams);
		return searchParams;
	}
	
	protected String buildQuery(List<String> params)
	{
		String paramString = String.join("&", params);
		String signingRequest = SIGNING_HEADER + paramString;
		try
		{
			String signature = hmacEncode(secretKey, signingRequest);
			return paramString + "&Signature=" + urlEncode(signature);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	protected String executeHttpRequest(String request) throws Exception
	{
		URL url = new URL(request);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();
	}
	
	protected List<AmazonProduct> extractAmazonProducts(NodeList items)
	{
		List<AmazonProduct> products = new ArrayList<>();
		for(int i = 0; i < items.getLength(); i++)
		{
			Element item = (Element) items.item(i);
			if(item.getNodeName().contains("Item"))
			{
				AmazonProduct prod = new AmazonProduct();
				
				prod.setAsin(item.getElementsByTagName("ASIN").item(0).getTextContent());
				prod.setUrl(item.getElementsByTagName("DetailPageURL").item(0).getTextContent());
				Element attributes = (Element) item.getElementsByTagName("ItemAttributes").item(0);
				prod.setDescription(attributes.getElementsByTagName("Title").item(0).getTextContent());
				System.out.println(prod.toString());
			}
		}
		return products;
	}
	
	// Wraps DocumentBuilderFactory and DocumentBuilder
	protected static Document parseXml(String xml) throws IOException, ParserConfigurationException, SAXException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return dBuilder.parse(is);
	}
	
	protected static String hmacEncode(String key, String data) throws Exception 
	{
		  Mac encoder = Mac.getInstance(HMAC_SHA256);
		  SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(UTF8), HMAC_SHA256);
		  encoder.init(secretKey);
		  return DatatypeConverter.printBase64Binary(encoder.doFinal(data.getBytes(UTF8)));
	}
	
	protected static String getCurrentTime()
	{
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(tz);
		return df.format(new Date());
	}
	
	protected static String urlEncode(String str)
	{
		try{
			return URLEncoder.encode(str, "UTF-8");
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		return str;
	}
	
}
