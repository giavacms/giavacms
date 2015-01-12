package org.giavacms.instagram.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class InstagramPhoto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	private String summary;
	private String imageUrl;
	private String width;
	private String height;
	private String thumbnailWidth;
	private String thumbnailHeight;
	private String thumbnailUrl;
	private InstagramCollection collection;
	private boolean cover;
	private boolean usable;
	private long index;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(String thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public String getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(String thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	@ManyToOne
	public InstagramCollection getCollection() {
		if (this.collection == null)
			this.collection = new InstagramCollection();
		return collection;
	}

	public void setCollection(InstagramCollection collection) {
		this.collection = collection;
	}

	@Transient
	public boolean isCover() {
		return cover;
	}

	public void setCover(boolean cover) {
		this.cover = cover;
	}

	@Transient
	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	@Transient
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

}