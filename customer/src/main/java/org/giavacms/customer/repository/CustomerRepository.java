package org.giavacms.customer.repository;

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
import org.giavacms.common.model.Search;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.customer.model.Customer;

@Named
@Stateless
@LocalBean
public class CustomerRepository extends AbstractRepository<Customer> {

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
		return "listOrder asc";
	}

	@Override
	public Customer fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			Customer customer = find(id);
			for (Document document : customer.getDocuments()) {
				document.getName();
			}

			for (Image image : customer.getImages()) {
				image.getName();
				image.getFilename();
			}
			return customer;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void applyRestrictions(Search<Customer> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";

		sb.append(separator).append(alias)
				.append(".category.active = :activeCategory");
		params.put("activeCategory", true);
		separator = " and ";

		// CATEGORY NAME
		if (search.getObj().getCategory() != null
				&& search.getObj().getCategory().getName() != null
				&& search.getObj().getCategory().getName().trim().length() > 0) {
			sb.append(separator).append(alias)
					.append(".category.name = :NAMECAT ");
			params.put("NAMECAT", search.getObj().getCategory().getName());
		}
		// CATEGORY ID
		if (search.getObj().getCategory() != null
				&& search.getObj().getCategory().getId() != null
				&& search.getObj().getCategory().getId() > 0) {
			sb.append(separator).append(alias).append(".category.id = :IDCAT ");
			params.put("IDCAT", search.getObj().getCategory().getId());
		}
		// NAME OR DESCRIPTION
		if (search.getObj().getName() != null
				&& !search.getObj().getName().isEmpty()) {
			sb.append(separator + " ( upper(").append(alias)
					.append(".name) LIKE :NAMEPROD ");
			params.put("NAMEPROD", likeParam(search.getObj().getName()
					.toUpperCase()));
			sb.append(" or ").append(" upper(").append(alias)
					.append(".description ) LIKE :DESC").append(") ");
			params.put("DESC", likeParam(search.getObj().getName()
					.toUpperCase()));
		}
	}

	@Override
	public List<Customer> getList(Search<Customer> ricerca, int startRow,
			int pageSize) {
		List<Customer> list = super.getList(ricerca, startRow, pageSize);
		for (Customer customer : list) {
			if (customer.getImages() != null) {
				for (Image img : customer.getImages()) {
					img.getId();
					img.getFilename();
					img.getFilePath();
				}
			}
		}
		return list;
	}

	@Override
	protected Customer prePersist(Customer customer) {
		customer.setDescription(HtmlUtils.normalizeHtml(customer.getDescription()));
		return customer;
	}

	@Override
	protected Customer preUpdate(Customer customer) {
		customer.setDescription(HtmlUtils.normalizeHtml(customer.getDescription()));
		return customer;
	}
}
