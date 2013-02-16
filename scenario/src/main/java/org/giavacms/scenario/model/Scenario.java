package org.giavacms.scenario.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Product;


@Entity
public class Scenario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String preview;
	private String description;
	private List<Product> products;
	private List<Document> documents;
	private List<Image> images;
	private boolean active = true;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	@Lob
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	public List<Product> getProducts() {
		if (products == null)
			this.products = new ArrayList<Product>();
		return products;
	}

	@Transient
	public String getProductNames() {
		StringBuffer productNames = new StringBuffer();
		for (Product product : getProducts()) {
			productNames.append("," + product.getName());
		}
		return productNames.length() > 0 ? productNames.toString().substring(1)
				: "";
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void addProduct(Product product) {
		getProducts().add(product);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "Scenario_Document", joinColumns = @JoinColumn(name = "Scenario_id"), inverseJoinColumns = @JoinColumn(name = "documents_id"))
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
	@JoinTable(name = "Scenario_Image", joinColumns = @JoinColumn(name = "Scenario_id"), inverseJoinColumns = @JoinColumn(name = "images_id"))
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Scenario [id=" + id + ", name=" + name + ", preview=" + preview
				+ ", description=" + description + ", active=" + active + "]";
	}

}
