package org.giavacms.instagram.api.model.common;

import org.codehaus.jackson.annotate.JsonProperty;

public class Attribution {
	private String website;
	private String itunesUrl;
	private String name;

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@JsonProperty(value = "itunes_url")
	public String getItunesUrl() {
		return itunesUrl;
	}

	public void setItunesUrl(String itunesUrl) {
		this.itunesUrl = itunesUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Attribution [website=" + website + ", itunesUrl=" + itunesUrl
				+ ", name=" + name + "]";
	}

}
