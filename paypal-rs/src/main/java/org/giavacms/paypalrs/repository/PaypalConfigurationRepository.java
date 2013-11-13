package org.giavacms.paypalrs.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.paypalrs.model.PaypalConfiguration;


@Named
@Stateless
@LocalBean
public class PaypalConfigurationRepository extends
		AbstractRepository<PaypalConfiguration> {

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

	public PaypalConfiguration load() {
		PaypalConfiguration c = null;
		try {
			c = find(1L);
		} catch (Exception e) {
		}
		if (c == null) {
			c = new PaypalConfiguration();
			persist(c);
		}
		return c;
	}

}
