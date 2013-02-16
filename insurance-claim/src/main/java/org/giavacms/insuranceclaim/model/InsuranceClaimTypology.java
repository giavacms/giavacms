package org.giavacms.insuranceclaim.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "ICTypology")
public class InsuranceClaimTypology implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private List<InsuranceClaimCategory> insuranceClaimCategories;
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
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "insuranceClaimTypology", fetch = FetchType.LAZY)
	@OrderBy("name")
	public List<InsuranceClaimCategory> getInsuranceClaimCategories() {
		if (insuranceClaimCategories == null)
			this.insuranceClaimCategories = new ArrayList<InsuranceClaimCategory>();
		return insuranceClaimCategories;
	}

	public void setInsuranceClaimCategories(
			List<InsuranceClaimCategory> insuranceClaimCategories) {
		this.insuranceClaimCategories = insuranceClaimCategories;
	}

	public void addInsuranceClaimCategory(
			InsuranceClaimCategory insuranceClaimCategory) {
		getInsuranceClaimCategories().add(insuranceClaimCategory);
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
}
