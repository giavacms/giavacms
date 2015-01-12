package org.giavacms.picasa.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.picasa.model.Photo;


@Named
@Stateless
@LocalBean
public class PhotoRepository extends AbstractRepository<Photo> {

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
	protected void applyRestrictions(Search<Photo> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		// COLLECTION ID
		if (search.getObj().getCollection().getId() != null
				&& !search.getObj().getCollection().getId().isEmpty()) {
			sb.append(separator).append(alias).append(".collection.id = :ID ");
			params.put("ID", search.getObj().getCollection().getId());
		}
		// TAG
		if (search.getObj().getCollection().getTags() != null
				&& !search.getObj().getCollection().getTags().isEmpty()) {
			sb.append(separator).append(alias)
					.append(".collection.tags LIKE :TAG ");
			params.put("TAG", search.getObj().getCollection().getTags());
		}
	}

}
