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
import org.giavacms.message.model.MessageConfiguration;
import org.giavacms.message.repository.MessageConfigurationRepository;

@Named
@SessionScoped
public class MessageConfigurationController extends
		AbstractLazyController<MessageConfiguration> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	@ListPage
	@EditPage
	public static String LIST = "/private/message/configuration.xhtml";

	// ------------------------------------------------

	
	@Inject
	@OwnRepository(MessageConfigurationRepository.class)
	MessageConfigurationRepository messageConfigurationRepository;

	@Override
	public MessageConfiguration getElement() {
		if (super.getElement() == null)
			setElement(messageConfigurationRepository.load());
		return super.getElement();
	}

	@Override
	public Object getId(MessageConfiguration t) {
		return t.getId();
	}

}
