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
import org.giavacms.instagram.api.model.common.Media;
import org.giavacms.instagram.api.model.result.PaginationResult;
import org.giavacms.instagram.api.util.HttpUtils;


public class SearchUserMediaById {

	private static String url = "https://api.instagram.com/v1/users/3/media/recent/?access_token=5624011.f59def8.649a92bc328d4b988884a7ce688a3d2e";
	private static String TOKEN = "5624011.6fd0d93.cc06ab2e8af94f37ace87092ec755c49";

	public static PaginationResult execute(String accessToken, String max_id,
			String userId, boolean debug) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		if (max_id != null && !max_id.trim().isEmpty())
			params.put("max_id", max_id);
		String toJson = HttpUtils.getQueryWithParams(params, "https",
				"api.instagram.com/v1", "/users/" + userId + "/media/recent",
				debug);
		ObjectMapper mapper = new ObjectMapper();
		PaginationResult result = mapper.readValue(toJson.getBytes(),
				PaginationResult.class);
		return result;
	}

	public static void main(String[] args) throws Exception {
		PaginationResult result = execute(TOKEN, "", "5624011", true);
		System.out.println(result);
		System.out.println(result.getPagination().getNextMaxId());
	}

	public static void main2(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {
		String q = "ale";
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> queryParamas = new ArrayList<NameValuePair>();
		queryParamas.add(new BasicNameValuePair("access_token", TOKEN));
		// queryParamas.add(new BasicNameValuePair("max_tag_id",
		// "1335992121537"));

		// 1336020887304

		URI uri = URIUtils.createURI("https",
				"api.instagram.com/v1/users/981416", -1, "/media/recent",
				URLEncodedUtils.format(queryParamas, "UTF-8"), null);
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
		PaginationResult result = mapper.readValue(toJson.getBytes(),
				PaginationResult.class);
		System.out
				.println("next min: " + result.getPagination().getNextMinId());
		System.out
				.println("next max: " + result.getPagination().getNextMaxId());
		System.out.println("next max tag: "
				+ result.getPagination().getNextMaxTagId());
		System.out.println("deprecation: "
				+ result.getPagination().getDeprecationWarning());
		System.out.println("next url: " + result.getPagination().getNextUrl());
		System.out.println("media num: " + result.getData().size());
		for (Media media : result.getData()) {
			System.out.println("id:" + media.getId());
			System.out.println("NOME: " + media.getUser().getFullName());
			System.out.println(media.getImages().getLowResolution().getUrl());
			System.out.println(media.getImages().getStandardResolution()
					.getUrl());
			System.out.println(media.getImages().getThumbnail().getUrl());
			System.out.println("-------------------");
		}
	}
}
