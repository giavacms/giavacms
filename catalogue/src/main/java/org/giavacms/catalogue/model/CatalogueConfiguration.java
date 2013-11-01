package org.giavacms.catalogue.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CatalogueConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private boolean resize;
	private int maxWidthOrHeight;
	private boolean withPrices;
	private boolean withDimensions;

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
		return "CatalogueConfiguration [id=" + id + ", resize=" + resize
				+ ", maxWidthOrHeight=" + maxWidthOrHeight + "]";
	}

   public boolean isWithPrices()
   {
      return withPrices;
   }

   public void setWithPrices(boolean withPrices)
   {
      this.withPrices = withPrices;
   }

   public boolean isWithDimensions()
   {
      return withDimensions;
   }

   public void setWithDimensions(boolean withDimensions)
   {
      this.withDimensions = withDimensions;
   }

}
