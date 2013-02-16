package org.giavacms.catalogue.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.catalogue.model.Category;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;


@Named
@Stateless
@LocalBean
public class CategoryRepository extends AbstractRepository<Category> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Override
	protected EntityManager getEm() {
		return em;
	}

	@Override
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	protected String getDefaultOrderBy() {
		// TODO Auto-generated method stub
		return "orderNum asc";
	}

	@Override
	public boolean delete(Object key) {
		try {
			Category category = getEm().find(getEntityType(), key);
			if (category != null) {
				category.setActive(false);
				getEm().merge(category);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

	@Override
	protected void applyRestrictions(Search<Category> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator).append(" upper(").append(alias)
					.append(".name ) like :NAME ");
			params.put("NAME", likeParam(search.getObj().getName()
					.toUpperCase()));
		}
	}

}
