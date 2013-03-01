/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE) // non ci risparmia una
// seconda query per calcolare il resto dei dati secondo me, e per contro rende
// "huge" la tabella PAGE
@DiscriminatorColumn(name = "EXTENSION", discriminatorType = DiscriminatorType.STRING)
public class Page extends I18nSupport
// implements I18Nable, Serializable
{

	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------

	boolean active = true;
	boolean clone = false;
	private String title;
	private String description;
	private TemplateImpl template;
	private String extension;

	// ------------------------------------------------------------------------

	// transiente, per accumulare il risultato finale
	private String content;

	// ------------------------------------------------------------------------

	public Page() {
	}

	// ------------------------------------------------------------------------

	@ManyToOne(fetch = FetchType.EAGER, cascade = { /*CascadeType.PERSIST,*/
			CascadeType.MERGE, CascadeType.DETACH })
	public TemplateImpl getTemplate() {
		if (template == null)
			this.template = new TemplateImpl();
		return template;
	}

	public void setTemplate(TemplateImpl template) {
		this.template = template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	@Column(length = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isClone() {
		return clone;
	}

	public void setClone(boolean clone) {
		this.clone = clone;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	// ------------------------------------------------------------------------

	@Transient
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// ------------------------------------------------------------------------

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Page))
			return false;
		Page p = (Page) o;
		return p.getId() == null ? false : p.getId().equals(this.getId());
	}

	@Override
	public String toString() {
		return "Page [id=" + this.getId() + ", active=" + active + ", title="
				+ title + ", description=" + description + ", template="
				+ template.getTemplate().getName() + ", content=" + content
				+ "]";
	}

	@Override
	public int hashCode() {
		return (this.getId() != null) ? this.getId().hashCode() : super
				.hashCode();
	}

}
