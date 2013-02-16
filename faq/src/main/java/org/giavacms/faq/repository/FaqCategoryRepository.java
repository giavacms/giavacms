package org.giavacms.faq.repository;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.faq.model.FaqCategory;

@Named
@Stateless
@LocalBean
public class FaqCategoryRepository extends AbstractRepository<FaqCategory> {

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
	public FaqCategory fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			FaqCategory faqCategory = find(id);
			return faqCategory;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	@Override
	public boolean delete(Object key) {
		try {
			FaqCategory faqCategory = getEm().find(getEntityType(), key);
			if (faqCategory != null) {
				faqCategory.setActive(false);
				getEm().merge(faqCategory);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

	@Override
	protected void applyRestrictions(Search<FaqCategory> search, String alias,
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

	@Override
	public List<FaqCategory> getList(Search<FaqCategory> ricerca, int startRow,
			int pageSize) {
		List<FaqCategory> list = super.getList(ricerca, startRow, pageSize);
		return list;
	}

	@Override
	public int getListSize(Search<FaqCategory> ricerca) {
		int size = super.getListSize(ricerca);
		return size;
	}
}
