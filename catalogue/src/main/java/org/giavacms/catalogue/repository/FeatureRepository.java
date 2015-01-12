/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.catalogue.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.catalogue.model.Feature;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;

@Named
@Stateless
@LocalBean
public class FeatureRepository extends AbstractRepository<Feature> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Override
	protected String getDefaultOrderBy() {
		return "name asc, option asc";
	}

	@Override
	public boolean delete(Object key) {
		try {
			Feature property = getEm().find(getEntityType(), key);
			if (property != null) {
				property.setActive(false);
				getEm().merge(property);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	protected void applyRestrictions(Search<Feature> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {

		if (true) {
			sb.append(separator).append(alias).append(".active = :ACTIVE ");
			params.put("ACTIVE", true);
			separator = " and ";
		}

		if (search.getObj().getName() != null
				&& search.getObj().getName().trim().length() > 0) {
			sb.append(separator).append(" upper ( ").append(alias)
					.append(".name ) like :NAME ");
			params.put("NAME", likeParam(search.getObj().getName().trim()
					.toUpperCase()));
			separator = " and ";
		}

		if (search.getObj().getOption() != null
				&& search.getObj().getOption().trim().length() > 0) {
			sb.append(separator).append(" upper ( ").append(alias)
					.append(".option ) like :OPTION ");
			params.put("OPTION", likeParam(search.getObj().getOption().trim()
					.toUpperCase()));
			separator = " and ";
		}

	}

	@SuppressWarnings("unchecked")
	public List<String> getRefereanceableNames() {
		try {
			return getEm()
					.createQuery(
							"select distinct p.name from "
									+ Feature.class.getSimpleName()
									+ " p where p.active = :ACTIVE order by p.name asc ")
					.setParameter("ACTIVE", true).getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<String>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getAvailableOptions(String name) {
		try {
			List<String> options = getEm()
					.createQuery(
							"select distinct p.option from "
									+ Feature.class.getSimpleName()
									+ " p where p.active = :ACTIVE and p.name = :NAME order by p.option asc ")
					.setParameter("ACTIVE", true).setParameter("NAME", name)
					.getResultList();
			if (options != null && options.size() == 1
					&& options.get(0) != null
					&& options.get(0).toLowerCase().startsWith("select ")) {
				return getEm().createNativeQuery(options.get(0))
						.getResultList();
			} else {
				return options;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<String>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> getOptions(String name) {
		try {
			return getEm()
					.createQuery(
							"select distinct p.option from "
									+ Feature.class.getSimpleName()
									+ " p where p.active = :ACTIVE and p.name = :NAME order by p.option asc ")
					.setParameter("ACTIVE", true).setParameter("NAME", name)
					.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<String>();
		}
	}

	@Override
	protected EntityManager getEm() {
		return em;
	}

	@Override
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
