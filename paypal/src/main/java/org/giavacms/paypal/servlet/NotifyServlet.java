package org.giavacms.paypal.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

@WebServlet(name = "NotifyServlet", value = { "/notify/*" })
public class NotifyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static String PAYPAL_URL = "";

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(PAYPAL_URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cmd", "_notify-validate"));
		// You need to add this parameter to
		// tell PayPal to verify
		for (Enumeration<String> e = request.getParameterNames(); e
				.hasMoreElements();) {
			String name = e.nextElement();
			String value = request.getParameter(name);
			params.add(new BasicNameValuePair(name, value));
		}
		post.setEntity(new UrlEncodedFormEntity(params));
		String rc = getRC(client.execute(post)).trim();
		if ("VERIFIED".equals(rc)) {
			// Your business code comes here
		}
	}

	private String getRC(HttpResponse response) throws IOException,
			IllegalStateException {
		InputStream is = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String result = "";
		String line = null;
		while ((line = br.readLine()) != null) {
			result += line;
		}
		return result;
	}

}