package org.giavacms.insuranceclaim.repository;

import java.util.List;
import java.util.Map;

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
import org.giavacms.insuranceclaim.model.InsuranceClaimProduct;

@Named
@Stateless
@LocalBean
public class InsuranceClaimProductRepository extends
		AbstractRepository<InsuranceClaimProduct> {

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
	public InsuranceClaimProduct fetch(Object key) {
		try {
			Long id;
			if (key instanceof String) {
				id = Long.valueOf((String) key);
			} else if (key instanceof Long) {
				id = (Long) key;
			} else {
				throw new Exception("key type is not correct!!");
			}
			InsuranceClaimProduct insuranceClamsProduct = find(id);
			for (Document document : insuranceClamsProduct.getDocuments()) {
				document.getName();
				document.getFilename();
				document.getDescription();
			}

			for (Image image : insuranceClamsProduct.getImages()) {
				image.getName();
				image.getFilename();
				image.getDescription();
			}
			return insuranceClamsProduct;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected void applyRestrictions(Search<InsuranceClaimProduct> search,
			String alias, String separator, StringBuffer sb,
			Map<String, Object> params) {
		sb.append(separator).append(alias).append(".active = :active");
		params.put("active", true);
		separator = " and ";

		// CATEGORY NAME
		if (search.getObj().getInsuranceClaimCategory() != null
				&& search.getObj().getInsuranceClaimCategory().getName() != null
				&& search.getObj().getInsuranceClaimCategory().getName().trim()
						.length() > 0) {
			sb.append(separator).append(alias)
					.append(".insuranceClaimCategory.name = :NAMECAT ");
			params.put("NAMECAT", search.getObj().getInsuranceClaimCategory()
					.getName());
		}
		// CATEGORY ID
		if (search.getObj().getInsuranceClaimCategory() != null
				&& search.getObj().getInsuranceClaimCategory().getId() != null
				&& search.getObj().getInsuranceClaimCategory().getId() > 0) {
			sb.append(separator).append(alias)
					.append(".insuranceClaimCategory.id = :IDCAT ");
			params.put("IDCAT", search.getObj().getInsuranceClaimCategory()
					.getId());
		}
		// CODE
		if (search.getObj().getCode() != null
				&& !search.getObj().getCode().isEmpty()) {
			sb.append(separator).append(alias).append(".code = :CODE ");
			params.put("CODE", search.getObj().getCode());
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
	public List<InsuranceClaimProduct> getList(
			Search<InsuranceClaimProduct> ricerca, int startRow, int pageSize) {
		List<InsuranceClaimProduct> list = super.getList(ricerca, startRow,
				pageSize);
		for (InsuranceClaimProduct insuranceClaimProduct : list) {
			if (insuranceClaimProduct.getImages() != null) {
				for (Image img : insuranceClaimProduct.getImages()) {
					img.getId();
					img.getFilename();
					img.getFilePath();
				}
			}

			if (insuranceClaimProduct.getDocuments() != null) {
				for (Document doc : insuranceClaimProduct.getDocuments()) {
					doc.getId();
					doc.getFilename();
					doc.getType();
				}
			}
		}
		return list;
	}

	@Override
	protected InsuranceClaimProduct prePersist(
			InsuranceClaimProduct insuranceClamsProduct) {
		insuranceClamsProduct.setDescription(HtmlUtils
				.normalizeHtml(insuranceClamsProduct.getDescription()));
		return insuranceClamsProduct;
	}

	@Override
	protected InsuranceClaimProduct preUpdate(
			InsuranceClaimProduct insuranceClamsProduct) {
		insuranceClamsProduct.setDescription(HtmlUtils
				.normalizeHtml(insuranceClamsProduct.getDescription()));
		return insuranceClamsProduct;
	}

	public InsuranceClaimProduct getFirst() {
		List<InsuranceClaimProduct> list = getList(
				new Search<InsuranceClaimProduct>(InsuranceClaimProduct.class),
				0, 1);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
}
