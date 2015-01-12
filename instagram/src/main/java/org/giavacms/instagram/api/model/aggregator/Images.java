package org.giavacms.instagram.api.model.aggregator;

import org.codehaus.jackson.annotate.JsonProperty;
import org.giavacms.instagram.api.model.common.Image;


public class Images {
	private Image standardResolution;
	private Image thumbnail;
	private Image lowResolution;

	@JsonProperty(value = "standard_resolution")
	public Image getStandardResolution() {
		return standardResolution;
	}

	public void setStandardResolution(Image standardResolution) {
		this.standardResolution = standardResolution;
	}

	@JsonProperty(value = "thumbnail")
	public Image getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}

	@JsonProperty(value = "low_resolution")
	public Image getLowResolution() {
		return lowResolution;
	}

	public void setLowResolution(Image lowResolution) {
		this.lowResolution = lowResolution;
	}
}
