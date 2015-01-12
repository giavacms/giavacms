package org.giavacms.instagram.api.model.result;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.giavacms.instagram.api.model.common.User;


public class AccessTokenResult implements Serializable {

	private String accessToken;
	private User user;

	@JsonProperty(value = "access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AccessTokenResult [accessToken=" + accessToken + ", user="
				+ user + "]";
	}

}
