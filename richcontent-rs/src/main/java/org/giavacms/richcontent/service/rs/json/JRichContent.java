package org.giavacms.richcontent.service.rs.json;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;

public class JRichContent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String description;
	private String preview;
	private String content;
	private String author;
	private Date date;
	private JRichContentType richContentType;
	private List<Document> documents;
	private List<Image> images;
	private boolean highlight;
	private List<String> tagList;

	public JRichContent() {
	}

	public JRichContent(String id, String title, String description,
			String preview, String content, String author, Date date,
			JRichContentType richContentType, List<Document> documents,
			List<Image> images, boolean highlight, List<String> tagList) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.preview = preview;
		this.content = content;
		this.author = author;
		this.date = date;
		this.richContentType = richContentType;
		this.documents = documents;
		this.images = images;
		this.highlight = highlight;
		this.tagList = tagList;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

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

	public JRichContentType getRichContentType() {
		return richContentType;
	}

	public void setRichContentType(JRichContentType richContentType) {
		this.richContentType = richContentType;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	@Override
	public String toString() {
		return "JRichContent ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (title != null ? "title=" + title + ", " : "")
				+ (description != null ? "description=" + description + ", "
						: "")
				+ (preview != null ? "preview=" + preview + ", " : "")
				+ (content != null ? "content=" + content + ", " : "")
				+ (author != null ? "author=" + author + ", " : "")
				+ (date != null ? "date=" + date + ", " : "")
				+ (richContentType != null ? "richContentType="
						+ richContentType + ", " : "")
				+ (documents != null ? "documents=" + documents + ", " : "")
				+ (images != null ? "images=" + images + ", " : "")
				+ "highlight=" + highlight + ", "
				+ (tagList != null ? "tagList=" + tagList : "") + "]";
	}

}
