/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.util.logging.Level;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.Template;
import org.giavacms.common.repository.AbstractRepository;

public abstract class AbstractPageRepository<T extends Page> extends
		AbstractRepository<T> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Inject
	TemplateRepository templateRepository;
	@Inject
	TemplateImplRepository templateImplRepository;

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	protected T prePersist(T page) {
		// closeHtmlTags(page);
		String idTitle = PageUtils.createPageId(page.getTitle());
		String idFinal = super.testKey(idTitle);
		Template template = templateRepository.find(page.getTemplate()
				.getTemplate().getId());
		page.getTemplate().setTemplate(template);
		if (page.getTemplate().getId() == null) {
			templateImplRepository.persist(page.getTemplate());
		}
		page.setId(idFinal);
		return page;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected T preUpdate(T page) {
		Template template = templateRepository.find(page.getTemplate()
				.getTemplate().getId());
		if (page.getTemplate().getId() == null) {
			templateImplRepository.persist(page.getTemplate());
		}
		page.getTemplate().setTemplate(template);
		return page;
	}

	@Override
	public boolean delete(Object key) {
		try {
			T obj = getEm().find(getEntityType(), key);
			if (obj != null) {
				obj.setActive(false);
				getEm().remove(obj);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

}
