package org.giavacms.github.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.richcontent.model.type.RichContentType;

@Named
@Stateless
@LocalBean
public class GithubContentTypeRepository extends
		AbstractRepository<GithubContentType> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	protected String getDefaultOrderBy() {
		return "name asc";
	}

	@Override
	protected void applyRestrictions(Search<GithubContentType> search,
			String alias, String separator, StringBuffer sb,
			Map<String, Object> params) {
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
	public boolean delete(Object key) {
		try {
			GithubContentType richContentType = getEm().find(getEntityType(),
					key);
			if (richContentType != null) {
				richContentType.setActive(false);
				getEm().merge(richContentType);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

}
