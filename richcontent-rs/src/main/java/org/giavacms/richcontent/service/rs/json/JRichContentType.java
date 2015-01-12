package org.giavacms.richcontent.service.rs.json;

import java.io.Serializable;

public class JRichContentType implements Serializable {

	private static final long serialVersionUID = 1L;

	public JRichContentType() {
	}

	public JRichContentType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "JRichContentType [" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name : "") + "]";
	}

}
