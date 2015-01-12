package org.giavacms.instagram.api.model.result;

import java.io.Serializable;
import java.util.List;

import org.giavacms.instagram.api.model.common.Media;
import org.giavacms.instagram.api.model.common.Meta;
import org.giavacms.instagram.api.model.common.Pagination;


public class PaginationResult implements Serializable {
	private Pagination pagination;
	private Meta meta;
	private List<Media> data;

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public List<Media> getData() {
		return data;
	}

	public void setData(List<Media> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PaginationResult [pagination=" + pagination + ", meta=" + meta
				+ ", data=" + data + "]";
	}

}
