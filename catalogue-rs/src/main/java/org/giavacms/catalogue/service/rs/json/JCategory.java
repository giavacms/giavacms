package org.giavacms.catalogue.service.rs.json;

import java.io.Serializable;

public class JCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String description;
	private int orderNum;

	private String prop1;
	private String prop2;
	private String prop3;
	private String prop4;
	private String prop5;
	private String prop6;
	private String prop7;
	private String prop8;
	private String prop9;
	private String prop10;

	private String ref1;
	private String ref2;
	private String ref3;
	private String ref4;
	private String ref5;
	private String ref6;
	private String ref7;
	private String ref8;
	private String ref9;
	private String ref10;

	public JCategory() {
	}

	public JCategory(String id, String title, String description, int orderNum,
			String prop1, String prop2, String prop3, String prop4,
			String prop5, String prop6, String prop7, String prop8,
			String prop9, String prop10, String ref1, String ref2, String ref3,
			String ref4, String ref5, String ref6, String ref7, String ref8,
			String ref9, String ref10) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.orderNum = orderNum;
		this.prop1 = prop1;
		this.prop2 = prop2;
		this.prop3 = prop3;
		this.prop4 = prop4;
		this.prop5 = prop5;
		this.prop6 = prop6;
		this.prop7 = prop7;
		this.prop8 = prop8;
		this.prop9 = prop9;
		this.prop10 = prop10;
		this.ref1 = ref1;
		this.ref2 = ref2;
		this.ref3 = ref3;
		this.ref4 = ref4;
		this.ref5 = ref5;
		this.ref6 = ref6;
		this.ref7 = ref7;
		this.ref8 = ref8;
		this.ref9 = ref9;
		this.ref10 = ref10;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getProp1() {
		return prop1;
	}

	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}

	public String getProp2() {
		return prop2;
	}

	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}

	public String getProp3() {
		return prop3;
	}

	public void setProp3(String prop3) {
		this.prop3 = prop3;
	}

	public String getProp4() {
		return prop4;
	}

	public void setProp4(String prop4) {
		this.prop4 = prop4;
	}

	public String getProp5() {
		return prop5;
	}

	public void setProp5(String prop5) {
		this.prop5 = prop5;
	}

	public String getProp6() {
		return prop6;
	}

	public void setProp6(String prop6) {
		this.prop6 = prop6;
	}

	public String getProp7() {
		return prop7;
	}

	public void setProp7(String prop7) {
		this.prop7 = prop7;
	}

	public String getProp8() {
		return prop8;
	}

	public void setProp8(String prop8) {
		this.prop8 = prop8;
	}

	public String getProp9() {
		return prop9;
	}

	public void setProp9(String prop9) {
		this.prop9 = prop9;
	}

	public String getProp10() {
		return prop10;
	}

	public void setProp10(String prop10) {
		this.prop10 = prop10;
	}

	public String getRef1() {
		return ref1;
	}

	public void setRef1(String ref1) {
		this.ref1 = ref1;
	}

	public String getRef2() {
		return ref2;
	}

	public void setRef2(String ref2) {
		this.ref2 = ref2;
	}

	public String getRef3() {
		return ref3;
	}

	public void setRef3(String ref3) {
		this.ref3 = ref3;
	}

	public String getRef4() {
		return ref4;
	}

	public void setRef4(String ref4) {
		this.ref4 = ref4;
	}

	public String getRef5() {
		return ref5;
	}

	public void setRef5(String ref5) {
		this.ref5 = ref5;
	}

	public String getRef6() {
		return ref6;
	}

	public void setRef6(String ref6) {
		this.ref6 = ref6;
	}

	public String getRef7() {
		return ref7;
	}

	public void setRef7(String ref7) {
		this.ref7 = ref7;
	}

	public String getRef8() {
		return ref8;
	}

	public void setRef8(String ref8) {
		this.ref8 = ref8;
	}

	public String getRef9() {
		return ref9;
	}

	public void setRef9(String ref9) {
		this.ref9 = ref9;
	}

	public String getRef10() {
		return ref10;
	}

	public void setRef10(String ref10) {
		this.ref10 = ref10;
	}

	@Override
	public String toString() {
		return "JCategory ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (title != null ? "title=" + title + ", " : "")
				+ (description != null ? "description=" + description + ", "
						: "") + "orderNum=" + orderNum + ", "
				+ (prop1 != null ? "prop1=" + prop1 + ", " : "")
				+ (prop2 != null ? "prop2=" + prop2 + ", " : "")
				+ (prop3 != null ? "prop3=" + prop3 + ", " : "")
				+ (prop4 != null ? "prop4=" + prop4 + ", " : "")
				+ (prop5 != null ? "prop5=" + prop5 + ", " : "")
				+ (prop6 != null ? "prop6=" + prop6 + ", " : "")
				+ (prop7 != null ? "prop7=" + prop7 + ", " : "")
				+ (prop8 != null ? "prop8=" + prop8 + ", " : "")
				+ (prop9 != null ? "prop9=" + prop9 + ", " : "")
				+ (prop10 != null ? "prop10=" + prop10 + ", " : "")
				+ (ref1 != null ? "ref1=" + ref1 + ", " : "")
				+ (ref2 != null ? "ref2=" + ref2 + ", " : "")
				+ (ref3 != null ? "ref3=" + ref3 + ", " : "")
				+ (ref4 != null ? "ref4=" + ref4 + ", " : "")
				+ (ref5 != null ? "ref5=" + ref5 + ", " : "")
				+ (ref6 != null ? "ref6=" + ref6 + ", " : "")
				+ (ref7 != null ? "ref7=" + ref7 + ", " : "")
				+ (ref8 != null ? "ref8=" + ref8 + ", " : "")
				+ (ref9 != null ? "ref9=" + ref9 + ", " : "")
				+ (ref10 != null ? "ref10=" + ref10 : "") + "]";
	}
}
