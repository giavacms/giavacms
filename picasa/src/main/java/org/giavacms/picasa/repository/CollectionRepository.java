package org.giavacms.picasa.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.picasa.model.Collection;
import org.giavacms.picasa.model.Photo;

@Named
@Stateless
@LocalBean
public class CollectionRepository extends AbstractRepository<Collection> {

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
	protected Collection prePersist(Collection collection) {
		// closeHtmlTags(page);
		String idByName = PageUtils.createPageId(collection.getName());
		String idFinal = super.testKey(idByName);
		collection.setId(idFinal);
		return collection;
	}

	@Override
	public Collection fetch(Object key) {
		try {
			Collection collection = find(key);
			for (Photo photo : collection.getPhotos()) {
				photo.getImageUrl();
			}
			return collection;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean delete(Object key) {
		try {
			Collection collection = find(key);
			collection.setActive(false);
			update(collection);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	protected void applyRestrictions(Search<Collection> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		// ID
		if (search.getObj().getId() != null
				&& !search.getObj().getId().isEmpty()) {
			sb.append(separator).append(alias).append(".id = :ID ");
			params.put("ID", search.getObj().getId());
		}

		// NAME
		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator + " ( upper(").append(alias)
					.append(".name) LIKE :NAME ");
			params.put("NAME", likeParam(search.getObj().getName()
					.toUpperCase()));
		}
	}

}
