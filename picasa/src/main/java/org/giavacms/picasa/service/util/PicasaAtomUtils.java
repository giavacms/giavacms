/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.giavacms.picasa.service.util;

import java.io.IOException;
import java.util.Scanner;

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetTemporaryToken;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.xml.atom.AtomParser;
import com.google.api.data.sample.picasa.model.util.Util;

/**
 * @author Yaniv Inbar
 */
public class PicasaAtomUtils {

	static OAuthHmacSigner signer;
	static OAuthCredentialsResponse credentials;

	public static HttpTransport setUpTransport(AuthType authType,
			String username, String password) throws IOException {
		HttpTransport transport = GoogleTransport.create();
		GoogleHeaders headers = (GoogleHeaders) transport.defaultHeaders;
		headers.setApplicationName("google-picasaatomsample-1.0");
		headers.gdataVersion = "2";
		AtomParser parser = new AtomParser();
		parser.namespaceDictionary = Util.NAMESPACE_DICTIONARY;
		transport.addParser(parser);
		if (authType == AuthType.OAUTH) {
			authorizeUsingOAuth(transport);
		} else {
			authorizeUsingClientLogin(transport, username, password);
		}
		return transport;
	}

	private static OAuthParameters createOAuthParameters() {
		OAuthParameters authorizer = new OAuthParameters();
		authorizer.consumerKey = "anonymous";
		authorizer.signer = signer;
		authorizer.token = credentials.token;
		return authorizer;
	}

	private static void authorizeUsingOAuth(HttpTransport transport)
			throws IOException {
		GoogleOAuthGetTemporaryToken temporaryToken = new GoogleOAuthGetTemporaryToken();
		signer = new OAuthHmacSigner();
		signer.clientSharedSecret = "anonymous";
		temporaryToken.signer = signer;
		temporaryToken.consumerKey = "anonymous";
		temporaryToken.scope = "http://picasaweb.google.com/data";
		temporaryToken.displayName = "Picasa Atom XML Sample for the GData Java library";
		OAuthCredentialsResponse tempCredentials = temporaryToken.execute();
		signer.tokenSharedSecret = tempCredentials.tokenSecret;
		System.out
				.println("Please go open this web page in a browser to authorize:");
		GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl = new GoogleOAuthAuthorizeTemporaryTokenUrl();
		authorizeUrl.temporaryToken = tempCredentials.token;
		System.out.println(authorizeUrl.build());
		System.out.println();
		System.out.println("Press enter to continue...");
		new Scanner(System.in).nextLine();
		GoogleOAuthGetAccessToken accessToken = new GoogleOAuthGetAccessToken();
		accessToken.temporaryToken = tempCredentials.token;
		accessToken.signer = signer;
		accessToken.consumerKey = "anonymous";
		accessToken.verifier = "";
		credentials = accessToken.execute();
		signer.tokenSharedSecret = credentials.tokenSecret;
		createOAuthParameters().signRequestsUsingAuthorizationHeader(transport);
	}

	private static void authorizeUsingClientLogin(HttpTransport transport,
			String username, String password) throws IOException {
		ClientLogin authenticator = new ClientLogin();
		authenticator.authTokenType = "lh2";
		authenticator.username = username;
		authenticator.password = password;
		authenticator.authenticate().setAuthorizationHeader(transport);
	}
}
