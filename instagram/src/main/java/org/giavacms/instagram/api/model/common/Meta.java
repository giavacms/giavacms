package org.giavacms.instagram.api.model.common;

import org.codehaus.jackson.annotate.JsonProperty;

public class Meta {
	private int code;
	private String erroeType;
	private String erroeMessage;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Meta [code=" + code + "]";
	}

	@JsonProperty(value = "error_type")
	public String getErroeType() {
		return erroeType;
	}

	public void setErroeType(String erroeType) {
		this.erroeType = erroeType;
	}

	@JsonProperty(value = "error_message")
	public String getErroeMessage() {
		return erroeMessage;
	}

	public void setErroeMessage(String erroeMessage) {
		this.erroeMessage = erroeMessage;
	}

}
