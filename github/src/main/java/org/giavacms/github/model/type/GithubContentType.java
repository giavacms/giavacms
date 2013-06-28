package org.giavacms.github.model.type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.giavacms.base.model.Page;

@Entity
public class GithubContentType implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private boolean active = true;
	private String name;
	private Page page;

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ManyToOne
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "RichContentType [id=" + id + ", active=" + active + ", name="
				+ name + ", page=" + page + "]";
	}

}
