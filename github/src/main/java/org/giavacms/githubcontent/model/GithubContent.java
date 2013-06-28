package org.giavacms.githubcontent.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.model.Page;

@Entity
@DiscriminatorValue(value = GithubContent.EXTENSION)
public class GithubContent extends Page {

	private static final long serialVersionUID = 1L;
	public static final String EXTENSION = "GithubContent";
	private static final String TAG_SEPARATOR = ",";

	private String preview;
	private String content;
	private String author;
	private Date date;
	private boolean highlight;
	private String tag;
	private List<String> tagList;
	private String tags;

	public GithubContent() {
		super();
		super.setExtension(EXTENSION);
	}

	@Transient
	public String getContentN() {
		return HtmlUtils.normalizeHtml(this.content);
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
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

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
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

	@Override
	public String toString() {
		return "RichContent [id=" + super.getId() + ", active="
				+ super.isActive() + ", title=" + super.getTitle()
				+ ", preview=" + preview + ", content=" + content + ", author="
				+ author + ", date=" + date + ", tags=" + tags + ", highlight="
				+ highlight + "]";
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
		this.tagList = null;
	}

	@Transient
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Transient
	public List<String> getTagList() {
		if (tagList != null) {
			return tagList;
		}
		tagList = new ArrayList<String>();
		if (tags == null) {
			return tagList;
		}
		String[] tagArray = tags.split(TAG_SEPARATOR);
		for (String tag : tagArray) {
			if (tag != null && tag.trim().length() > 0) {
				tagList.add(tag.trim());
			}
		}
		return tagList;
	}

	@Transient
	public Image getFirstImage() {
		return firstImage;
	}

	public void setFirstImage(Image firstImage) {
		this.firstImage = firstImage;
	}

}
