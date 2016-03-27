package com.contact.product;

<<<<<<< HEAD
import java.util.List;
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
>>>>>>> master

public interface ProductSearch<P extends Product> {
	
	
	public boolean start();
	
	public List<P> search(String query);
}
