package org.giavacms.faq.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.faq.model.Faq;

@Named
@Stateless
@LocalBean
public class FaqRepository extends AbstractRepository<Faq> {

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
		return "date asc";
	}

	@Override
	public Faq fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			Faq product = find(id);
			return product;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	@Override
	protected void applyRestrictions(Search<Faq> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";

		sb.append(separator).append(alias)
				.append(".faqCategory.active = :activeCategory");
		params.put("activeCategory", true);
		separator = " and ";

		// CATEGORY NAME
		if (search.getObj().getFaqCategory() != null
				&& search.getObj().getFaqCategory().getName() != null
				&& search.getObj().getFaqCategory().getName().trim().length() > 0) {
			sb.append(separator).append(alias)
					.append(".faqCategory.name = :NAMECAT ");
			params.put("NAMECAT", search.getObj().getFaqCategory().getName());
		}
		// CATEGORY ID
		if (search.getObj().getFaqCategory() != null
				&& search.getObj().getFaqCategory().getId() != null
				&& search.getObj().getFaqCategory().getId() > 0) {
			sb.append(separator).append(alias)
					.append(".faqCategory.id = :IDCAT ");
			params.put("IDCAT", search.getObj().getFaqCategory().getId());
		}

		// Answer
		if (search.getObj().getAnswer() != null
				&& !search.getObj().getAnswer().isEmpty()) {
			sb.append(separator + " upper(").append(alias)
					.append(".answer) LIKE :ANSWER ");
			params.put("ANSWER", likeParam(search.getObj().getAnswer()
					.toUpperCase()));
		}

		// question
		if (search.getObj().getQuestion() != null
				&& !search.getObj().getQuestion().isEmpty()) {
			sb.append(separator + " upper(").append(alias)
					.append(".question) LIKE :QUESTION ");
			params.put("QUESTION", likeParam(search.getObj().getQuestion()
					.toUpperCase()));
		}
	}

	@Override
	protected Faq prePersist(Faq faq) {
		faq.setAnswer(HtmlUtils.normalizeHtml(faq.getAnswer()));
		faq.setQuestion(HtmlUtils.normalizeHtml(faq.getQuestion()));
		return faq;
	}

	@Override
	protected Faq preUpdate(Faq faq) {
		faq.setAnswer(HtmlUtils.normalizeHtml(faq.getAnswer()));
		faq.setQuestion(HtmlUtils.normalizeHtml(faq.getQuestion()));
		return faq;
	}
}
