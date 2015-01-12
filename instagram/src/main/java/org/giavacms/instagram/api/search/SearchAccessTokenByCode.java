package org.giavacms.instagram.api.search;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.giavacms.instagram.api.model.result.AccessTokenResult;
import org.giavacms.instagram.api.util.HttpUtils;


public class SearchAccessTokenByCode {

	// per ottenere il CODE, invoca:
	private static final String urlInitial = "https://instagram.com/oauth/authorize/?client_id=6fd0d93b10a84c168ed99c495adba692&redirect_uri=http://www.withflower.in/p/instagram&response_type=code";

	private static final String CLIENT_ID = "6fd0d93b10a84c168ed99c495adba692";
	private static final String CLIENT_SECRET = "e570b391a60b45f5aadefcdcf853f1e4";
	private static final String GRANT_TYPE = "authorization_code";
	private static final String REDIRECT_URI = "http://www.withflower.in/instagram";
	private static final String CODE = "1e8e034060114443b12a3ce61c2142d8";
	private static final String uri = "https://api.instagram.com/oauth/access_token";

	public static AccessTokenResult execute(String clientId,
			String clientSecret, String grantType, String redirectUri,
			String code, boolean debug) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		params.put("grant_type", grantType);
		params.put("redirect_uri", redirectUri);
		params.put("code", code);
		String toJson = HttpUtils.postQueryWithParams(params, uri, debug);
		ObjectMapper mapper = new ObjectMapper();
		AccessTokenResult result = mapper.readValue(toJson.getBytes(),
				AccessTokenResult.class);
		return result;
	}

	public static void main(String[] args) throws Exception {
		AccessTokenResult result = execute(CLIENT_ID, CLIENT_SECRET,
				GRANT_TYPE, REDIRECT_URI, CODE, true);
		System.out.println(result);
	}

	public static void main2(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("client_id", CLIENT_ID));
		postParameters.add(new BasicNameValuePair("client_secret",
				CLIENT_SECRET));
		postParameters.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
		postParameters
				.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
		postParameters.add(new BasicNameValuePair("code", CODE));

		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpResponse response = httpclient.execute(httpPost);
		String toJson = EntityUtils.toString(response.getEntity());
		System.out.println(toJson);
		System.out.println(response.getProtocolVersion());
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.getStatusLine().getReasonPhrase());
		System.out.println(response.getStatusLine().toString());
		ObjectMapper mapper = new ObjectMapper();
		AccessTokenResult result = mapper.readValue(toJson.getBytes(),
				AccessTokenResult.class);
		System.out.println(result.getAccessToken());
		System.out.println(result.getUser().getUsername());
	}
}
