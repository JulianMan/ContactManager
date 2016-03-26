package com.contact.social;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contact.event.EventBus;
import com.contact.event.FriendAddedEvent;
import com.contact.person.Attribute;
import com.contact.utils.RequestHelper;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.Address;
import com.google.api.services.people.v1.model.Birthday;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.Photo;

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
                .setRequestMaskIncludeField("person.addresses,person.age_range,person.biographies,person.birthdays,person.bragging_rights,person.cover_photos,person.email_addresses,person.events,person.genders,person.im_clients,person.interests,person.locales,person.memberships,person.metadata,person.names,person.nicknames,person.occupations,person.organizations,person.phone_numbers,person.photos,person.relations,person.relationship_interests,person.relationship_statuses,person.residences,person.skills,person.taglines,person.urls")
                .execute();

        // Print display name of connections if available.
        List<Person> googlePeople = listConnectionsResponse.getConnections();
        if (googlePeople != null && googlePeople.size() > 0) {
            for (Person googlePerson : googlePeople) {
            	com.contact.person.Person contactManagerPerson = new com.contact.person.Person();
            	
            	List<Name> names = googlePerson.getNames();
                if (names != null && names.size() > 0) {
                	String googleContactName = googlePerson.getNames().get(0).getDisplayName();
                	logger.info("Found google contact: " + googleContactName);
                	contactManagerPerson.setName(googleContactName);
                	contactManagerPerson.setUserId(RequestHelper.getUserId(req));
                	
                	List<Photo> photos = googlePerson.getPhotos();
                	if(photos != null) {
                		Attribute attribute = new Attribute("_photo", photos.get(0).getUrl());
                		contactManagerPerson.setAttribute(attribute);
                	}
                	
                	List<EmailAddress> emails = googlePerson.getEmailAddresses();
                	if(emails != null) {
                		Attribute attribute = new Attribute("Email", emails.get(0).getValue());
                		contactManagerPerson.setAttribute(attribute);
                	}
                	
                	List<Birthday> birthdays = googlePerson.getBirthdays();
                	if(birthdays != null) {
                		Attribute attribute = new Attribute("Birthday", birthdays.get(0).getText());
                		contactManagerPerson.setAttribute(attribute);
                	}
                	
                	List<PhoneNumber> phoneNumbers = googlePerson.getPhoneNumbers();
                	if(phoneNumbers != null) {
                		Attribute attribute = new Attribute("Phone", phoneNumbers.get(0).getCanonicalForm());
                		contactManagerPerson.setAttribute(attribute);
                	}
                	
                	List<Address> addresses = googlePerson.getAddresses();
                	if(addresses != null) {
                		Attribute attribute = new Attribute("Address", addresses.get(0).getFormattedValue());
                		contactManagerPerson.setAttribute(attribute);
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