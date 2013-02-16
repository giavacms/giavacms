package org.giavacms.insuranceclaim.controller.request;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.insuranceclaim.model.InsuranceClaimCategory;
import org.giavacms.insuranceclaim.repository.InsuranceClaimCategoryRepository;


@Named
@RequestScoped
public class InsuranceClaimCategoryRequestController extends
		AbstractRequestController<InsuranceClaimCategory> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TIPOLOGIA = "tipologia";
	public static final String CATEGORIA = "categoria";
	public static final String SEARCH = "q";
	public static final String[] PARAM_NAMES = new String[] { TIPOLOGIA, SEARCH };
	public static final String ID_PARAM = "id";
	public static final String CURRENT_PAGE_PARAM = "start";

	@Inject
	@OwnRepository(InsuranceClaimCategoryRepository.class)
	InsuranceClaimCategoryRepository insuranceClaimCategoryRepository;

	public InsuranceClaimCategoryRequestController() {
		super();
	}

	@Override
	protected void init() {
		super.init();
	}

	public List<InsuranceClaimCategory> getCategorie(String tipologia) {
		Search<InsuranceClaimCategory> r = new Search<InsuranceClaimCategory>(
				InsuranceClaimCategory.class);
		r.getObj().getInsuranceClaimTypology().setName(tipologia);
		return insuranceClaimCategoryRepository.getList(r, 0, 0);
	}

	@Override
	public List<InsuranceClaimCategory> loadPage(int startRow, int pageSize) {
		Search<InsuranceClaimCategory> r = new Search<InsuranceClaimCategory>(
				InsuranceClaimCategory.class);
		r.getObj().setName(getParams().get(SEARCH));
		r.getObj().getInsuranceClaimTypology()
				.setName(getParams().get(TIPOLOGIA));
		return insuranceClaimCategoryRepository.getList(r, startRow, pageSize);
	}

	@Override
	public int totalSize() {
		// siamo all'interno della stessa richiesta per servire la quale è
		// avvenuta la postconstruct
		Search<InsuranceClaimCategory> r = new Search<InsuranceClaimCategory>(
				InsuranceClaimCategory.class);
		r.getObj().getInsuranceClaimTypology()
				.setName(getParams().get(TIPOLOGIA));
		r.getObj().setName(getParams().get(SEARCH));
		return insuranceClaimCategoryRepository.getListSize(r);
	}

	@Override
	public String[] getParamNames() {
		return PARAM_NAMES;
	}

	@Override
	public String getIdParam() {
		return ID_PARAM;
	}

	@Override
	public String getCurrentPageParam() {
		return CURRENT_PAGE_PARAM;
	}

	public boolean isScheda() {
		return getElement() != null && getElement().getId() != null;
	}

	public String viewElement(Long id) {
		setElement(insuranceClaimCategoryRepository.fetch(id));
		return viewPage();
	}

	public InsuranceClaimCategory getElementByName() {
		if (getParams().get(CATEGORIA) != null
				&& !getParams().get(CATEGORIA).isEmpty()) {
			logger.info("CATEGORIA: " + getParams().get(CATEGORIA));
			return insuranceClaimCategoryRepository.findByName(getParams().get(
					CATEGORIA));
		}
		return new InsuranceClaimCategory();
	}

	public List<InsuranceClaimCategory> getAllCategories(String tipologia) {
		return insuranceClaimCategoryRepository.fetchAll(tipologia);
	}
}
