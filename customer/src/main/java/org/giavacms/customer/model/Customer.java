/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.customer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;

@Entity
@DiscriminatorValue(value = Customer.EXTENSION)
@Table(name = Customer.TABLE_NAME)
public class Customer extends Page implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String EXTENSION = "Customer";
	public static final String TABLE_NAME = "Customer";
	public static final boolean HAS_DETAILS = true;

	public Customer() {
		super();
		super.setExtension(EXTENSION);
	}

	// private Long id --> super.id;
	// private String name --> super.title;
	// private String description --> super.description;

	// private boolean active = true;

	private String preview;
	private String address;
	private String web;
	private String contact;
	private String social;
	private CustomerCategory category;
	List<Document> documents;
	List<Image> images;

	private String dimensions;
	private Integer listOrder = 0;
	private String area;

	@Transient
	@Deprecated
	public String getName() {
		return super.getTitle();
	}

	@Deprecated
	public void setName(String name) {
		super.setTitle(name);
	}

	@Lob
	@Column(length = 1024)
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	@ManyToOne
	public CustomerCategory getCategory() {
		if (this.category == null)
			this.category = new CustomerCategory();
		return category;
	}

	public void setCategory(CustomerCategory category) {
		this.category = category;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "Customer_Document", joinColumns = @JoinColumn(name = "Customer_id"), inverseJoinColumns = @JoinColumn(name = "documents_id"))
	public List<Document> getDocuments() {
		if (this.documents == null)
			this.documents = new ArrayList<Document>();
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public void addDocument(Document document) {
		getDocuments().add(document);
	}

	@Transient
	public int getDocSize() {
		return getDocuments().size();
	}

	@Transient
	public Image getImage() {
		if (getImages() != null && getImages().size() > 0)
			return getImages().get(0);
		return null;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "Customer_Image", joinColumns = @JoinColumn(name = "Customer_id"), inverseJoinColumns = @JoinColumn(name = "images_id"))
	public List<Image> getImages() {
		if (this.images == null)
			this.images = new ArrayList<Image>();
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public void addImage(Image image) {
		getImages().add(image);
	}

	@Transient
	public int getImgSize() {
		return getImages().size();
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public Integer getListOrder() {
		return listOrder;
	}

	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getSocial() {
		return social;
	}

	public void setSocial(String social) {
		this.social = social;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Customer [preview=" + preview + ", address=" + address
				+ ", web=" + web + ", contact=" + contact + ", social="
				+ social + ", category="
				+ (category == null ? null : category.getName())
				+ ", dimensions=" + dimensions + ", listOrder=" + listOrder
				+ ", area=" + area + "]";
	}

	public void setLangAsString(String lang) {
		super.setLang(Integer.parseInt(lang));
	}

}
