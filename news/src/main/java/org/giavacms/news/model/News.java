package org.giavacms.news.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.news.model.type.NewsType;


@Entity
public class News implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private boolean active = true;
	private String title;
	private String preview;
	private String content;
	private String author;
	private Date date;
	private NewsType newsType;
	private boolean highlight;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	@Lob
	public String getContent() {
		return content;
	}

	@Transient
	public String getContentN() {
		return HtmlUtils.normalizeHtml(this.content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAutor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne
	public NewsType getNewsType() {
		if (newsType == null)
			newsType = new NewsType();
		return newsType;
	}

	public void setNewsType(NewsType newsType) {
		this.newsType = newsType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", active=" + active + ", title=" + title
				+ ", preview=" + preview + ", content=" + content + ", author="
				+ author + ", date=" + date + ", newsType=" + newsType
				+ ", highlight=" + highlight + "]";
	}

}
