package org.giavacms.catalogue.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.catalogue.model.Category;
import org.giavacms.common.model.Search;

@Named
@Stateless
@LocalBean
public class CategoryRepository extends AbstractPageRepository<Category> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Override
	protected String getDefaultOrderBy() {
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

		if (true) {
			sb.append(separator).append(alias).append(".active = :active");
			params.put("active", true);
			separator = " and ";
		}

		if (search.getObj().getTitle() != null
				&& !search.getObj().getTitle().isEmpty()) {
			sb.append(separator).append(" upper(").append(alias)
					.append(".title ) like :TITLE ");
			params.put("TITLE", likeParam(search.getObj().getTitle()
					.toUpperCase()));
		}
	}

	@Override
	protected Category prePersist(Category n) {
		n.setClone(true);
		n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
		n = super.prePersist(n);
		return n;
	}

	@Override
	protected Category preUpdate(Category n) {
		n.setClone(true);
		n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
		n = super.preUpdate(n);
		return n;
	}

}
