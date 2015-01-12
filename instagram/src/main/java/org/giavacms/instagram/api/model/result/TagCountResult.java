package org.giavacms.instagram.api.model.result;

import org.giavacms.instagram.api.model.common.MediaCount;
import org.giavacms.instagram.api.model.common.Meta;

public class TagCountResult {
	private MediaCount data;
	private Meta meta;

	public MediaCount getData() {
		return data;
	}

	public void setData(MediaCount data) {
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
		return "TagCountResult [data=" + data + ", meta=" + meta + "]";
	}

}
