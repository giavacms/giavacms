package org.giavacms.errors.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.errors.model.Errors;
import org.giavacms.errors.producer.ErrorsProducer;
import org.giavacms.errors.repository.ErrorsRepository;
import org.primefaces.event.RowEditEvent;

@Named
@SessionScoped
public class ErrorsController extends AbstractLazyController<Errors> {

	private static final long serialVersionUID = 1L;

	@Inject
	ErrorsProducer errorsProducer;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/errors/view.xhtml";
	@ListPage
	public static String LIST = "/private/errors/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/errors/edit.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(ErrorsRepository.class)
	ErrorsRepository errorsRepository;

	// --------------------------------------------------------

	public ErrorsController() {
	}

	// --------------------------------------------------------

	@Override
	public String save() {
		String outcome = super.save();
		errorsProducer.reset();
		return outcome;
	}

	@Override
	public String update() {
		String outcome = super.update();
		errorsProducer.reset();
		return outcome;
	}

	public void onRowEdit(RowEditEvent ree) {
		Errors error = (Errors) ree.getObject();
		getRepository().update(error);
		errorsProducer.reset();
	}

}
