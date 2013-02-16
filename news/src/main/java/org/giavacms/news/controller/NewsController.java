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
import org.giavacms.news.model.News;
import org.giavacms.news.repository.NewsRepository;


@Named
@SessionScoped
public class NewsController extends AbstractLazyController<News> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/amministrazione.xhtml";
	@ViewPage
	public static String VIEW = "/private/news/view.xhtml";
	@ListPage
	public static String LIST = "/private/news/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/news/edit.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(NewsRepository.class)
	NewsRepository newsRepository;

	// --------------------------------------------------------

	public NewsController() {
	}

	// --------------------------------------------------------
	@Override
	public String save() {

		super.save();

		return viewPage();
	}

	@Override
	public String delete() {
		super.delete();

		return listPage();
	}

	@Override
	public String update() {
		super.update();

		return viewPage();
	}

	// // -----------------------------------------------------------
	//
	// private LocalDataModel<Notizia> latestNewsModel;
	// private String latestTipo;
	// private Integer latestPageSize;
	//
	// public LocalDataModel<Notizia> ultimeNotizie(String tipo, int pageSize) {
	// if (latestNewsModel == null || this.latestTipo == null
	// || this.latestPageSize == null || !this.latestTipo.equals(tipo)
	// || this.latestPageSize != pageSize) {
	// Ricerca<Notizia> ricerca = new Ricerca<Notizia>(Notizia.class);
	// ricerca.getOggetto().setTipo(new TipoInformazione());
	// ricerca.getOggetto().getTipo().setNome(tipo);
	// latestNewsModel = new LocalDataModel<Notizia>(pageSize, ricerca,
	// notizieSession);
	// }
	// return latestNewsModel;
	// }
	//
	// // -----------------------------------------------------------

}
