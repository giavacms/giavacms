package org.giavacms.insuranceclaim.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.insuranceclaim.model.InsuranceClaimConfiguration;


@Named
@Stateless
@LocalBean
public class InsuranceClaimConfigurationRepository extends
		AbstractRepository<InsuranceClaimConfiguration> {

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
		return "id asc";
	}

	public InsuranceClaimConfiguration load() {
		InsuranceClaimConfiguration c = null;
		try {
			c = find(1L);
		} catch (Exception e) {
		}
		if (c == null) {
			c = new InsuranceClaimConfiguration();
			c.setResize(false);
			c.setMaxWidthOrHeight(0);
			persist(c);
		}
		return c;
	}

}
