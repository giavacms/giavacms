package org.giavacms.instagram.api.model.aggregator;

import java.util.List;

import org.giavacms.instagram.api.model.common.Comment;


public class Comments {
	private List<Comment> data;
	private long count;

	public List<Comment> getData() {
		return data;
	}

	public void setData(List<Comment> data) {
		this.data = data;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
