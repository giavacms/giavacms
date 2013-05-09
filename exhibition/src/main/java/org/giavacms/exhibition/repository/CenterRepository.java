package org.giavacms.exhibition.repository;

import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.exhibition.model.Center;

@Named
@Stateless
@LocalBean
public class CenterRepository extends AbstractRepository<Center> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Inject
	SubjectRepository subjectRepository;

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
		return "surname asc";
	}

	@Override
	protected Center prePersist(Center center) {
		String idTitle = PageUtils.createPageId(center.getName());
		String idFinal = subjectRepository.testKey(idTitle);
		center.setId(idFinal);
		return center;
	}

	@Override
	protected void applyRestrictions(Search<Center> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		// NAME
		if (search.getObj().getName() != null
				&& search.getObj().getName().trim().length() > 0) {
			sb.append(separator).append(alias).append(".name = :NAME ");
			params.put("NAME", search.getObj().getName());
		}
	}

	@Override
	public boolean delete(Object key) {
		try {
			Center center = getEm().find(getEntityType(), key);
			if (center != null) {
				center.setActive(false);
				getEm().merge(center);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

}
