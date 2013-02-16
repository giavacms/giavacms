package org.giavacms.catalogue.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.producer.CatalogueProducer;
import org.giavacms.catalogue.repository.CategoryRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;


@Named
@SessionScoped
public class CategoryController extends AbstractLazyController<Category> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/catalogue/category/list.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(CategoryRepository.class)
	CategoryRepository categoryRepository;

	@Inject
	CatalogueProducer catalogueProducer;

	@Override
	public void initController() {
		if (getElement() == null) {
			setElement(new Category());
		}
	}

	@Override
	public String save() {
		logger.info("name: " + getElement().getName());
		logger.info("description: " + getElement().getDescription());
		catalogueProducer.reset();
		super.save();
		setElement(new Category());
		return listPage();
	}

	@Override
	public Object getId(Category t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public String reset() {
		catalogueProducer.reset();
		return super.reset();
	}
}
