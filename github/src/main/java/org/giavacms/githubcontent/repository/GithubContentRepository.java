package org.giavacms.githubcontent.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.controller.util.TimeUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.common.model.Search;
import org.giavacms.common.util.StringUtils;

@Named
@Stateless
@LocalBean
public class GithubContentRepository extends
		AbstractPageRepository<GithubContent> {

	private static final long serialVersionUID = 1L;

	/**
	 * criteri di default, comuni a tutti, ma specializzabili da ogni EJB
	 * tramite overriding
	 */

	@Override
	protected void applyRestrictions(Search<GithubContent> search,
			String alias, String separator, StringBuffer sb,
			Map<String, Object> params) {

		// ACTIVE TYPE
		if (true) {
			sb.append(separator).append(alias)
					.append(".richContentType.active = :activeContentType ");
			params.put("activeContentType", true);
			separator = " and ";
		}
		// TAG
		if (search.getObj().getTag() != null
				&& search.getObj().getTag().trim().length() > 0) {
			// try
			// {
			// params.put("TAGNAME",
			// URLEncoder.encode(search.getObj().getTag().trim(), "UTF-8"));
			// sb.append(separator).append(alias).append(".id in ( ");
			// sb.append(" select distinct rt.richContent.id from ").append(Tag.class.getSimpleName())
			// .append(" rt where rt.tagName = :TAGNAME ");
			// sb.append(" ) ");
			// separator = " and ";
			// }
			// catch (UnsupportedEncodingException e)
			{
				String tagName = search.getObj().getTag().trim();
				String tagNameCleaned = StringUtils.clean(
						search.getObj().getTag().trim()).replace('-', ' ');
				boolean likeMatch = false;
				if (!tagName.equals(tagNameCleaned)) {
					likeMatch = true;
				}
				sb.append(separator).append(alias).append(".id in ( ");
				sb.append(" select distinct rt.richContent.id from ")
						.append(Tag.class.getSimpleName())
						.append(" rt where rt.tagName ")
						.append(likeMatch ? "like" : "=").append(" :TAGNAME ");
				sb.append(" ) ");
				params.put("TAGNAME", likeMatch ? likeParam(tagNameCleaned)
						: tagName);
				separator = " and ";
			}
		}

		// TAG LIKE
		if (search.getObj().getTagList().size() > 0) {
			sb.append(separator).append(" ( ");
			for (int i = 0; i < search.getObj().getTagList().size(); i++) {
				sb.append(i > 0 ? " or " : "");

				// da provare quale versione piu' efficiente
				boolean usaJoin = false;
				if (usaJoin) {
					sb.append(alias).append(".id in ( ");
					sb.append(" select distinct rt.richContent.id from ")
							.append(Tag.class.getSimpleName())
							.append(" rt where upper ( rt.tagName ) like :TAGNAME")
							.append(i).append(" ");
					sb.append(" ) ");
				} else {
					sb.append(" upper ( ").append(alias)
							.append(".tags ) like :TAGNAME").append(i)
							.append(" ");
				}

				params.put("TAGNAME" + i, likeParam(search.getObj().getTag()
						.trim().toUpperCase()));
			}
			separator = " and ";
		}

		super.applyRestrictions(search, alias, separator, sb, params);

	}

	@Override
	protected boolean likeSearch(String likeText, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(" ( ");

		sb.append(" upper ( ").append(alias).append(".title ) LIKE :LIKETEXT ");
		sb.append(" or ").append(" upper ( ").append(alias)
				.append(".content ) LIKE :LIKETEXT ");

		sb.append(" ) ");

		params.put("LIKETEXT", StringUtils.clean(likeText));

		return true;
	}

	@Override
	protected GithubContent prePersist(GithubContent n) {
		n.setClone(true);
		if (n.getDate() == null)
			n.setDate(new Date());
		n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
		n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
		return super.prePersist(n);
	}

	@Override
	protected GithubContent preUpdate(GithubContent n) {
		n.setClone(true);
		if (n.getDate() == null)
			n.setDate(new Date());
		n.setDate(TimeUtils.adjustHourAndMinute(n.getDate()));
		n.setContent(HtmlUtils.normalizeHtml(n.getContent()));
		n = super.preUpdate(n);
		return n;
	}

	public GithubContent findLast() {
		GithubContent ret = new GithubContent();
		try {
			ret = (GithubContent) getEm()
					.createQuery(
							"select p from "
									+ GithubContent.class.getSimpleName()
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

	public GithubContent findHighlight() {
		GithubContent ret = new GithubContent();
		try {
			ret = (GithubContent) getEm()
					.createQuery(
							"select p from "
									+ GithubContent.class.getSimpleName()
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
		List<GithubContent> ret = null;
		try {
			ret = (List<GithubContent>) getEm()
					.createQuery(
							"select p from "
									+ GithubContent.class.getSimpleName()
									+ " p where p.id != :ID AND p.highlight = :STATUS")
					.setParameter("ID", id).setParameter("STATUS", true)
					.getResultList();
			if (ret != null) {
				for (GithubContent richContent : ret) {
					richContent.setHighlight(false);
					update(richContent);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String getDefaultOrderBy() {
		return "date desc";
	}

	@Override
	public GithubContent fetch(Object key) {
		try {
			GithubContent githubContent = find(key);
			return githubContent;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<GithubContent> getList(Search<GithubContent> ricerca,
			int startRow, int pageSize) {
		// TODO Auto-generated method stub
		List<GithubContent> list = super.getList(ricerca, startRow, pageSize);
		return list;
	}

}
