package com.contact.social;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contact.event.EventBus;
import com.contact.event.FriendAddedEvent;
import com.contact.person.PersonManager;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;

@WebServlet("/GoogleOAuthServerletCallback")
public class GoogleOAuthServerletCallback extends AbstractAuthorizationCodeCallbackServlet {
	
	protected Logger logger = Logger.getGlobal();

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
			throws ServletException, IOException {
		System.out.println("Token: " + credential.getAccessToken());
		
		// TODO: Where should we redirect?
		resp.sendRedirect("/contactmanager/");
		
		String userId = getUserId(req);
				
        People service = GoogleContactManager.getPeopleService(userId);
                
        // Request max number of connections.
        ListConnectionsResponse listConnectionsResponse = service.people().connections()
                .list("people/me")
                .setPageSize(500)
                .execute();

        // Print display name of connections if available.
        List<Person> googlePeople = listConnectionsResponse.getConnections();
        if (googlePeople != null && googlePeople.size() > 0) {
            for (Person googlePerson : googlePeople) {
            	com.contact.data.Person contactManagerPerson = new com.contact.data.Person();
            	
            	List<Name> names = googlePerson.getNames();
                if (names != null && names.size() > 0) {
                	String googleContactName = googlePerson.getNames().get(0).getDisplayName();
                	logger.info("Found google contact: " + googleContactName);
                	contactManagerPerson.setName(googleContactName);
                	
                    for(String key : googlePerson.keySet()) {
                    	// TODO: Will this toString() burn us?
                    	contactManagerPerson.setAttribute(key, googlePerson.get(key).toString());
                    }
                    
                    FriendAddedEvent event = new FriendAddedEvent(contactManagerPerson);
                    EventBus.getInstance().broadcastEvent(FriendAddedEvent.class, event);
                    
                } else {
                    logger.info("No names available for google contact: " + googlePerson.toPrettyString());
                }
            }
        } else {
        	logger.warning("No connections found.");
        }
	}

	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
			throws ServletException, IOException {
		throw new IOException(errorResponse.toString());
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		url.setRawPath("/contactmanager/GoogleOAuthServerletCallback");
		return url.build();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		return GoogleContactManager.authorizationCodeFlow();
	}

	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO: Acutal UserID
		return "1";
	}
}