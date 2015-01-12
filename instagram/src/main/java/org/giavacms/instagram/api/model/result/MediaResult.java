package org.giavacms.instagram.api.model.result;

import org.giavacms.instagram.api.model.common.Media;
import org.giavacms.instagram.api.model.common.Meta;


public class MediaResult {
	private Media data;
	private Meta meta;

	public Media getData() {
		return data;
	}

	public void setData(Media data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return "MediaResult [data=" + data + ", meta=" + meta + "]";
	}

}
