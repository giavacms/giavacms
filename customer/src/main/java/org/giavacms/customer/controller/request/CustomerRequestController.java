package org.giavacms.customer.controller.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.controller.AbstractRequestController;
import org.giavacms.common.model.Search;
import org.giavacms.customer.model.Customer;
import org.giavacms.customer.model.CustomerCategory;
import org.giavacms.customer.repository.CustomerCategoryRepository;
import org.giavacms.customer.repository.CustomerRepository;

@Named
@RequestScoped
public class CustomerRequestController extends
		AbstractRequestController<Customer> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CATEGORIA = "categoria";
	public static final String SEARCH = "q";
	public static final String[] PARAM_NAMES = new String[] { CATEGORIA, SEARCH };
	public static final String ID_PARAM = "id";
	public static final String CURRENT_PAGE_PARAM = "start";

	@Inject
	@OwnRepository(CustomerRepository.class)
	CustomerRepository customerRepository;

	@Inject
	CustomerCategoryRepository categoryRepository;

	public CustomerRequestController() {
		super();
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public List<Customer> loadPage(int startRow, int pageSize) {
		Search<Customer> r = new Search<Customer>(Customer.class);
		r.getObj().setName(getParams().get(SEARCH));
		r.getObj().getCategory().setName(getParams().get(CATEGORIA));
		return customerRepository.getList(r, startRow, pageSize);
	}

	@Override
	public int totalSize() {
		// siamo all'interno della stessa richiesta per servire la quale è
		// avvenuta la postconstruct
		Search<Customer> r = new Search<Customer>(Customer.class);
		r.getObj().getCategory().setName(getParams().get(CATEGORIA));
		r.getObj().setName(getParams().get(SEARCH));
		return customerRepository.getListSize(r);
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
		setElement(customerRepository.fetch(id));
		return viewPage();
	}

	public List<String> getCustomerCategories() {
		Search<CustomerCategory> r = new Search<CustomerCategory>(
				CustomerCategory.class);
		List<CustomerCategory> list = categoryRepository.getList(r, 0, 0);
		List<String> l = new ArrayList<String>();
		for (CustomerCategory rnt : list) {
			l.add(rnt.getName());
		}
		return l;
	}

	public List<CustomerCategory> getAllCategories() {
		Search<CustomerCategory> r = new Search<CustomerCategory>(
				CustomerCategory.class);
		List<CustomerCategory> l = categoryRepository.getList(r, 0, 0);
		return l;
	}

	public String getCustomerCategoryOptionsHTML() {
		StringBuffer sb = new StringBuffer();
		Search<CustomerCategory> r = new Search<CustomerCategory>(
				CustomerCategory.class);
		List<CustomerCategory> list = categoryRepository.getList(r, 0, 0);
		for (CustomerCategory pc : list) {
			sb.append("<option value=\"")
					.append(pc.getName())
					.append("\"")
					.append(pc.getName().equals(getParams().get(CATEGORIA)) ? " selected=\"selected\""
							: "").append(">").append(pc.getName())
					.append("</option>");
		}
		return sb.toString();
	}
}
