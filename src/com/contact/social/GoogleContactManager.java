package com.contact.social;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GoogleContactManager {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Contact Manager";
    
    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/credential_store.json");

    /** Persistent Data Store. */
    private static DataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Scopes required by this application. */
    private static final Set<String> SCOPES =
        PeopleScopes.all();

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            // TODO: Change to be database backed
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    public static AuthorizationCodeFlow authorizationCodeFlow() throws IOException {
    	// Load client secrets.
//        InputStream in =
//            PeopleQuickstart.class.getResourceAsStream("/client_secret.json");
//        GoogleClientSecrets clientSecrets =
//            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
    	return new GoogleAuthorizationCodeFlow.Builder(
    			HTTP_TRANSPORT, 
    			JSON_FACTORY,
		        "925627078368-9fm960o4pf3u85m51r3vf317oodo6rda.apps.googleusercontent.com", 
		        "C5rdBzegjjnrH-wCyBcjfJ_D",
		        SCOPES
	        ).setDataStoreFactory(DATA_STORE_FACTORY)
	    		.setAccessType("offline")
	    		.build();
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(String userId) throws IOException {
        // Build flow and trigger user authorization request.
    	AuthorizationCodeFlow flow = authorizationCodeFlow();
        Credential credential = flow.loadCredential(userId);
        System.out.println("Credentials loaded. Token: " + credential.getAccessToken());
        return credential;
    }

    /**
     * Build and return an authorized People client service.
     * @return an authorized People client service
     * @throws IOException
     */
    public static People getPeopleService(String UserId) throws IOException {
        Credential credential = authorize(UserId);
        return new People.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}