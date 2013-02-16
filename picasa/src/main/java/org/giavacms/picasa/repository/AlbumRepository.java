package org.giavacms.picasa.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.picasa.model.Album;


@Named
@Stateless
@LocalBean
public class AlbumRepository extends AbstractRepository<Album> {

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
		return "id asc";
	}

	@Override
	protected void applyRestrictions(Search<Album> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		if (search.getObj().getTitle() != null
				&& !search.getObj().getTitle().isEmpty()) {
			sb.append(separator).append(" upper(").append(alias)
					.append(".title ) like :NAME ");
			params.put("NAME", likeParam(search.getObj().getTitle()
					.toUpperCase()));
		}
	}

}
