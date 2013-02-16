package by.giava.giavacms.errors.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import by.giava.common.annotation.BackPage;
import by.giava.common.annotation.EditPage;
import by.giava.common.annotation.ListPage;
import by.giava.common.annotation.OwnRepository;
import by.giava.common.annotation.ViewPage;
import by.giava.common.controller.AbstractLazyController;
import by.giava.giavacms.errors.model.Errors;
import by.giava.giavacms.errors.producer.ErrorsProducer;
import by.giava.giavacms.errors.repository.ErrorsRepository;

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
