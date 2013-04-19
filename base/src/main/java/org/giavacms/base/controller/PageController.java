/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.controller.util.PageUtils;
import org.giavacms.base.model.Page;
import org.giavacms.base.model.Template;
import org.giavacms.base.producer.BaseProducer;
import org.giavacms.base.repository.PageRepository;
import org.giavacms.base.repository.TemplateRepository;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;

@Named
@SessionScoped
public class PageController extends AbstractLazyController<Page> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/page/view.xhtml";
	@ListPage
	public static String LIST = "/private/page/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/page/edit.xhtml";

	public static String PREVIEW = "/private/pagine/preview.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(PageRepository.class)
	PageRepository pageRepository;

	@Inject
	BaseProducer baseProducer;

	@Inject
	TemplateRepository templateRepository;

	// --------------------------------------------------------

	/**
	 * Obbligatoria l'invocazione 'appropriata' di questo super construttore
	 * protetto da parte delle sottoclassi
	 */
	public PageController() {
	}

	@Override
	public String reset() {
		baseProducer.setPageItems(null);
		return super.reset();
	}

	@Override
	public Page getElement() {
		if (super.getElement() == null)
			super.setElement(new Page());
		return super.getElement();
	}

	// -----------------------------------------------------

	@Override
	public Object getId(Page t) {
		return t.getId();
	}

	// ------------------------------------------------

	public String addPaginaStatica() {
		setElement(new Page());
		getElement().getTemplate().getTemplate().setStatico(true);
		return editPage();
	}

	public String addPaginaDinamica() {
		setElement(new Page());
		getElement().getTemplate().getTemplate().setStatico(false);
		return editPage();
	}

	// -----------------------------------------------------
	@Override
	public String save() {
		return super.save();
	}

	@Override
	public String update() {
		return super.update();
	}

	@Override
	public String delete() {
		return super.delete();
	}

	public String reallyDelete() {
		pageRepository.reallyDelete(getElement().getId());
		return listPage();
	}

	public String cloneElement() {
		Page original = (Page) getModel().getRowData();
		original = getRepository().fetch(original.getId());
		setEditMode(true);
		setReadOnlyMode(false);
		setElement(PageUtils.clone(original));
		return editPage();
	}

	// -----------------------------------------------------

	public void cambioTemplate() {
		// Long id = getElement().getTemplate().getTemplate().getId();
		Template template = templateRepository.find(getElement().getTemplate()
				.getTemplate().getId());
		getElement().getTemplate().setTemplate(template);
	}

	public String anteprimaTestuale() {
		PageUtils.generateContent(getElement());
		return PREVIEW;
	}

	/**
	 * Necessario salvare per l'anteprima, ma se ridirigessi all'uscita di
	 * questo metodo e non in outputLink causerei la morte di hibernate in caso
	 * di errori nel parser facelet
	 * 
	 * @return
	 */
	public String salvaPerAnteprimaRisultato() {
		if (this.getElement().getId() == null)
			save();
		else
			update();
		return editPage();
	}

}