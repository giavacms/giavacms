package org.giavacms.catalogue.service.rs.util;

import java.util.ArrayList;
import java.util.List;

import org.giavacms.catalogue.model.Category;
import org.giavacms.catalogue.model.Product;
import org.giavacms.catalogue.service.rs.json.JProduct;
import org.giavacms.catalogue.service.rs.json.JCategory;

public class JsonUtils {
	public static JCategory getJCategory(Category category) {
		JCategory jCategory = new JCategory(category.getId(),
				category.getTitle(), category.getDescription(),
				category.getOrderNum(), category.getProp1(),
				category.getProp2(), category.getProp3(), category.getProp4(),
				category.getProp5(), category.getProp6(), category.getProp7(),
				category.getProp8(), category.getProp9(), category.getProp10(),
				category.getRef1(), category.getRef2(), category.getRef3(),
				category.getRef4(), category.getRef5(), category.getRef6(),
				category.getRef7(), category.getRef8(), category.getRef9(),
				category.getRef10());
		return jCategory;
	}

	public static JProduct getJProduct(Product product) {
		JProduct jProduct = new JProduct(product.getId(), product.getTitle(),
				product.getPreview(), product.getDescription(),
				getJCategory(product.getCategory()), product.getDimensions(),
				product.getCode(), product.getDocuments(), product.getImages(),
				product.getPrice(), product.getVat(), product.getVal1(),
				product.getVal2(), product.getVal3(), product.getVal4(),
				product.getVal5(), product.getVal6(), product.getVal7(),
				product.getVal8(), product.getVal9(), product.getVal10());
		return jProduct;
	}

	public static List<JProduct> getJProducts(List<Product> products) {
		List<JProduct> jproducts = new ArrayList<JProduct>();
		for (Product product : products) {
			jproducts.add(getJProduct(product));
		}
		return jproducts;

	}

	public static List<JCategory> getJCategories(List<Category> categories) {
		List<JCategory> jcategories = new ArrayList<JCategory>();
		for (Category category : categories) {
			jcategories.add(getJCategory(category));
		}
		return jcategories;

	}
}
