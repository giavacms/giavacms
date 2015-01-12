package org.giavacms.instagram.api.model.result;

import java.util.List;

import org.giavacms.instagram.api.model.common.Meta;
import org.giavacms.instagram.api.model.common.User;


public class UserSearchResult {
	private List<User> data;
	private Meta meta;

	public List<User> getData() {
		return data;
	}

	public void setData(List<User> data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
