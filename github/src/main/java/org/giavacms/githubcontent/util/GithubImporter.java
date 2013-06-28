package org.giavacms.githubcontent.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GithubImporter {

	public static String getContent(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			return doc.getElementsByTag("article").html();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
