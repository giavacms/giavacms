package org.giavacms.scenario.repository;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.giavacms.base.common.util.HtmlUtils;
import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Product;
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.scenario.model.Scenario;
import org.hibernate.Query;
import org.hibernate.Session;


@Named
@Stateless
@LocalBean
public class ScenarioRepository extends AbstractRepository<Scenario> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Override
	protected EntityManager getEm() {
		// TODO Auto-generated method stub
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
	protected void applyRestrictions(Search<Scenario> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {

		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";

		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator).append(alias).append(".name LIKE :NAME ");
			params.put("NAME", likeParam(search.getObj().getName()));
		}
	}

	@Override
	public Scenario fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			Scenario scenario = find(id);
			for (Document document : scenario.getDocuments()) {
				document.getName();
			}

			for (Image image : scenario.getImages()) {
				image.getName();
			}
			for (Product product : scenario.getProducts()) {
				product.getName();
				for (Image image : product.getImages()) {
					// logger.info("prod: " + product.getName() + " - img:"
					// + image.getFilename());
					image.getName();
					image.getFilename();
				}
			}
			return scenario;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	@Override
	protected Scenario prePersist(Scenario scenario) {
		scenario.setDescription(HtmlUtils.normalizeHtml(scenario
				.getDescription()));
		return scenario;
	}

	@Override
	protected Scenario preUpdate(Scenario scenario) {
		scenario.setDescription(HtmlUtils.normalizeHtml(scenario
				.getDescription()));
		return scenario;
	}

	@Override
	public List<Scenario> getList(Search<Scenario> ricerca, int startRow,
			int pageSize) {
		// TODO Auto-generated method stub
		List<Scenario> list = super.getList(ricerca, startRow, pageSize);
		for (Scenario scenario : list) {
			if (scenario.getImages() != null) {
				for (Image img : scenario.getImages()) {
					img.getId();
					img.getFilename();
					img.getFilePath();
				}
			}
		}
		return list;
	}

	public List<Scenario> loadRandomList(int pageSize) {
		// TODO Auto-generated method stub
		String query = "SELECT e FROM Scenario e ORDER BY RAND()";
		Session session = (Session) getEm().getDelegate();

		List<Scenario> list = session.createQuery(query)
				.setMaxResults(pageSize).list();
		for (Scenario scenario : list) {
			if (scenario.getImages() != null) {
				for (Image img : scenario.getImages()) {
					img.getId();
					img.getFilename();
					img.getFilePath();
				}
			}
		}
		return list;
	}
}
