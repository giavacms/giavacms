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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Image;

@Entity
@Table(name = "ICCategory")
public class InsuranceClaimCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private String preview;
	private List<InsuranceClaimProduct> insuranceClaimProducts;
	private InsuranceClaimTypology insuranceClaimTypology;
	private boolean active = true;
	private int orderNum;

	private Image image;
	private Image newImage;

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
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "insuranceClaimCategory", fetch = FetchType.LAZY)
	@OrderBy("name")
	public List<InsuranceClaimProduct> getInsuranceClaimProducts() {
		if (insuranceClaimProducts == null)
			this.insuranceClaimProducts = new ArrayList<InsuranceClaimProduct>();
		return insuranceClaimProducts;
	}

	public void setInsuranceClaimProducts(
			List<InsuranceClaimProduct> insuranceClaimProducts) {
		this.insuranceClaimProducts = insuranceClaimProducts;
	}

	public void addInsuranceClaimProduct(
			InsuranceClaimProduct insuranceClaimProduct) {
		getInsuranceClaimProducts().add(insuranceClaimProduct);
	}

	@Override
	public String toString() {
		return "InsuranceClaimCategory [id=" + id + ", name=" + name
				+ ", description=" + description + ", active=" + active + "]";
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

	@ManyToOne
	public InsuranceClaimTypology getInsuranceClaimTypology() {
		if (this.insuranceClaimTypology == null)
			this.insuranceClaimTypology = new InsuranceClaimTypology();
		return insuranceClaimTypology;
	}

	public void setInsuranceClaimTypology(
			InsuranceClaimTypology insuranceClaimTypology) {
		this.insuranceClaimTypology = insuranceClaimTypology;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image_id", unique = true, nullable = true, insertable = true, updatable = true)
	public Image getImage() {
		if (image == null)
			this.image = new Image();
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

	@Lob
	@Column(length = 100 * 1024)
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

}
