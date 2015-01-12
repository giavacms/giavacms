package org.giavacms.insuranceclaim.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ICConfiguration")
public class InsuranceClaimConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private boolean resize;
	private int maxWidthOrHeight;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isResize() {
		return resize;
	}

	public void setResize(boolean resize) {
		this.resize = resize;
	}

	public int getMaxWidthOrHeight() {
		return maxWidthOrHeight;
	}

	public void setMaxWidthOrHeight(int maxWidthOrHeight) {
		this.maxWidthOrHeight = maxWidthOrHeight;
	}

	@Override
	public String toString() {
		return "InsuranceClaimConfiguration [id=" + id + ", resize=" + resize
				+ ", maxWidthOrHeight=" + maxWidthOrHeight + "]";
	}

}
