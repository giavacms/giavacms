package org.giavacms.banner.repository;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.banner.model.Banner;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;

@Named
@Stateless
@LocalBean
public class BannerRepository extends AbstractRepository<Banner> {

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
	public Banner fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			Banner banner = find(id);
			return banner;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	@Override
	protected void applyRestrictions(Search<Banner> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";

		// TYPOLOGY NAME
		if (search.getObj().getBannerTypology() != null
				&& search.getObj().getBannerTypology().getName() != null
				&& search.getObj().getBannerTypology().getName().trim()
						.length() > 0) {
			sb.append(separator).append(alias)
					.append(".bannerTypology.name = :NAMETYP ");
			params.put("NAMETYP", search.getObj().getBannerTypology().getName());
		}
		// TYPOLOGY ID
		if (search.getObj().getBannerTypology() != null
				&& search.getObj().getBannerTypology().getId() != null
				&& search.getObj().getBannerTypology().getId() > 0) {
			sb.append(separator).append(alias)
					.append(".bannerTypology.id = :IDTYP ");
			params.put("IDTYP", search.getObj().getBannerTypology().getId());
		}

		// NAME OR DESCRIPTION
		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator + " ( upper(").append(alias)
					.append(".name) LIKE :NAMEPROD ");
			params.put("NAMEPROD", likeParam(search.getObj().getName()
					.toUpperCase()));
			sb.append(" or ").append(" upper(").append(alias)
					.append(".description ) LIKE :DESC").append(") ");
			params.put("DESC", likeParam(search.getObj().getName()
					.toUpperCase()));
		}
	}

	public Banner getFirst() {
		List<Banner> list = getList(new Search<Banner>(Banner.class), 0, 1);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public Banner getRandomByTypology(String typology) {
		Banner banner = (Banner) getEm().createNativeQuery(
				" SELECT * FROM Banner ORDER BY RAND() LIMIT 1")
				.getSingleResult();
		if (banner != null)
			return banner;
		return new Banner();
	}

}
