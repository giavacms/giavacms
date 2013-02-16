package org.giavacms.richnews.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.controller.util.TimeUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.richnews.model.RichNews;
import org.giavacms.richnews.model.type.RichNewsType;


@Named
@Stateless
@LocalBean
public class RichNewsRepository extends AbstractRepository<RichNews> {

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
	public boolean delete(Object key) {
		try {
			RichNews richNews = getEm().find(getEntityType(), key);
			if (richNews != null) {
				richNews.setActive(false);
				getEm().merge(richNews);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

	/**
	 * criteri di default, comuni a tutti, ma specializzabili da ogni EJB
	 * tramite overriding
	 */

	@Override
	protected void applyRestrictions(Search<RichNews> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {

		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";

		if (search.getObj().getRichNewsType() != null
				&& search.getObj().getRichNewsType().getName() != null
				&& search.getObj().getRichNewsType().getName().length() > 0) {
			sb.append(separator).append(alias)
					.append(".richNewsType.name = :NAMETYPE ");
			params.put("NAMETYPE", search.getObj().getRichNewsType().getName());
		}
		if (search.getObj().getRichNewsType() != null
				&& search.getObj().getRichNewsType().getId() != null) {
			sb.append(separator).append(alias)
					.append(".richNewsType.id = :IDTYPE ");
			params.put("IDTYPE", search.getObj().getRichNewsType().getId());
		}

		if (search.getObj().getTitle() != null
				&& !search.getObj().getTitle().isEmpty()) {
			sb.append(separator + " (").append(alias)
					.append(".title LIKE :TITLENEWS ");
			params.put("TITLENEWS", likeParam(search.getObj().getTitle()));
			sb.append(" or ").append(alias)
					.append(".content LIKE :CONTENTNEWS");
			params.put("CONTENTNEWS", likeParam(search.getObj().getTitle()));
			sb.append(" ) ");
		}
	}

	@Override
	protected RichNews prePersist(RichNews n) {
		String idTitle = PageUtils.createPageId(n.getTitle());
		String idFinal = testKey(idTitle);
		n.setId(idFinal);
		if (n.getDate() == null)
			n.setDate(new Date());
		if (n.getRichNewsType() != null && n.getRichNewsType().getId() != null)
			n.setRichNewsType(getEm().find(RichNewsType.class,
					n.getRichNewsType().getId()));
		if (n.getDocuments() != null && n.getDocuments().size() == 0) {
			n.setDocuments(null);
		}
		if (n.getImages() != null && n.getImages().size() == 0) {
			n.setImages(null);
		}
		n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
		n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
		return n;
	}

	@Override
	protected RichNews preUpdate(RichNews n) {
		if (n.getDate() == null)
			n.setDate(new Date());
		if (n.getRichNewsType() != null && n.getRichNewsType().getId() != null)
			n.setRichNewsType(getEm().find(RichNewsType.class,
					n.getRichNewsType().getId()));
		if (n.getDocuments() != null && n.getDocuments().size() == 0) {
			n.setDocuments(null);
		}
		if (n.getImages() != null && n.getImages().size() == 0) {
			n.setImages(null);
		}
		n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
		n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
		return n;
	}

	public RichNews findLast() {
		RichNews ret = new RichNews();
		try {
			ret = (RichNews) em
					.createQuery(
							"select p from RichNews p order by p.date desc ")
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

	public RichNews findHighlight() {
		RichNews ret = new RichNews();
		try {
			ret = (RichNews) em
					.createQuery(
							"select p from RichNews p where p.highlight = :STATUS ")
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
		List<RichNews> ret = null;
		try {
			ret = (List<RichNews>) em
					.createQuery(
							"select p from RichNews p where p.id != :ID AND p.highlight = :STATUS")
					.setParameter("ID", id).setParameter("STATUS", true)
					.getResultList();
			if (ret != null) {
				for (RichNews richNews : ret) {
					richNews.setHighlight(false);
					update(richNews);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public Image findHighlightImage() {
		try {
			List<RichNews> nl = em
					.createQuery(
							"select p from RichNews p where p.highlight = :STATUS")
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
	public RichNews fetch(Object key) {
		try {
			RichNews richNews = find(key);
			for (Document document : richNews.getDocuments()) {
				document.getName();
			}

			for (Image image : richNews.getImages()) {
				image.getName();
			}
			return richNews;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	public RichNews getLast(String category) {
		Search<RichNews> r = new Search<RichNews>(RichNews.class);
		r.getObj().getRichNewsType().setName(category);
		List<RichNews> list = getList(r, 0, 1);
		if (list != null && list.size() > 0) {
			RichNews ret = list.get(0);
			for (Document document : ret.getDocuments()) {
				document.getName();
			}

			for (Image image : ret.getImages()) {
				image.getName();
			}
			return ret;
		}
		return new RichNews();
	}

	@Override
	public List<RichNews> getList(Search<RichNews> ricerca, int startRow,
			int pageSize) {
		// TODO Auto-generated method stub
		List<RichNews> list = super.getList(ricerca, startRow, pageSize);
		for (RichNews richNews : list) {
			if (richNews.getImages() != null) {
				for (Image img : richNews.getImages()) {
					img.getId();
					img.getFilename();
					img.getFilePath();
				}
			}
			if (richNews.getDocuments() != null) {
				for (Document doc : richNews.getDocuments()) {
					doc.getId();
					doc.getFilename();
					doc.getType();
				}
			}
		}
		return list;
	}

}
