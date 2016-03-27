package com.contact.social;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.http.GenericUrl;

@WebServlet("/GoogleOAuthServerlet")
public class GoogleOAuthServerlet extends AbstractAuthorizationCodeServlet {
	private static final long serialVersionUID = 1L;

	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws IOException {
	    // do stuff
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
	      // TODO: UserIDs
		  return "1";
	  }
	}


