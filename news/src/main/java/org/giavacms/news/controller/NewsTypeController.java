package org.giavacms.news.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.news.model.type.NewsType;
import org.giavacms.news.repository.NewsTypeRepository;


@Named
@SessionScoped
public class NewsTypeController extends AbstractLazyController<NewsType> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/amministrazione.xhtml";
	@ViewPage
	public static String VIEW = "/private/news/newstype/view.xhtml";
	@ListPage
	public static String LIST = "/private/news/newtype/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/news/newstype/edit.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(NewsTypeRepository.class)
	NewsTypeRepository tipoInformazioniRepository;

	// ------------------------------------------------

	/**
	 * Obbligatoria l'invocazione 'appropriata' di questo super construttore
	 * protetto da parte delle sottoclassi
	 */
	public NewsTypeController() {

	}

	@Override
	public String reset() {
		// propertiesHandler.setTipoInformazioneItems(null);
		return super.reset();
	}

	// ------------------------------------------------
	@Override
	public String save() {
		super.save();

		return viewPage();
	}

	@Override
	public String update() {
		super.update();
		return viewPage();
	}

	@Override
	public String delete() {
		super.delete();
		return listPage();
	}

}