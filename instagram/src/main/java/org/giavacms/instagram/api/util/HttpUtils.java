package org.giavacms.instagram.api.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	public static String postQueryWithParams(Map<String, String> params,
			String url, boolean debug) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			postParameters.add(new BasicNameValuePair(key, params.get(key)));
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
		HttpResponse response = httpclient.execute(httpPost);
		String toJson = EntityUtils.toString(response.getEntity());
		if (debug) {
			System.out.println(toJson);
			System.out.println(response.getProtocolVersion());
			System.out.println(response.getStatusLine().getStatusCode());
			System.out.println(response.getStatusLine().getReasonPhrase());
		}
		return toJson;
	}

	public static String getQueryWithParams(Map<String, String> params,
			String scheme, String host, String path, boolean debug)
			throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> queryParamas = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			queryParamas.add(new BasicNameValuePair(key, params.get(key)));
		}
		URI uri = URIUtils.createURI(scheme, host, -1, path,
				URLEncodedUtils.format(queryParamas, "UTF-8"), null);
		System.out.println(uri);
		HttpGet httpGet = new HttpGet(uri);

		HttpResponse response = httpclient.execute(httpGet);
		String toJson = EntityUtils.toString(response.getEntity());
		if (debug) {
			System.out.println(toJson);
			System.out.println(response.getProtocolVersion());
			System.out.println(response.getStatusLine().getStatusCode());
			System.out.println(response.getStatusLine().getReasonPhrase());
		}
		return toJson;
	}
}
