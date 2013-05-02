package org.giavacms.customer.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.customer.model.CustomerCategory;
import org.giavacms.customer.producer.CustomerProducer;
import org.giavacms.customer.repository.CustomerCategoryRepository;

@Named
@SessionScoped
public class CustomerCategoryController extends AbstractLazyController<CustomerCategory> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/customer/category/list.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(CustomerCategoryRepository.class)
	CustomerCategoryRepository categoryRepository;

	@Inject
	CustomerProducer customerProducer;

	@Override
	public void initController() {
		if (getElement() == null) {
			setElement(new CustomerCategory());
		}
	}

	@Override
	public String save() {
		logger.info("name: " + getElement().getName());
		logger.info("description: " + getElement().getDescription());
		customerProducer.reset();
		super.save();
		setElement(new CustomerCategory());
		return listPage();
	}

	@Override
	public Object getId(CustomerCategory t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public String reset() {
		customerProducer.reset();
		return super.reset();
	}
}
