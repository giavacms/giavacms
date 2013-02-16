package org.giavacms.instagram.api.model.common;

import org.codehaus.jackson.annotate.JsonProperty;

public class Like {

	private String username;
	private String fullName;
	private String id;
	private String profilePicture;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty(value = "full_name")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty(value = "profile_picture")
	public String getProfile_picture() {
		return profilePicture;
	}

	public void setProfile_picture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

}
