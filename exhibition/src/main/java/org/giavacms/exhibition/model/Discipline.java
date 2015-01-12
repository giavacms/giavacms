package org.giavacms.exhibition.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ExhibitionDiscipline")
public class Discipline implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private int num;

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "Discipline [id=" + id + ", name=" + name + ", num=" + num + "]";
	}

}
