/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.model.TemplateImpl;
import org.giavacms.common.repository.AbstractRepository;

@Named
@Stateless
@LocalBean
public class TemplateImplRepository extends AbstractRepository<TemplateImpl>
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	protected String getDefaultOrderBy() {
		return "id asc";
	}

	@Override
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
