package org.giavacms.instagram.api.model.common;

import org.codehaus.jackson.annotate.JsonProperty;

public class Caption {
	private String createdTime;
	private String text;
	private From from;
	private String id;

	@JsonProperty(value = "created_time")
	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
