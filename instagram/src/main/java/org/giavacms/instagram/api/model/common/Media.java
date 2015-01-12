package org.giavacms.instagram.api.model.common;

import java.io.Serializable;
import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonProperty;
import org.giavacms.instagram.api.model.aggregator.Comments;
import org.giavacms.instagram.api.model.aggregator.Images;
import org.giavacms.instagram.api.model.aggregator.Likes;


public class Media implements Serializable {
	private Attribution attribution;
	private String type;
	private String filter;
	private String[] tags;
	private Comments comments;
	private Caption caption;
	private Likes likes;
	private String link;
	private User user;
	private String createdTime;
	private Images images;
	private String id;
	private Location location;
	private String userHasLiked;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String[] getTags() {
		return tags;
	}

	public String getAllTags() {
		StringBuffer ret = new StringBuffer();
		for (String toA : tags) {
			ret.append("," + toA);
		}
		if (ret != null && ret.length() > 0)
			return ret.toString().substring(1);
		return "";
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public Caption getCaption() {
		return caption;
	}

	public void setCaption(Caption caption) {
		this.caption = caption;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonProperty(value = "created_time")
	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Comments getComments() {
		return comments;
	}

	public void setComments(Comments comments) {
		this.comments = comments;
	}

	public Likes getLikes() {
		return likes;
	}

	public void setLikes(Likes likes) {
		this.likes = likes;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public Attribution getAttribution() {
		return attribution;
	}

	public void setAttribution(Attribution attribution) {
		this.attribution = attribution;
	}

	@JsonProperty(value = "user_has_liked")
	public String getUserHasLiked() {
		return userHasLiked;
	}

	public void setUserHasLiked(String userHasLiked) {
		this.userHasLiked = userHasLiked;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Media [attribution=" + attribution + ", type=" + type
				+ ", filter=" + filter + ", tags=" + Arrays.toString(tags)
				+ ", comments=" + comments + ", caption=" + caption
				+ ", likes=" + likes + ", link=" + link + ", user=" + user
				+ ", createdTime=" + createdTime + ", images=" + images
				+ ", id=" + id + ", location=" + location + ", userHasLiked="
				+ userHasLiked + "]";
	}

}
