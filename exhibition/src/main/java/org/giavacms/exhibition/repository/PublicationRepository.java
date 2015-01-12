package org.giavacms.exhibition.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.exhibition.model.Publication;

@Named
@Stateless
@LocalBean
public class PublicationRepository extends AbstractRepository<Publication> {

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
		return "id desc";
	}

	@Override
	protected Publication prePersist(Publication publication) {
		String idTitle = PageUtils.createPageId(publication.getTitle());
		String idFinal = super.testKey(idTitle);
		publication
				.setContent(HtmlUtils.normalizeHtml(publication.getContent()));
		publication.setId(idFinal);
		return publication;
	}

	@Override
	protected Publication preUpdate(Publication publication) {
		publication
				.setContent(HtmlUtils.normalizeHtml(publication.getContent()));
		return super.preUpdate(publication);
	}

	@Override
	protected void applyRestrictions(Search<Publication> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		// TITLE
		if (search.getObj().getTitle() != null
				&& search.getObj().getTitle().trim().length() > 0) {
			sb.append(separator).append(alias).append(".title like :TITLE ");
			params.put("TITLE", likeParam(search.getObj().getTitle()));
		}

		// AUTHOR
		if (search.getObj().getAuthor() != null
				&& !search.getObj().getAuthor().isEmpty()) {
			sb.append(separator).append(alias).append(".author = :AUTHOR ");
			params.put("AUTHOR", search.getObj().getAuthor());
		}

		// EXHIBITION
		if (search.getObj().getExhibition() != null
				&& search.getObj().getExhibition().getId() != null
				&& !search.getObj().getExhibition().getId().isEmpty()) {
			sb.append(separator).append(alias)
					.append(".exhibition.id = :EXHIBITION_ID ");
			params.put("EXHIBITION_ID", search.getObj().getExhibition().getId());
		}
	}

	@Override
	public Publication fetch(Object key) {
		try {
			Publication publication = find(key);
			for (Document document : publication.getDocuments()) {
				document.getName();
			}

			for (Image image : publication.getImages()) {
				image.getName();
			}
			return publication;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean delete(Object key) {
		try {
			Publication publication = getEm().find(getEntityType(), key);
			if (publication != null) {
				publication.setActive(false);
				getEm().merge(publication);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

}
