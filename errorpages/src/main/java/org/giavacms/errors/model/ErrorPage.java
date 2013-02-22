package org.giavacms.errors.model;

import java.io.Serializable;

import org.giavacms.errors.model.type.HttpError;

public class ErrorPage implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PATH = "errors";

	public ErrorPage() {
	}

	public ErrorPage(HttpError httpError) {
		super();
		this.httpError = httpError;
	}

	public ErrorPage(HttpError httpError, String pageContent) {
		super();
		this.httpError = httpError;
		this.pageContent = pageContent;
	}

	HttpError httpError;
	String pageContent;

	public HttpError getHttpError() {
		return httpError;
	}

	public void setHttpError(HttpError httpError) {
		this.httpError = httpError;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	public String getPath() {
		return PATH;
	}

}
