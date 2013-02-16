package org.giavacms.exhibition.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Image;

@Entity
@Table(name = "Exhibition")
public class Exhibition implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String preview;
	private String description;
	private String year;
	private Image image;
	private Image newImage;
	private String catalogue;
	private Image catalogueImage;
	private Image newCatalogueImage;
	private String website;
	private Date date;
	private boolean active = true;
	private String externalImage;
	private String externalCatalogueImage;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image_id", unique = true, nullable = true, insertable = true, updatable = true)
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Transient
	public Image getNewImage() {
		if (newImage == null)
			this.newImage = new Image();
		return newImage;
	}

	public void setNewImage(Image newImage) {
		this.newImage = newImage;
	}

	public String getCatalogue() {
		return catalogue;
	}

	public void setCatalogue(String catalogue) {
		this.catalogue = catalogue;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getExternalImage() {
		return externalImage;
	}

	public void setExternalImage(String externalImage) {
		this.externalImage = externalImage;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "catalogueImage_id", unique = true, nullable = true, insertable = true, updatable = true)
	public Image getCatalogueImage() {
		if (catalogueImage == null)
			this.catalogueImage = new Image();
		return catalogueImage;
	}

	public void setCatalogueImage(Image catalogueImage) {
		this.catalogueImage = catalogueImage;
	}

	@Transient
	public Image getNewCatalogueImage() {
		if (newCatalogueImage == null)
			this.newCatalogueImage = new Image();
		return newCatalogueImage;
	}

	public void setNewCatalogueImage(Image newCatalogueImage) {
		this.newCatalogueImage = newCatalogueImage;
	}

	public String getExternalCatalogueImage() {
		return externalCatalogueImage;
	}

	public void setExternalCatalogueImage(String externalCatalogueImage) {
		this.externalCatalogueImage = externalCatalogueImage;
	}

	@Override
	public String toString() {
		return "Exhibition [id=" + id + ", name=" + name + ", preview="
				+ preview + ", description=" + description + ", year=" + year
				+ ", website=" + website + ", date=" + date + "]";
	}

}
