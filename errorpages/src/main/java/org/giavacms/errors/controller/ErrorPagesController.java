package org.giavacms.errors.controller;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractController;
import org.giavacms.errors.model.type.ErrorPages;
import org.giavacms.errors.repository.ErrorPagesRepository;

@Named
@SessionScoped
public class ErrorPagesController extends AbstractController<ErrorPages> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/errorpages/view.xhtml";
	@ListPage
	public static String LIST = "/private/errorpages/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/errorpages/edit.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(ErrorPagesRepository.class)
	ErrorPagesRepository errorPagesRepository;

	// --------------------------------------------------------

	public ErrorPagesController() {
	}

	@Override
	public Object getId(ErrorPages t) {
		return t.name();
	}

	@Override
	public String update() {
		String outcome = super.update();
		if (outcome == null) {
			super.addFacesMessage("Errori nel salvataggio della pagina;");
			return null;
		}
		return outcome;
	}

	@Override
	public void refreshModel() {
		setModel(new ListDataModel<ErrorPages>(getRepository().getAllList()));
	}
}
