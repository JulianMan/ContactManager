package com.contact.manager;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.people.v1.PeopleScopes;

@WebServlet("/GoogleOAuthServerletCallback")
public class GoogleOAuthServerletCallback extends AbstractAuthorizationCodeCallbackServlet {

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
			throws ServletException, IOException {
		resp.sendRedirect("/contactmanager");
	}

	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
			throws ServletException, IOException {
		// handle error
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		url.setRawPath("/contactmanager/GoogleOAuthServerletCallback");
		return url.build();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		// TODO: Use a persistent data store
		MemoryDataStoreFactory memoryDataStoreFactory = MemoryDataStoreFactory.getDefaultInstance();

		return new GoogleAuthorizationCodeFlow.Builder(
				new NetHttpTransport(), 
				JacksonFactory.getDefaultInstance(),
				"925627078368-9fm960o4pf3u85m51r3vf317oodo6rda.apps.googleusercontent.com", 
				"C5rdBzegjjnrH-wCyBcjfJ_D",
				Collections.singleton(PeopleScopes.CONTACTS_READONLY)
				).setDataStoreFactory(memoryDataStoreFactory)
				.setAccessType("offline").build();
	}

	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO: Acutal UserID
		return "1";
	}
}