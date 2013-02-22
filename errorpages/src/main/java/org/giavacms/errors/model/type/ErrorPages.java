package org.giavacms.errors.model.type;

public enum ErrorPages {

	/**
	 * This should not happen since pages are always retrieved from db
	 */
	HttpCode304(
			"not modified",
			"the document has not been modified since the date and time specified thus the server does not send the document body to the client"),

	HttpCode400("bad request",
			"the request had bad syntax or was inherently impossible to be satisfied"),

	HttpCode401(
			"unauthorized",
			"the parameter to this message gives a specification of authorization schemes which are acceptable. The client should retry the request with a suitable Authorization header"),

	HttpCode403("forbidden", "the request is for something forbidden"),

	HttpCode404("not found",
			"the server has not found anything matching the URI given"),

	HttpCode500(
			"internal error",
			"the server encountered an unexpected condition which prevented it from fulfilling the request"),

	HttpCode501("not implemented",
			"the server does not support the facility required"),

	HttpCode502(
			"service temporarily overloaded",
			"the server cannot process the request due to a high load, but this is a temporary condition which maybe alleviated at other times"),

	javaLangException("internal error",
			"use this to better specify http code 500"),

	javaLangThrowable("internal error",
			"use this to better specify http code 500"),

	javaLangNullPointerException("internal error",
			"use this to better specify http code 500"),

	;

	String meaning;
	String description;
	String pageContent;

	private ErrorPages(String meaning, String description) {
		this.meaning = meaning;
		this.description = description;
	}

	public String getMeaning() {
		return meaning;
	}

	public String getDescription() {
		return description;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

}
