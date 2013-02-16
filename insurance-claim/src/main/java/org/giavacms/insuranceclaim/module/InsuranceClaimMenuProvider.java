package org.giavacms.insuranceclaim.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.common.model.Search;
import org.giavacms.insuranceclaim.model.InsuranceClaimCategory;
import org.giavacms.insuranceclaim.model.InsuranceClaimProduct;
import org.giavacms.insuranceclaim.repository.InsuranceClaimCategoryRepository;
import org.giavacms.insuranceclaim.repository.InsuranceClaimProductRepository;

@Named
@ApplicationScoped
public class InsuranceClaimMenuProvider implements MenuProvider {

	@Inject
	InsuranceClaimProductRepository insuranceClaimProductRepository;

	@Inject
	InsuranceClaimCategoryRepository insuranceClaimCategoryRepository;

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("categorie prodotti e sinistri");
		list.add("prodotti e sinistri");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		if (name.compareTo("categorie prodotti e sinistri") == 0) {
			List<InsuranceClaimCategory> list = insuranceClaimCategoryRepository
					.getAllList();
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (InsuranceClaimCategory insuranceClamsCategory : list) {
					menuList.add(new MenuValue(
							insuranceClamsCategory.getName(), ""
									+ insuranceClamsCategory.getId()));
				}
			return menuList;
		} else if (name.compareTo("prodotti e sinistri") == 0) {
			Search<InsuranceClaimProduct> search = new Search<InsuranceClaimProduct>(
					new InsuranceClaimProduct());
			List<InsuranceClaimProduct> list = insuranceClaimProductRepository
					.getList(search, 0, 0);
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (InsuranceClaimProduct product : list) {
					menuList.add(new MenuValue(product.getName(), ""
							+ product.getId()));
				}
			return menuList;
		}
		return null;
	}

	@Override
	public String getName() {
		return "insurance claim";
	}

}
