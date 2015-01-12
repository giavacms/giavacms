package org.giavacms.exhibition.repository;

import java.util.ArrayList;
import java.util.Iterator;
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
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.exhibition.model.Exhibition;

@Named
@Stateless
@LocalBean
public class ExhibitionRepository extends AbstractRepository<Exhibition> {

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
		return "date desc";
	}

	@Override
	protected Exhibition prePersist(Exhibition exhibition) {
		String idTitle = PageUtils.createPageId(exhibition.getName());
		String idFinal = super.testKey(idTitle);
		exhibition.setDescription(HtmlUtils.normalizeHtml(exhibition
				.getDescription()));
		exhibition.setId(idFinal);
		return exhibition;
	}

	@Override
	protected Exhibition preUpdate(Exhibition exhibition) {
		exhibition.setDescription(HtmlUtils.normalizeHtml(exhibition
				.getDescription()));
		return super.preUpdate(exhibition);
	}

	@Override
	protected void applyRestrictions(Search<Exhibition> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		// ID
		if (search.getObj().getId() != null
				&& search.getObj().getId().trim().length() > 0) {
			sb.append(separator).append(alias).append(".id = :ID ");
			params.put("ID", search.getObj().getId());
		}
		// NAME
		if (search.getObj().getName() != null
				&& search.getObj().getName().trim().length() > 0) {
			sb.append(separator).append(" upper(" + alias)
					.append(".name) like :NAME ");
			params.put("NAME", likeParam(search.getObj().getName()));
		}
		// YEAR
		if (search.getObj().getYear() != null
				&& !search.getObj().getYear().isEmpty()) {
			sb.append(separator).append(alias).append(".year = :YEAR ");
			params.put("YEAR", search.getObj().getYear());
		}
	}

	public List<Exhibition> getAll() {

		List<Exhibition> exhibitions = new ArrayList<Exhibition>();
		Iterator<Object[]> results = getEm()
				.createNativeQuery(
						"SELECT E.id, E.name FROM `Exhibition` E WHERE E.active = :ACTIVE order by E.date DESC ")
				.setParameter("ACTIVE", true).getResultList().iterator();
		while (results.hasNext()) {
			Object[] row = results.next();
			int i = 0;
			String id = (String) row[i];
			i++;
			String name = (String) row[i];
			Exhibition l = new Exhibition();
			l.setId(id);
			l.setName(name);
			exhibitions.add(l);

		}
		return exhibitions;
	}

	@Override
	public boolean delete(Object key) {
		try {
			Exhibition exhibition = getEm().find(getEntityType(), key);
			if (exhibition != null) {
				exhibition.setActive(false);
				getEm().merge(exhibition);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public Exhibition getLatest() {
		Search<Exhibition> search = new Search<Exhibition>(Exhibition.class);
		List<Exhibition> list = getList(search, 0, 1);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

}
