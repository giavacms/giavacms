package org.giavacms.exhibition.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.exhibition.model.Discipline;
import org.giavacms.exhibition.producer.ExhibitionProducer;
import org.giavacms.exhibition.repository.DisciplineRepository;

@Named
@SessionScoped
public class DisciplineController extends AbstractLazyController<Discipline> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/exhibition/discipline/list.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(DisciplineRepository.class)
	DisciplineRepository disciplineRepository;

	@Inject
	ExhibitionProducer exhibitionProducer;

	// --------------------------------------------------------

	@Override
	public void initController() {
		if (getElement() == null) {
			setElement(new Discipline());
		}
	}

	@Override
	public String save() {
		super.save();
		exhibitionProducer.reset();
		setElement(new Discipline());
		return listPage();
	}

	@Override
	public String delete() {
		exhibitionProducer.reset();
		return listPage();
	}

	@Override
	public String update() {
		super.update();
		exhibitionProducer.reset();
		return listPage();
	}

}
