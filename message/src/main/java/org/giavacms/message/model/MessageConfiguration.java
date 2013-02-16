package org.giavacms.message.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MessageConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private boolean approve;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}

	@Override
	public String toString() {
		return "MessageConfiguration [id=" + id + ", approve=" + approve + "]";
	}

}
