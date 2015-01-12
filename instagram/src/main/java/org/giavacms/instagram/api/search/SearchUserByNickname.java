package org.giavacms.instagram.api.search;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.giavacms.instagram.api.model.common.User;
import org.giavacms.instagram.api.model.result.UserSearchResult;
import org.giavacms.instagram.api.util.HttpUtils;


public class SearchUserByNickname {

	private static String url = "https://api.instagram.com/v1/users/search?q=jack&access_token=5624011.f59def8.649a92bc328d4b988884a7ce688a3d2e";
	private static String TOKEN = "5624011.6fd0d93.cc06ab2e8af94f37ace87092ec755c49";

	public static UserSearchResult execute(String accessToken, String q,
			int count, boolean debug) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("q", q);
		params.put("count", "" + count);
		String toJson = HttpUtils.getQueryWithParams(params, "https",
				"api.instagram.com/v1", "/users/search", debug);
		ObjectMapper mapper = new ObjectMapper();
		UserSearchResult result = mapper.readValue(toJson.getBytes(),
				UserSearchResult.class);
		return result;
	}

	public static void main(String[] args) throws Exception {
		UserSearchResult result = execute(TOKEN, "ale", 1000, true);
		System.out.println(result);
	}

	public static void main2(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {
		String q = "ale";
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> queryParamas = new ArrayList<NameValuePair>();
		queryParamas.add(new BasicNameValuePair("access_token", TOKEN));
		queryParamas.add(new BasicNameValuePair("q", "aleesio"));

		// 1336020887304

		URI uri = URIUtils.createURI("https", "api.instagram.com/v1/users", -1,
				"/search", URLEncodedUtils.format(queryParamas, "UTF-8"), null);
		HttpGet httpGet = new HttpGet(uri);

		HttpResponse response = httpclient.execute(httpGet);
		String toJson = EntityUtils.toString(response.getEntity());
		System.out.println(toJson);
		System.out.println(response.getProtocolVersion());
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.getStatusLine().getReasonPhrase());
		System.out.println(response.getStatusLine().toString());
		ObjectMapper mapper = new ObjectMapper();
		// MediaResult result = mapper.readValue(new File(
		// "/home/fiorenzo/Scrivania/testmedia.json"), MediaResult.class);
		UserSearchResult result = mapper.readValue(toJson.getBytes(),
				UserSearchResult.class);

		System.out.println("media num: " + result.getData().size());
		for (User user : result.getData()) {
			System.out.println("id:" + user.getId());
			System.out.println("username: " + user.getUsername());
			System.out.println("fullname: " + user.getFullName());
			System.out.println("first_name: " + user.getFirstName());
			System.out.println("last_name: " + user.getLastName());
			System.out.println("bio: " + user.getBio());
			System.out.println("picture: " + user.getProfilePicture());
			System.out.println("-------------------");
		}
	}
}
