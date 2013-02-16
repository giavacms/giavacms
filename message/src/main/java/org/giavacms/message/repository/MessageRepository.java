package org.giavacms.message.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.message.model.Message;

@Named
@Stateless
@LocalBean
public class MessageRepository extends AbstractRepository<Message> {

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
	protected void applyRestrictions(Search<Message> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {

		if (search.getObj().isActive()) {
			sb.append(separator).append(alias).append(".active = :active");
			params.put("active", true);
			separator = " and ";
		}

		if (search.getObj().getSourceKey() != null
				&& !search.getObj().getSourceKey().isEmpty()) {
			sb.append(separator).append(alias)
					.append(".sourceKey = :SOURCEKEY ");
			params.put("SOURCEKEY", search.getObj().getSourceKey());
		}
		if (search.getObj().getSourceType() != null
				&& !search.getObj().getSourceType().isEmpty()) {
			sb.append(separator).append(alias)
					.append(".sourceType = :SOURCETYPE ");
			params.put("SOURCETYPE", search.getObj().getSourceType());
		}
		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator).append(alias).append(".name = :NAME ");
			params.put("NAME", search.getObj().getName());
		}
		if (search.getObj().getEmail() != null
				&& !search.getObj().getEmail().isEmpty()) {
			sb.append(separator).append(alias).append(".email = :EMAIL ");
			params.put("EMAIL", search.getObj().getEmail());
		}

	}

	public List<String> getDistinctType() {
		try {
			List<String> list = getEm().createQuery(
					"select distinct(m.sourceType) from Message m")
					.getResultList();
			return list;
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return new ArrayList<String>();
	}
}
