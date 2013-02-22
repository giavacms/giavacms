package org.giavacms.test.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class TestBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

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
}