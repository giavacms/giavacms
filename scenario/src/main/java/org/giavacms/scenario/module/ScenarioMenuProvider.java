package org.giavacms.scenario.module;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.module.MenuProvider;
import org.giavacms.base.common.module.MenuValue;
import org.giavacms.common.model.Search;
import org.giavacms.scenario.model.Scenario;
import org.giavacms.scenario.repository.ScenarioRepository;


@Named
@ApplicationScoped
public class ScenarioMenuProvider implements MenuProvider {

	@Inject
	ScenarioRepository scenarioRepository;

	@Override
	public List<String> getMenuItemSources() {
		List<String> list = new ArrayList<String>();
		list.add("realizzazioni");
		return list;
	}

	@Override
	public List<MenuValue> getMenuItemValues(String name) {
		if (name.compareTo("realizzazioni") == 0) {
			Search<Scenario> search = new Search<Scenario>(new Scenario());
			List<Scenario> list = scenarioRepository.getList(search, 0, 0);
			List<MenuValue> menuList = new ArrayList<MenuValue>();
			if (list != null && list.size() > 0)
				for (Scenario scenario : list) {
					menuList.add(new MenuValue(scenario.getName(), ""
							+ scenario.getId()));
				}
			return menuList;
		}
		return null;
	}

	@Override
	public String getName() {
		return "scenario";
	}

}
