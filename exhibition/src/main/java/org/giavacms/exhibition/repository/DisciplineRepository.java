package org.giavacms.exhibition.repository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.exhibition.model.Discipline;

@Named
@Stateless
@LocalBean
public class DisciplineRepository extends AbstractRepository<Discipline> {

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
	protected Discipline prePersist(Discipline discipline) {
		String idTitle = PageUtils.createPageId(discipline.getName());
		String idFinal = super.testKey(idTitle);
		discipline.setId(idFinal);
		return discipline;
	}

}
