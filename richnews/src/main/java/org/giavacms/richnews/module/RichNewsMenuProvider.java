package org.giavacms.richnews.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.common.model.Search;
import org.giavacms.richnews.model.RichNews;
import org.giavacms.richnews.repository.RichNewsRepository;


@Named
@ApplicationScoped
public class RichNewsMenuProvider implements MenuProvider {

	@Inject
	RichNewsRepository richNewsRepository;

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("richnews");
		list.add("latestrichnews");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		if (name.compareTo("richnews") == 0) {
			List<RichNews> list = richNewsRepository.getAllList();
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (RichNews richNews : list) {
					menuList.add(new MenuValue(richNews.getTitle(), ""
							+ richNews.getId()));
				}
			return menuList;
		} else if (name.compareTo("latestrichnews") == 0) {
			Search<RichNews> search = new Search<RichNews>(new RichNews());
			List<RichNews> list = richNewsRepository.getList(search, 0, 0);
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (RichNews richNews : list) {
					menuList.add(new MenuValue(richNews.getTitle(), ""
							+ richNews.getId()));
				}
			return menuList;
		}
		return null;
	}

	@Override
	public String getName() {
		return "catalogue";
	}

}
