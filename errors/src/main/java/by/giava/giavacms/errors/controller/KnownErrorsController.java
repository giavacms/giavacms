package by.giava.giavacms.errors.controller;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import by.giava.common.annotation.BackPage;
import by.giava.common.annotation.EditPage;
import by.giava.common.annotation.ListPage;
import by.giava.common.annotation.OwnRepository;
import by.giava.common.annotation.ViewPage;
import by.giava.common.controller.AbstractController;
import by.giava.giavacms.errors.model.type.KnownErrors;
import by.giava.giavacms.errors.repository.ErrorsRepository;

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
