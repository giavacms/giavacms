/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TemplateImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	private Long id;
	boolean active = true;

	private String header;
	private String footer;
	private String col1;
	private String col2;
	private String col3;
	// private Page page;
	private List<Page> pages;
	private Template template;

	// ------------------------------------------------------------------------

	public TemplateImpl() {
	}

	// ------------------------------------------------------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Template getTemplate() {
		if (template == null)
			this.template = new Template();
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}

	@Lob
	@Column(length = 100 * 1024)
	public String getCol3() {
		return col3;
	}

	public void setCol3(String col3) {
		this.col3 = col3;
	}

	// @OneToOne
	// public Page getPage()
	// {
	// return page;
	// }
	//
	// public void setPage(Page page)
	// {
	// this.page = page;
	// }

	@OneToMany(mappedBy = "template")
	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return (this.id != null) ? this.id.toString() : super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TemplateImpl))
			return false;
		TemplateImpl t = (TemplateImpl) o;
		return t.getId() == null ? false : t.getId().equals(this.id);
	}

	@Override
	public int hashCode() {
		return (this.id != null) ? this.id.hashCode() : super.hashCode();
	}

}
