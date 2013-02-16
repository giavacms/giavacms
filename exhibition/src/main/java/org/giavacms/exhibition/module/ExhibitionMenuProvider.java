package org.giavacms.exhibition.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.exhibition.model.Exhibition;
import org.giavacms.exhibition.repository.ExhibitionRepository;

@Named
@ApplicationScoped
public class ExhibitionMenuProvider implements MenuProvider {

	@Inject
	ExhibitionRepository exhibitionRepository;

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("esibizioni");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		if (name.compareTo("esibizioni") == 0) {
			List<Exhibition> list = exhibitionRepository.getAllList();
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (Exhibition exhibition : list) {
					menuList.add(new MenuValue(exhibition.getName(), ""
							+ exhibition.getId()));
				}
			return menuList;
		}
		return null;
	}

	@Override
	public String getName() {
		return "esibizioni";
	}

}
