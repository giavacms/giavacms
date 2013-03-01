/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

import org.giavacms.base.model.Page;
import org.giavacms.common.model.Search;

@Named
@Stateless
@LocalBean
public class PageRepository
// extends AbstractRepository<Page>
		extends AbstractPageRepository<Page> {

	private static final long serialVersionUID = 1L;

	// @PersistenceContext
	// EntityManager em;
	//
	// @Inject
	// TemplateRepository templateRepository;
	//
	// @Override
	// public EntityManager getEm() {
	// return em;
	// }
	//
	// @Override
	// public void setEm(EntityManager em) {
	// this.em = em;
	// }

	// @Override
	// protected Page prePersist(Page page) {
	// // closeHtmlTags(page);
	// String idTitle = PageUtils.createPageId(page.getTitle());
	// String idFinal = super.testKey(idTitle);
	// Template template = templateRepository.find(page.getTemplate()
	// .getTemplate().getId());
	// page.getTemplate().setTemplate(template);
	// page.setId(idFinal);
	// return page;
	// }
	//
	// @TransactionAttribute(TransactionAttributeType.REQUIRED)
	// protected Page preUpdate(Page page) {
	// Template template = templateRepository.find(page.getTemplate()
	// .getTemplate().getId());
	// page.getTemplate().setTemplate(template);
	// return page;
	// }

	@Override
	protected void applyRestrictions(Search<Page> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active ");
		params.put("active", true);
		separator = " and ";

		if (!search.getObj().isClone()) {
			sb.append(separator).append(alias).append(".clone = :clone ");
			params.put("clone", false);
			separator = " and ";
		}

		if (search.getObj().getTemplate() != null
				&& search.getObj().getTemplate().getTemplate() != null
				&& search.getObj().getTemplate().getTemplate()
						.getSearchStatico() != null) {
			sb.append(separator).append(alias)
					.append(".template.template.statico = :statico ");
			params.put("statico", search.getObj().getTemplate().getTemplate()
					.getSearchStatico());
		}

		if (search.getObj().getTitle() != null
				&& !search.getObj().getTitle().isEmpty()) {
			sb.append(separator).append(alias).append(".title like :title ");
			params.put("title", likeParam(search.getObj().getTitle()));
		}
		if (search.getObj().getLang() > 0) {
			if (search.getObj().getLang() == 1) {
				sb.append(separator).append(alias).append(".id = ")
						.append(alias).append(".lang1id ");
			} else if (search.getObj().getLang() == 2) {
				sb.append(separator).append(alias).append(".id = ")
						.append(alias).append(".lang2id ");
			} else if (search.getObj().getLang() == 3) {
				sb.append(separator).append(alias).append(".id = ")
						.append(alias).append(".lang3id ");
			} else if (search.getObj().getLang() == 4) {
				sb.append(separator).append(alias).append(".id = ")
						.append(alias).append(".lang4id ");
			} else if (search.getObj().getLang() == 5) {
				sb.append(separator).append(alias).append(".id = ")
						.append(alias).append(".lang5id ");
			}
		}

	}

	@Override
	public Page fetch(Object id) {
		logger.info("fetchPage: " + id);
		Page ret = null;
		try {
			ret = (Page) em
					.createQuery(
							"select p from Page p left join fetch p.template ti left join fetch ti.template t where p.id = :ID ")
					.setParameter("ID", id).setMaxResults(1).getSingleResult();
			em.refresh(ret);
			// i18n support
			ret.setLang(ret.getId().equals(ret.getLang1id()) ? 1 : ret.getId()
					.equals(ret.getLang2id()) ? 2 : ret.getId().equals(
					ret.getLang3id()) ? 3 : ret.getId()
					.equals(ret.getLang4id()) ? 4 : ret.getId().equals(
					ret.getLang5id()) ? 5 : 0);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return ret;
	}

	public boolean reallyDelete(Object key) {
		try {
			Page obj = getEm().find(getEntityType(), key);
			getEm().remove(obj);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

	// @Override
	// public boolean delete(Object key) {
	// try {
	// Page obj = getEm().find(getEntityType(), key);
	// if (obj != null) {
	// obj.setActive(false);
	// getEm().remove(obj);
	// }
	// return true;
	// } catch (Exception e) {
	// logger.log(Level.SEVERE, null, e);
	// return false;
	// }
	// }

	@Override
	protected String getDefaultOrderBy() {
		return "title asc";
	}

	@SuppressWarnings("unchecked")
	public List<Page> getExtensions(String extension) {
		try {
			StringBuffer sbq = new StringBuffer("select p from "
					+ Page.class.getSimpleName() + " p where p.clone = :CLONE and p.extension ");
			if (extension != null && extension.trim().length() > 0) {
				sbq.append(" = :EXTENSION ");
			} else {
				sbq.append(" is null ");
			}
			Query q = getEm().createQuery(sbq.toString()).setParameter("CLONE", false);
			if (extension != null && extension.trim().length() > 0) {
				q.setParameter("EXTENSION", extension);
			}
			return q.getResultList();
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return new ArrayList<Page>();
		}
	}

	public void markExtensionByTemplateId(Long id, String extension) {
		try {
			int updatedPages = getEm()
					.createQuery(
							"update "
									+ Page.class.getSimpleName()
									+ " p set p.extension = :EXTENSION where p.template.id = :TEMPLATEIMPLID ")
					.setParameter("EXTENSION", extension)
					.setParameter("TEMPLATEIMPLID", id).executeUpdate();
			logger.info(updatedPages + " where updated to " + extension);
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
		}
	}
}
