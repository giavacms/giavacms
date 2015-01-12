package org.giavacms.banner.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.banner.model.Banner;
import org.giavacms.banner.model.BannerTypology;
import org.giavacms.banner.repository.BannerRepository;
import org.giavacms.banner.repository.BannerTypologyRepository;
import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.common.model.Search;

@Named
@ApplicationScoped
public class BannerMenuProvider implements MenuProvider {

	@Inject
	BannerRepository bannerRepository;

	@Inject
	BannerTypologyRepository bannerTypologyRepository;

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("tipologie banner");
		list.add("banner");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		if (name.compareTo("tipologie banner") == 0) {
			List<BannerTypology> list = bannerTypologyRepository.getAllList();
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (BannerTypology bannerTypology : list) {
					menuList.add(new MenuValue(bannerTypology.getName(), ""
							+ bannerTypology.getId()));
				}
			return menuList;
		} else if (name.compareTo("banner") == 0) {
			Search<Banner> search = new Search<Banner>(new Banner());
			List<Banner> list = bannerRepository.getList(search, 0, 0);
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (Banner banner : list) {
					menuList.add(new MenuValue(banner.getName(), ""
							+ banner.getId()));
				}
			return menuList;
		}
		return null;
	}

	@Override
	public String getName() {
		return "banner";
	}

}
