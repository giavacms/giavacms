package org.giavacms.picasa.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;

@Named
@ApplicationScoped
public class PicasaMenuProvider implements MenuProvider {

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("picasa collections");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		return null;
	}

	@Override
	public String getName() {
		return "picasa";
	}

}
