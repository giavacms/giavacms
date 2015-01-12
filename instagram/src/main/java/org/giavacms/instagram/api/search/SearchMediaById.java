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
import org.giavacms.instagram.api.model.result.AccessTokenResult;
import org.giavacms.instagram.api.model.result.MediaResult;
import org.giavacms.instagram.api.util.HttpUtils;


public class SearchMediaById {

	private static String url = "https://api.instagram.com/v1/media/3?access_token=5624011.f59def8.649a92bc328d4b988884a7ce688a3d2e";
	private static String TOKEN = "5624011.6fd0d93.cc06ab2e8af94f37ace87092ec755c49";

	public static MediaResult execute(String accessToken, String mediaId,
			boolean debug) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		String toJson = HttpUtils.getQueryWithParams(params, "https",
				"api.instagram.com/v1", "/media/" + mediaId, debug);
		ObjectMapper mapper = new ObjectMapper();
		MediaResult result = mapper.readValue(toJson.getBytes(),
				MediaResult.class);
		return result;
	}

	public static void main(String[] args) throws Exception {
		MediaResult result = execute(TOKEN, "176071718492615078_5624011", true);
		System.out.println(result);
	}

	public static void main2(String[] args) throws ClientProtocolException,
			IOException, URISyntaxException {
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> queryParamas = new ArrayList<NameValuePair>();
		queryParamas.add(new BasicNameValuePair("access_token", TOKEN));

		// 1336020887304

		URI uri = URIUtils.createURI("https", "api.instagram.com/v1", -1,
				"/media/176071718492615078_5624011",
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
		MediaResult result = mapper.readValue(toJson.getBytes(),
				MediaResult.class);

		System.out.println("id:" + result.getData().getId());
		System.out.println("NOME: " + result.getData().getUser().getFullName());
		System.out.println(result.getData().getImages().getLowResolution()
				.getUrl());
		System.out.println(result.getData().getImages().getStandardResolution()
				.getUrl());
		System.out
				.println(result.getData().getImages().getThumbnail().getUrl());
		System.out.println("-------------------");

	}
}
