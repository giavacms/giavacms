package org.giavacms.richnews.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.richnews.model.type.RichNewsType;
import org.giavacms.richnews.producer.RichNewsProducer;
import org.giavacms.richnews.repository.RichNewsTypeRepository;


@Named
@SessionScoped
public class RichNewsTypeController extends
		AbstractLazyController<RichNewsType> {
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/administration.xhtml";

	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/richnews/richnewstype/list.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(RichNewsTypeRepository.class)
	RichNewsTypeRepository richNewsTypeRepository;

	@Inject
	RichNewsProducer richNewsProducer;

	// --------------------------------------------------------

	public RichNewsTypeController() {
	}

	// --------------------------------------------------------

	@Override
	public String reset() {

		return super.reset();
	}

	@Override
	public String save() {
		richNewsProducer.reset();
		super.save();
		setElement(new RichNewsType());
		return super.viewPage();
	}

	@Override
	public String update() {
		richNewsProducer.reset();
		super.update();
		return super.viewPage();
	}

	@Override
	public RichNewsType getElement() {
		if (super.getElement() == null) {
			setElement(new RichNewsType());
		}
		return super.getElement();
	}

	@Override
	public Object getId(RichNewsType t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public void deleteInline() {
		richNewsProducer.reset();
		// TODO Auto-generated method stub
		super.deleteInline();
	}
}
