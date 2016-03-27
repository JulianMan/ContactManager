package com.contact.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;

public class TwillioMessager implements Messager {
	private static final String ACCOUNT_SID = "AC7d19ea7635feb869b7e9d604dbe0b387";
	private static final String AUTH_TOKEN = "9d92647c98001316d5dd653c34bb618e";
	private static Logger logger = Logger.getGlobal();
	private static TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
	private static MessageFactory messageFactory = client.getAccount().getMessageFactory();

	@Override
	public boolean message(int userId, String message) {
		logger.info("Sending text message to user " + userId + " with message: " + message);
		 
	    // Build a filter for the MessageList
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("Body", message));
	    params.add(new BasicNameValuePair("To", "+12266005438"));
	    params.add(new BasicNameValuePair("From", "+17059900308"));
	    
	    try {
			messageFactory.create(params);
			return true;
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
