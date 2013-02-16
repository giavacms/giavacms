package org.giavacms.faq.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Image;

@Entity
public class FaqCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private Image image;
	private Image newImage;
	private List<Faq> faqs;
	private boolean active = true;
	private int orderNum;

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
	@Column(length = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "faqCategory", fetch = FetchType.LAZY)
	@OrderBy("question")
	public List<Faq> getFaqs() {
		if (faqs == null)
			this.faqs = new ArrayList<Faq>();
		return faqs;
	}

	public void setFaqs(List<Faq> faqs) {
		this.faqs = faqs;
	}

	public void addFaq(Faq faq) {
		getFaqs().add(faq);
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

	@Override
	public String toString() {
		return "FaqCategory [id=" + id + ", name=" + name + ", description="
				+ description + ", active=" + active + ", orderNum=" + orderNum
				+ "]";
	}

}
