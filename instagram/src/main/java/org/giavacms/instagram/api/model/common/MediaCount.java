package org.giavacms.instagram.api.model.common;

import org.codehaus.jackson.annotate.JsonProperty;

public class MediaCount {
	private int mediaCount;
	private String name;

	@JsonProperty(value = "media_count")
	public int getMediaCount() {
		return mediaCount;
	}

	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MediaCount [mediaCount=" + mediaCount + ", name=" + name + "]";
	}

}
