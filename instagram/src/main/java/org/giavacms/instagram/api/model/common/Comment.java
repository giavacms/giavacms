package org.giavacms.instagram.api.model.common;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class Comment implements Serializable {
	private String createdTime;
	private String text;
	private From from;
	private String username;
	private String id;

	@JsonProperty(value = "created_time")
	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreated_time(String createdTime) {
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Comment [createdTime=" + createdTime + ", text=" + text
				+ ", from=" + from + ", username=" + username + ", id=" + id
				+ "]";
	}

}
