package com.google.api.data.sample.picasa.model;

import com.google.api.client.util.Key;

public class PhotoMediaThumbnail {

	@Key("@url")
	public String url;

	@Key("@height")
	public String height;

	@Key("@width")
	public String width;
}
