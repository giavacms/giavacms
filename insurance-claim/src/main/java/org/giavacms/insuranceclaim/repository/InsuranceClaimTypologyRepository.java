package org.giavacms.insuranceclaim.repository;

import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.insuranceclaim.model.InsuranceClaimTypology;


@Named
@Stateless
@LocalBean
public class InsuranceClaimTypologyRepository extends
		AbstractRepository<InsuranceClaimTypology> {

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
	public boolean delete(Object key) {
		try {
			InsuranceClaimTypology insuranceClaimTypology = getEm().find(
					getEntityType(), key);
			if (insuranceClaimTypology != null) {
				insuranceClaimTypology.setActive(false);
				getEm().merge(insuranceClaimTypology);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	protected void applyRestrictions(Search<InsuranceClaimTypology> search,
			String alias, String separator, StringBuffer sb,
			Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator).append(" upper(").append(alias)
					.append(".name ) like :NAME ");
			params.put("NAME", likeParam(search.getObj().getName()
					.toUpperCase()));
		}
	}

}
