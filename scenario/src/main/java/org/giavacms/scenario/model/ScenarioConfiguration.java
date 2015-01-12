package org.giavacms.scenario.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ScenarioConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private int maxWidthOrHeight;
	private boolean resize;

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
		return "ScenarioConfiguration [id=" + id + ", maxWidthOrHeight="
				+ maxWidthOrHeight + ", resize=" + resize + "]";
	}

}
