package org.giavacms.exhibition.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.exhibition.model.Association;

@Named
@Stateless
@LocalBean
public class AssociationRepository extends AbstractRepository<Association> {

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
	protected Association prePersist(Association association) {
		String idTitle = PageUtils.createPageId(association.getName());
		String idFinal = subjectRepository.testKey(idTitle);
		association.setId(idFinal);
		return association;
	}

	@Override
	protected void applyRestrictions(Search<Association> search, String alias,
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
			Association association = getEm().find(getEntityType(), key);
			if (association != null) {
				association.setActive(false);
				getEm().merge(association);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

}
