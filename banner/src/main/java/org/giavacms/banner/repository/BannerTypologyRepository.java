package org.giavacms.banner.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.banner.model.BannerTypology;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;

@Named
@Stateless
@LocalBean
public class BannerTypologyRepository extends
		AbstractRepository<BannerTypology> {

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
		return "name asc";
	}

	@Override
	public boolean delete(Object key) {
		try {
			BannerTypology bannerTypology = getEm().find(getEntityType(), key);
			if (bannerTypology != null) {
				bannerTypology.setActive(false);
				getEm().merge(bannerTypology);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

	@Override
	protected void applyRestrictions(Search<BannerTypology> search,
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

}
