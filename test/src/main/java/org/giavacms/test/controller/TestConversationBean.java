package org.giavacms.test.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ConversationScoped
public class TestConversationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	Conversation conversation;

	private String name;

	public TestConversationBean() {

	}

	@PostConstruct
	private void init() {
		conversation.begin();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSayWelcome() {
		// check if null?
		if ("".equals(name) || name == null) {
			return "";
		} else {
			return "Ajax message : Welcome " + name;
		}
	}

	public void terminate() {
		conversation.end();
	}

	@PreDestroy
	public void destroy() {
		conversation.end();
	}
}