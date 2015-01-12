package org.giavacms.catalogue.service.rs.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.giavacms.base.model.attachment.Document;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.catalogue.model.Category;

public class JProduct implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String preview;
	private String description;
	private JCategory category;
	private String dimensions;
	private String code;
	private List<Document> documents;
	private List<Image> images;
	private String price;
	private String vat;

	private String val1;
	private String val2;
	private String val3;
	private String val4;
	private String val5;
	private String val6;
	private String val7;
	private String val8;
	private String val9;
	private String val10;

	public JProduct() {
	}

	public JProduct(String id, String title, String preview,
			String description, JCategory category, String dimensions,
			String code, List<Document> documents, List<Image> images,
			String price, String vat, String val1, String val2, String val3,
			String val4, String val5, String val6, String val7, String val8,
			String val9, String val10) {
		this.id = id;
		this.title = title;
		this.preview = preview;
		this.description = description;
		this.category = category;
		this.dimensions = dimensions;
		this.code = code;
		this.documents = documents;
		this.images = images;
		this.price = price;
		this.vat = vat;
		this.val1 = val1;
		this.val2 = val2;
		this.val3 = val3;
		this.val4 = val4;
		this.val5 = val5;
		this.val6 = val6;
		this.val7 = val7;
		this.val8 = val8;
		this.val9 = val9;
		this.val10 = val10;
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

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public JCategory getCategory() {
		return category;
	}

	public void setCategory(JCategory category) {
		this.category = category;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Document> getDocuments() {
		if (documents == null)
			this.documents = new ArrayList<Document>();
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public List<Image> getImages() {
		if (images == null)
			this.images = new ArrayList<Image>();
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getVal1() {
		return val1;
	}

	public void setVal1(String val1) {
		this.val1 = val1;
	}

	public String getVal2() {
		return val2;
	}

	public void setVal2(String val2) {
		this.val2 = val2;
	}

	public String getVal3() {
		return val3;
	}

	public void setVal3(String val3) {
		this.val3 = val3;
	}

	public String getVal4() {
		return val4;
	}

	public void setVal4(String val4) {
		this.val4 = val4;
	}

	public String getVal5() {
		return val5;
	}

	public void setVal5(String val5) {
		this.val5 = val5;
	}

	public String getVal6() {
		return val6;
	}

	public void setVal6(String val6) {
		this.val6 = val6;
	}

	public String getVal7() {
		return val7;
	}

	public void setVal7(String val7) {
		this.val7 = val7;
	}

	public String getVal8() {
		return val8;
	}

	public void setVal8(String val8) {
		this.val8 = val8;
	}

	public String getVal9() {
		return val9;
	}

	public void setVal9(String val9) {
		this.val9 = val9;
	}

	public String getVal10() {
		return val10;
	}

	public void setVal10(String val10) {
		this.val10 = val10;
	}

	@Override
	public String toString() {
		return "JProduct [" + (id != null ? "id=" + id + ", " : "")
				+ (title != null ? "title=" + title + ", " : "")
				+ (preview != null ? "preview=" + preview + ", " : "")
				+ (category != null ? "category=" + category + ", " : "")
				+ (dimensions != null ? "dimensions=" + dimensions + ", " : "")
				+ (code != null ? "code=" + code + ", " : "")
				+ (price != null ? "price=" + price + ", " : "")
				+ (vat != null ? "vat=" + vat + ", " : "")
				+ (val1 != null ? "val1=" + val1 + ", " : "")
				+ (val2 != null ? "val2=" + val2 + ", " : "")
				+ (val3 != null ? "val3=" + val3 + ", " : "")
				+ (val4 != null ? "val4=" + val4 + ", " : "")
				+ (val5 != null ? "val5=" + val5 + ", " : "")
				+ (val6 != null ? "val6=" + val6 + ", " : "")
				+ (val7 != null ? "val7=" + val7 + ", " : "")
				+ (val8 != null ? "val8=" + val8 + ", " : "")
				+ (val9 != null ? "val9=" + val9 + ", " : "")
				+ (val10 != null ? "val10=" + val10 : "") + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
