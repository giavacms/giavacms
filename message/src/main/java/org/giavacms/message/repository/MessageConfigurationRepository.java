package org.giavacms.message.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.message.model.MessageConfiguration;

@Named
@Stateless
@LocalBean
public class MessageConfigurationRepository extends
		AbstractRepository<MessageConfiguration> {

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
		return "data desc";
	}

	public MessageConfiguration load() {
		MessageConfiguration c = null;
		try {
			c = find(1L);
		} catch (Exception e) {
		}
		if (c == null) {
			c = new MessageConfiguration();
			c.setApprove(false);
			persist(c);
		}
		return c;
	}

}
