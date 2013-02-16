package org.giavacms.message.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.message.model.Message;
import org.giavacms.message.repository.MessageRepository;

@Named
@SessionScoped
public class MessageController extends AbstractLazyController<Message> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------

	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/message/view.xhtml";
	@ListPage
	public static String LIST = "/private/message/list.xhtml";
	@EditPage
	public static String NEW_OR_EDIT = "/private/message/edit.xhtml";

	// --------------------------------------------------------

	@Inject
	@OwnRepository(MessageRepository.class)
	MessageRepository messageRepository;

	// --------------------------------------------------------

	public MessageController() {
	}

	// --------------------------------------------------------

	@Override
	public String save() {
		return super.save();
	}

	@Override
	public String delete() {
		return super.delete();
	}

	@Override
	public String update() {
		return super.update();
	}

	@Override
	public Object getId(Message t) {
		return t.getId();
	}
}
