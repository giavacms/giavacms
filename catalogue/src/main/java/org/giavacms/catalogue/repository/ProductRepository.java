package org.giavacms.catalogue.repository;

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
import org.giavacms.base.repository.AbstractPageRepository;
import org.giavacms.catalogue.model.Product;
import org.giavacms.common.model.Search;

@Named
@Stateless
@LocalBean
public class ProductRepository extends AbstractPageRepository<Product> {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	EntityManager em;

	@Override
	protected String getDefaultOrderBy() {
		return "code asc";
	}

	@Override
	public Product fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			Product product = find(id);
			for (Document document : product.getDocuments()) {
				document.getName();
			}

			for (Image image : product.getImages()) {
				image.getName();
				image.getFilename();
			}
			return product;
		} catch (Exception e) {
			logger.log(Level.SEVERE, null, e);
			return null;
		}
	}

	@Override
	protected void applyRestrictions(Search<Product> search, String alias,
			String separator, StringBuffer sb, Map<String, Object> params) {

		if (true) {
			sb.append(separator).append(alias).append(".active = :active");
			params.put("active", true);
			separator = " and ";
		}

		if (true) {
			sb.append(separator).append(alias)
					.append(".category.active = :activeCategory");
			params.put("activeCategory", true);
			separator = " and ";
		}

		// CATEGORY NAME
		if (search.getObj().getCategory() != null
				&& search.getObj().getCategory().getTitle() != null
				&& search.getObj().getCategory().getTitle().trim().length() > 0) {
			sb.append(separator).append(alias)
					.append(".category.title = :NAMECAT ");
			params.put("NAMECAT", search.getObj().getCategory().getTitle());
		}

		// CATEGORY ID
		if (search.getObj().getCategory() != null
				&& search.getObj().getCategory().getId() != null
				&& search.getObj().getCategory().getId().trim().length() > 0) {
			sb.append(separator).append(alias).append(".category.id = :IDCAT ");
			params.put("IDCAT", search.getObj().getCategory().getId());
		}

		// CODE
		if (search.getObj().getCode() != null
				&& !search.getObj().getCode().isEmpty()) {
			sb.append(separator).append(alias).append(".code = :CODE ");
			params.put("CODE", search.getObj().getCode());
		}

		// NAME OR DESCRIPTION
		if (search.getObj().getTitle() != null
				&& !search.getObj().getTitle().isEmpty()) {
			sb.append(separator + " ( upper(").append(alias)
					.append(".title) LIKE :NAMEPROD ");
			params.put("NAMEPROD", likeParam(search.getObj().getTitle()
					.toUpperCase()));
			sb.append(" or ").append(" upper(").append(alias)
					.append(".description ) LIKE :DESC").append(") ");
			params.put("DESC", likeParam(search.getObj().getTitle()
					.toUpperCase()));
		}
	}

	@Override
	public List<Product> getList(Search<Product> ricerca, int startRow,
			int pageSize) {
		List<Product> list = super.getList(ricerca, startRow, pageSize);
		for (Product product : list) {
			if (product.getImages() != null) {
				for (Image img : product.getImages()) {
					img.getId();
					img.getFilename();
					img.getFilePath();
				}
			}
		}
		return list;
	}

	@Override
	protected Product prePersist(Product n) {
		n.setClone(true);
		n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
		return n;
	}

	@Override
	protected Product preUpdate(Product n) {
		n.setClone(true);
		n.setDescription(HtmlUtils.normalizeHtml(n.getDescription()));
		return n;
	}
}
