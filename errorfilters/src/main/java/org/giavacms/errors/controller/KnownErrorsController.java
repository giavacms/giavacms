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
import org.giavacms.errors.model.type.KnownErrors;
import org.giavacms.errors.repository.ErrorsRepository;

@Named
@SessionScoped
public class KnownErrorsController extends AbstractController<KnownErrors> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/knownerrors/view.xhtml";
	@ListPage
	public static String LIST = "/private/knownerrors/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/knownerrors/edit.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(ErrorsRepository.class)
	ErrorsRepository richNewsRepository;

	// --------------------------------------------------------

	public KnownErrorsController() {
	}

	@Override
	public Object getId(KnownErrors t) {
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
		setModel(new ListDataModel<KnownErrors>(getRepository().getAllList()));
	}
}
