package org.giavacms.company.repository;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.company.model.Company;

@Named
@Stateless
@LocalBean
public class CompanyRepository extends AbstractRepository<Company> {

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
			Company company = getEm().find(getEntityType(), key);
			if (company != null) {
				company.setActive(false);
				getEm().merge(company);
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return false;
		}
	}

	@Override
	protected void applyRestrictions(Search<Company> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
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

	public Company findPrincipal() {
		Company ret = new Company();
		try {
			ret = (Company) em
					.createQuery(
							"select p from Company p where p.principal = :STATUS ")
					.setParameter("STATUS", true).setMaxResults(1)
					.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ret == null)
			return findLast();
		return ret;
	}

	public Company findLast() {
		Company ret = new Company();
		try {
			ret = (Company) em
					.createQuery("select p from Company p order by p.id desc ")
					.setMaxResults(1).getSingleResult();
			if (ret == null) {
				return null;
			} else {
				return this.fetch(ret.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public void refreshPrincipal(Long id) {
		List<Company> ret = null;
		try {
			ret = (List<Company>) em
					.createQuery(
							"select p from Company p where p.id != :ID AND p.principal = :STATUS")
					.setParameter("ID", id).setParameter("STATUS", true)
					.getResultList();
			if (ret != null) {
				for (Company company : ret) {
					company.setPrincipal(false);
					update(company);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Company fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			return super.fetch(id);
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	@Override
	public Company find(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			return super.find(id);
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

}
