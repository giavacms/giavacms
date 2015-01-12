package org.giavacms.instagram.api.model.aggregator;

import java.util.List;

import org.giavacms.instagram.api.model.common.Like;


public class Likes {
	private long count;
	private List<Like> data;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<Like> getData() {
		return data;
	}

	public void setData(List<Like> data) {
		this.data = data;
	}
}
