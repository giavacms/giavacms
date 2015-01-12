package org.giavacms.richcontent.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.common.model.Search;
import org.giavacms.richcontent.model.RichContent;
import org.giavacms.richcontent.repository.RichContentRepository;


@Named
@ApplicationScoped
public class RichContentMenuProvider implements MenuProvider {

	@Inject
	RichContentRepository richContentRepository;

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("richcontent");
		list.add("latestrichcontent");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		if (name.compareTo("richcontent") == 0) {
			List<RichContent> list = richContentRepository.getAllList();
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (RichContent richContent : list) {
					menuList.add(new MenuValue(richContent.getTitle(), ""
							+ richContent.getId()));
				}
			return menuList;
		} else if (name.compareTo("latestrichcontent") == 0) {
			Search<RichContent> search = new Search<RichContent>(new RichContent());
			List<RichContent> list = richContentRepository.getList(search, 0, 0);
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (RichContent richContent : list) {
					menuList.add(new MenuValue(richContent.getTitle(), ""
							+ richContent.getId()));
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
