package org.giavacms.instagram.api.model.common;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Pagination implements Serializable {
	private String nextMaxTagId;
	private String deprecationWarning;
	private String nextMaxId;
	private String nextMinId;
	private String minTagId;
	private String nextUrl;

	@JsonProperty(value = "next_max_tag_id")
	public String getNextMaxTagId() {
		return nextMaxTagId;
	}

	public void setNextMaxTagId(String nextMaxTagId) {
		this.nextMaxTagId = nextMaxTagId;
	}

	@JsonProperty(value = "deprecation_warning")
	public String getDeprecationWarning() {
		return deprecationWarning;
	}

	public void setDeprecationWarning(String deprecationWarning) {
		this.deprecationWarning = deprecationWarning;
	}

	@JsonProperty(value = "next_max_id")
	public String getNextMaxId() {
		return nextMaxId;
	}

	public void setNextMaxId(String nextMaxId) {
		this.nextMaxId = nextMaxId;
	}

	@JsonProperty(value = "next_min_id")
	public String getNextMinId() {
		return nextMinId;
	}

	public void setNextMinId(String nextMinId) {
		this.nextMinId = nextMinId;
	}

	@JsonProperty(value = "min_tag_id")
	public String getMinTagId() {
		return minTagId;
	}

	public void setMinTagId(String minTagId) {
		this.minTagId = minTagId;
	}

	@JsonProperty(value = "next_url")
	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	@Override
	public String toString() {
		return "Pagination [nextMaxTagId=" + nextMaxTagId
				+ ", deprecationWarning=" + deprecationWarning + ", nextMaxId="
				+ nextMaxId + ", nextMinId=" + nextMinId + ", minTagId="
				+ minTagId + ", nextUrl=" + nextUrl + "]";
	}

}
