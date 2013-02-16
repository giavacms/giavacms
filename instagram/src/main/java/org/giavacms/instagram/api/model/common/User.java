package org.giavacms.instagram.api.model.common;

import java.io.Serializable;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonProperty;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;// fiorenzino",
	private String bio;
	private String website; // ":"http:\/\/son.flower.io","
	private String profilePicture; // ":"http:\/\/images.instagram.com\/profiles\/profile_5624011_75sq_1312413639.jpg","
	private String fullName; // ":"Flower The power",
	private String id;// ":"5624011"}}
	private String firstName;
	private String lastName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBio() {
		return bio;
	}

	public String getBioJS() {
		return StringEscapeUtils.escapeJavaScript(bio);
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@JsonProperty(value = "profile_picture")
	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	@JsonProperty(value = "full_name")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@JsonProperty(value = "first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty(value = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", bio=" + bio + ", website="
				+ website + ", profilePicture=" + profilePicture
				+ ", fullName=" + fullName + ", id=" + id + ", firstName="
				+ firstName + ", lastName=" + lastName + "]";
	}

}
