package org.giavacms.catalogue.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.catalogue.repository.ProductRepository;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;


@Named
@RequestScoped
public class ProductRequestController extends
		AbstractRequestController<Product> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CATEGORIA = "categoria";
	public static final String SEARCH = "q";
	public static final String[] PARAM_NAMES = new String[] { CATEGORIA, SEARCH };
	public static final String ID_PARAM = "id";
	public static final String CURRENT_PAGE_PARAM = "start";

	@Inject
	@OwnRepository(ProductRepository.class)
	ProductRepository productRepository;

	@Inject
	CategoryRepository categoryRepository;

	public ProductRequestController() {
		super();
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public List<Product> loadPage(int startRow, int pageSize) {
		Search<Product> r = new Search<Product>(Product.class);
		r.getObj().setTitle(getParams().get(SEARCH));
		r.getObj().getCategory().setTitle(getParams().get(CATEGORIA));
		return productRepository.getList(r, startRow, pageSize);
	}

	@Override
	public int totalSize() {
		// siamo all'interno della stessa richiesta per servire la quale è
		// avvenuta la postconstruct
		Search<Product> r = new Search<Product>(Product.class);
		r.getObj().getCategory().setTitle(getParams().get(CATEGORIA));
		r.getObj().setTitle(getParams().get(SEARCH));
		return productRepository.getListSize(r);
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
		setElement(productRepository.fetch(id));
		return viewPage();
	}

	public List<String> getProductCategories() {
		Search<Category> r = new Search<Category>(Category.class);
		List<Category> list = categoryRepository.getList(r, 0, 0);
		List<String> l = new ArrayList<String>();
		for (Category rnt : list) {
			l.add(rnt.getTitle());
		}
		return l;
	}

	public List<Category> getAllCategories() {
		Search<Category> r = new Search<Category>(Category.class);
		List<Category> l = categoryRepository.getList(r, 0, 0);
		return l;
	}

	public String getProductCategoryOptionsHTML() {
		StringBuffer sb = new StringBuffer();
		Search<Category> r = new Search<Category>(Category.class);
		List<Category> list = categoryRepository.getList(r, 0, 0);
		for (Category pc : list) {
			sb.append("<option value=\"")
					.append(pc.getTitle())
					.append("\"")
					.append(pc.getTitle().equals(getParams().get(CATEGORIA)) ? " selected=\"selected\""
							: "").append(">").append(pc.getTitle())
					.append("</option>");
		}
		return sb.toString();
	}
}
