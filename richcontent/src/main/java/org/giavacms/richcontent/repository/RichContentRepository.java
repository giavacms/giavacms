package org.giavacms.richcontent.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.controller.util.TimeUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.model.type.RichContentType;

@Named
@Stateless
@LocalBean
public class RichContentRepository extends AbstractPageRepository<RichContent> {

	private static final long serialVersionUID = 1L;

	/**
	 * criteri di default, comuni a tutti, ma specializzabili da ogni EJB
	 * tramite overriding
	 */

	@Override
	protected void applyRestrictions(Search<RichContent> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {

		if (true) {
			sb.append(separator).append(alias).append(".active = :active");
			params.put("active", true);
			separator = " and ";
		}
		
		if (search.getObj().getRichContentType() != null
				&& search.getObj().getRichContentType().getName() != null
				&& search.getObj().getRichContentType().getName().length() > 0) {
			sb.append(separator).append(alias)
					.append(".richContentType.name = :NAMETYPE ");
			params.put("NAMETYPE", search.getObj().getRichContentType()
					.getName());
		}
		if (search.getObj().getRichContentType() != null
				&& search.getObj().getRichContentType().getId() != null) {
			sb.append(separator).append(alias)
					.append(".richContentType.id = :IDTYPE ");
			params.put("IDTYPE", search.getObj().getRichContentType().getId());
		}

		if (search.getObj().getTitle() != null
				&& !search.getObj().getTitle().isEmpty()) {
			sb.append(separator + " (");

			// sb.append(alias).append(".title LIKE :TITLENEWS ");
			sb.append(alias).append(".id in ( select rcp.id from ")
					.append(Page.class.getSimpleName())
					.append(" rcp where rcp.title like = :TITLENEWS ) ");
			params.put("TITLENEWS", likeParam(search.getObj().getTitle()
					.toUpperCase()));

			sb.append(" or ").append(alias)
					.append(".content LIKE :CONTENTNEWS");
			params.put("CONTENTNEWS", likeParam(search.getObj().getTitle()
					.toUpperCase()));
			sb.append(" ) ");
		}
	}

	@Override
	protected RichContent prePersist(RichContent n) {
		// String idTitle = PageUtils.createPageId(n.getTitle());
		// String idFinal = testKey(idTitle);
		// n.setId(idFinal);
		n.setClone(true);
		if (n.getDate() == null)
			n.setDate(new Date());
		if (n.getRichContentType() != null
				&& n.getRichContentType().getId() != null)
			n.setRichContentType(getEm().find(RichContentType.class,
					n.getRichContentType().getId()));
		if (n.getDocuments() != null && n.getDocuments().size() == 0) {
			n.setDocuments(null);
		}
		if (n.getImages() != null && n.getImages().size() == 0) {
			n.setImages(null);
		}
		n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
		n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
		return super.prePersist(n);
	}

	@Override
	protected RichContent preUpdate(RichContent n) {
		n.setClone(true);
		if (n.getDate() == null)
			n.setDate(new Date());
		if (n.getRichContentType() != null
				&& n.getRichContentType().getId() != null)
			n.setRichContentType(getEm().find(RichContentType.class,
					n.getRichContentType().getId()));
		if (n.getDocuments() != null && n.getDocuments().size() == 0) {
			n.setDocuments(null);
		}
		if (n.getImages() != null && n.getImages().size() == 0) {
			n.setImages(null);
		}
		n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
		n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
		n = super.preUpdate(n);
		return n;
	}

	public RichContent findLast() {
		RichContent ret = new RichContent();
		try {
			ret = (RichContent) getEm()
					.createQuery(
							"select p from "
									+ RichContent.class.getSimpleName()
									+ " p order by p.date desc ")
					.setMaxResults(1).getSingleResult();
			if (ret == null) {
				return null;
			} else {
				return this.fetch(ret.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public RichContent findHighlight() {
		RichContent ret = new RichContent();
		try {
			ret = (RichContent) getEm()
					.createQuery(
							"select p from "
									+ RichContent.class.getSimpleName()
									+ " p where p.highlight = :STATUS ")
					.setParameter("STATUS", true).setMaxResults(1)
					.getSingleResult();
			for (Document document : ret.getDocuments()) {
				document.getName();
			}

			for (Image image : ret.getImages()) {
				image.getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ret == null)
			return findLast();
		return ret;
	}

	@SuppressWarnings("unchecked")
	public void refreshEvidenza(String id) {
		List<RichContent> ret = null;
		try {
			ret = (List<RichContent>) getEm()
					.createQuery(
							"select p from "
									+ RichContent.class.getSimpleName()
									+ " p where p.id != :ID AND p.highlight = :STATUS")
					.setParameter("ID", id).setParameter("STATUS", true)
					.getResultList();
			if (ret != null) {
				for (RichContent richContent : ret) {
					richContent.setHighlight(false);
					update(richContent);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Image findHighlightImage() {
		try {
			List<RichContent> nl = getEm()
					.createQuery(
							"select p from "
									+ RichContent.class.getSimpleName()
									+ " p where p.highlight = :STATUS")
					.setParameter("STATUS", true).getResultList();
			if (nl == null || nl.size() == 0 || nl.get(0).getImages() == null
					|| nl.get(0).getImages().size() == 0) {
				return null;
			}
			return nl.get(0).getImages().get(0);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected String getDefaultOrderBy() {
		return "date desc";
	}

	@Override
	public RichContent fetch(Object key) {
		try {
			RichContent richContent = find(key);
			for (Document document : richContent.getDocuments()) {
				document.getName();
			}

			for (Image image : richContent.getImages()) {
				image.getName();
			}
			return richContent;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	public RichContent getLast(String category) {
		Search<RichContent> r = new Search<RichContent>(RichContent.class);
		r.getObj().getRichContentType().setName(category);
		List<RichContent> list = getList(r, 0, 1);
		if (list != null && list.size() > 0) {
			RichContent ret = list.get(0);
			for (Document document : ret.getDocuments()) {
				document.getName();
			}

			for (Image image : ret.getImages()) {
				image.getName();
			}
			return ret;
		}
		return new RichContent();
	}

	@Override
	public List<RichContent> getList(Search<RichContent> ricerca, int startRow,
			int pageSize) {
		// TODO Auto-generated method stub
		List<RichContent> list = super.getList(ricerca, startRow, pageSize);
		for (RichContent richContent : list) {
			if (richContent.getImages() != null) {
				for (Image img : richContent.getImages()) {
					img.getId();
					img.getFilename();
					img.getFilePath();
				}
			}
			if (richContent.getDocuments() != null) {
				for (Document doc : richContent.getDocuments()) {
					doc.getId();
					doc.getFilename();
					doc.getType();
				}
			}
		}
		return list;
	}

}
