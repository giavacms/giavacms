package org.giavacms.insuranceclaim.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;

@Entity
@Table(name = "ICProduct")
public class InsuranceClaimProduct implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String preview;
	private String description;
	private InsuranceClaimCategory insuranceClaimCategory;
	private String code;
	List<Document> documents;
	List<Image> images;
	private int orderNum;
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
	@Column(length = 100 * 1024)
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	public InsuranceClaimCategory getInsuranceClaimCategory() {
		if (this.insuranceClaimCategory == null)
			this.insuranceClaimCategory = new InsuranceClaimCategory();
		return insuranceClaimCategory;
	}

	public void setInsuranceClaimCategory(
			InsuranceClaimCategory insuranceClaimCategory) {
		this.insuranceClaimCategory = insuranceClaimCategory;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "InsuranceClaimProduct_Document", joinColumns = @JoinColumn(name = "InsuranceClaimProduct_id"), inverseJoinColumns = @JoinColumn(name = "documents_id"))
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
	@JoinTable(name = "InsuranceClaimProduct_Image", joinColumns = @JoinColumn(name = "InsuranceClaimProduct_id"), inverseJoinColumns = @JoinColumn(name = "images_id"))
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", preview=" + preview
				+ ", description=" + description + ", category="
				+ insuranceClaimCategory.getName() + ", code=" + code
				+ ", active=" + active + "]";
	}

}
