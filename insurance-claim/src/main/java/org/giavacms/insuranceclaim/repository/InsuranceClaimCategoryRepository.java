package org.giavacms.insuranceclaim.repository;

import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.insuranceclaim.model.InsuranceClaimCategory;
import org.giavacms.insuranceclaim.model.InsuranceClaimProduct;


@Named
@Stateless
@LocalBean
public class InsuranceClaimCategoryRepository extends
		AbstractRepository<InsuranceClaimCategory> {

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
		return "orderNum asc";
	}

	@Override
	public boolean delete(Object key) {
		try {
			InsuranceClaimCategory insuranceClaimCategory = getEm().find(
					getEntityType(), key);
			if (insuranceClaimCategory != null) {
				insuranceClaimCategory.setActive(false);
				getEm().merge(insuranceClaimCategory);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	protected void applyRestrictions(Search<InsuranceClaimCategory> search,
			String alias, String separator, StringBuffer sb,
			Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";
		// NAME
		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator).append(" upper(").append(alias)
					.append(".name ) like :NAME ");
			params.put("NAME", likeParam(search.getObj().getName()
					.toUpperCase()));
		}
		// NAME_TIPOLOGIA
		if (search.getObj().getInsuranceClaimTypology() != null
				&& search.getObj().getInsuranceClaimTypology().getName() != null
				&& !search.getObj().getInsuranceClaimTypology().getName()
						.isEmpty()) {
			sb.append(separator).append(" upper(").append(alias)
					.append(".insuranceClaimTypology.name ) like :NAME_TIP ");
			params.put("NAME_TIP", likeParam(search.getObj()
					.getInsuranceClaimTypology().getName().toUpperCase()));
		}
	}

	public InsuranceClaimCategory findByName(String name) {
		Search<InsuranceClaimCategory> r = new Search<InsuranceClaimCategory>(
				InsuranceClaimCategory.class);
		r.getObj().setName(name);
		List<InsuranceClaimCategory> list = getList(r, 0, 0);
		if (list != null && list.size() > 0) {
			logger.info("ci sono con name: " + name);
			return list.get(0);
		}
		logger.info("NON ci sono con name: " + name);
		return null;
	}

	public List<InsuranceClaimCategory> fetchAll(String tipologia) {
		Search<InsuranceClaimCategory> r = new Search<InsuranceClaimCategory>(
				InsuranceClaimCategory.class);
		r.getObj().getInsuranceClaimTypology().setName(tipologia);
		r.getObj().getInsuranceClaimTypology().setName(tipologia);
		List<InsuranceClaimCategory> list = getList(r, 0, 0);
		for (InsuranceClaimCategory insuranceClaimCategory : list) {
			for (InsuranceClaimProduct prod : insuranceClaimCategory
					.getInsuranceClaimProducts()) {
				prod.getName();
				prod.getDescription();
			}
		}
		return list;
	}

	@Override
	public InsuranceClaimCategory fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			InsuranceClaimCategory insuranceClaimCategory = super.fetch(id);
			return insuranceClaimCategory;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	
}
