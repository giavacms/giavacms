package org.giavacms.richcontent.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.richcontent.model.type.RichContentType;
import org.giavacms.richcontent.producer.RichContentProducer;
import org.giavacms.richcontent.repository.RichContentTypeRepository;


@Named
@SessionScoped
public class RichContentTypeController extends
		AbstractLazyController<RichContentType> {
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/administration.xhtml";

	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/richcontent/richcontenttype/list.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(RichContentTypeRepository.class)
	RichContentTypeRepository richContentTypeRepository;

	@Inject
	RichContentProducer richContentProducer;

	// --------------------------------------------------------

	public RichContentTypeController() {
	}

	// --------------------------------------------------------

	@Override
	public String reset() {

		return super.reset();
	}

	@Override
	public String save() {
		richContentProducer.reset();
		super.save();
		setElement(new RichContentType());
		return super.viewPage();
	}

	@Override
	public String update() {
		richContentProducer.reset();
		super.update();
		return super.viewPage();
	}

	@Override
	public RichContentType getElement() {
		if (super.getElement() == null) {
			setElement(new RichContentType());
		}
		return super.getElement();
	}

	@Override
	public Object getId(RichContentType t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public void deleteInline() {
		richContentProducer.reset();
		// TODO Auto-generated method stub
		super.deleteInline();
	}
}
